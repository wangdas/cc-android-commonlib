package com.bokecc.common.exception;

import android.text.TextUtils;

import com.bokecc.common.application.ApplicationData;
import com.bokecc.common.crash.CCCrashManager;
import com.bokecc.common.log.CCLogConfig;
import com.bokecc.common.utils.Tools;
import com.bokecc.common.utils.FileUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;

//import javax.validation.constraints.NotNull;


/**
 * 自定义全局异常类
 * UncaughtExceptionHandler
 *
 * @author wangyue
 */
public class CrashException implements UncaughtExceptionHandler {

    private final UncaughtExceptionHandler defaultExceptionHandler;

    private String crashPackageName;

    private String crashClassName;

    private String exceptionType;

    private String exceptionContent;

    private CrashException() {
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static CrashException getInstance() {
        return CrashException.CrashExceptionHolder.INSTANCE;
    }

    private static class CrashExceptionHolder {
        private static final CrashException INSTANCE = new CrashException();
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable e) {
        if (defaultExceptionHandler == null) {
            return;
        }
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            String className = stackTraceElement.getClassName();
            if (className == null || TextUtils.isEmpty(className)) {
                continue;
            }
            if (className.contains(CCLogConfig.ORIGIN)) {
                crashClassName = stackTraceElement.getClassName();
                break;
            }
        }
        if (TextUtils.isEmpty(crashClassName) && stackTrace.length != 0) {
            crashClassName = stackTrace[0].getClassName();
        }
        crashPackageName = Tools.getPackageNameByClassName(crashClassName);
        HashMap<Object, Object> hashMap = new HashMap<>(16);
        parseExceptionInfo(e, hashMap);
        if (shouldInterrupterException()) {
            return;
        }
        fillBusinessInfo(hashMap);
        String jsonString = hashMap.toString();
        saveErrorLog(jsonString);
        defaultExceptionHandler.uncaughtException(thread, e);
    }

    /**
     * 针对不引起crash的异常进行拦截
     */
    private boolean shouldInterrupterException() {
        return TextUtils.equals(exceptionType, IllegalStateException.class.getName()) &&
                exceptionContent.contains("android.media.MediaPlayer.isPlaying(Native Method)") &&
                exceptionContent.contains("com.bokecc.sdk.mobile");
    }


    /**
     * 填充crash时，exception的相关信息
     *
     * @param e       当前抛出的异常
     * @param hashMap 解析异常相关信息，并存储在当前hashMap中
     */
    private void parseExceptionInfo(Throwable e, HashMap<Object, Object> hashMap) {
        //将异常信息输出
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        exceptionContent = sw.toString();
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < stackTrace.length; i++) {
            sb.append(stackTrace[i]);
        }
        exceptionType = getExceptionTypeByCausedBy(sw.toString());
        if (TextUtils.isEmpty(exceptionType)) {
            String[] splitForType = sw.toString().split(":");
            exceptionType = splitForType.length > 0 ? splitForType[0] : "";
        }
        hashMap.put("app_version", Tools.getAppVersionName());
        hashMap.put("app_package_name", Tools.getAppPackageName());
        hashMap.put("crash_class_name", crashClassName);
        hashMap.put("crash_package_name", Tools.getPackageNameByClassName(crashClassName));
        hashMap.put("crash_time", System.currentTimeMillis());
        hashMap.put("crash_origin", crashBySdk() ? 1 : 2);
        hashMap.put("exception_type", exceptionType);
        hashMap.put("exception_content", sw.toString());
    }

    private String getExceptionTypeByCausedBy(String swString) {
        //TODO caused by获取可能会存在不包含:的情况，而是换行，需兼容
        if (swString.contains(CCLogConfig.CAUSED_BY)) {
            String[] splitForType = swString.split(CCLogConfig.CAUSED_BY);
            if (splitForType.length > 1) {
                String causedBy = splitForType[1];
                String[] causedArray = causedBy.split(":");
                if (causedArray.length > 1) {
                    exceptionType = causedArray[1];
                }
            }
        }
        return "";
    }

    private boolean crashBySdk() {
        if (crashPackageName != null && !TextUtils.isEmpty(crashPackageName)) {
            return crashPackageName.contains(CCLogConfig.ORIGIN) || exceptionContent.contains(CCLogConfig.ORIGIN);
        }
        return false;
    }

    /**
     * 填充业务相关字段信息
     *
     * @param hashMap 存储在当前hashMap中
     */
    private void fillBusinessInfo(HashMap<Object, Object> hashMap) {
        hashMap.put("ver", CCLogConfig.VERSION_NAME);
        if (TextUtils.equals(crashPackageName, CCLogConfig.VOD_SDK)) {
            hashMap.put("appVer", CCCrashManager.getInstance().getVodSdkVersion());
        } else if (TextUtils.equals(crashPackageName, CCLogConfig.LIVE_SDK)) {
            hashMap.put("appVer", CCCrashManager.getInstance().getLiveSdkVersion());
        } else if (TextUtils.equals(crashPackageName, CCLogConfig.CLASS_SDK)) {
            hashMap.put("appVer", CCCrashManager.getInstance().getClassSdkVersion());
        } else {
            hashMap.put("appVer", CCCrashManager.getInstance().getBusinessSdkVersion());
        }
        hashMap.put("business", CCCrashManager.getInstance().getBusiness());
        hashMap.put("event", "crash");
        HashMap<Object, Object> businessParams = CCCrashManager.getInstance().getBusinessParams();
        if (businessParams != null && !businessParams.isEmpty()) {
            hashMap.putAll(businessParams);
        }
    }

    /**
     * 保存错误日志
     *
     * @param content content
     */
    private void saveErrorLog(String content) {
        try {
            File file = new File(Tools.getSdcardRootPath(ApplicationData.globalContext) + CCLogConfig.CRASH_FILE_PATH);
            FileUtil.writeDataToSdcard(content.getBytes(), file.getPath(), "/" + System.currentTimeMillis() + ".txt", false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
