package com.bokecc.common.crash;


import android.text.TextUtils;

import com.bokecc.common.application.ApplicationData;
import com.bokecc.common.log.CCLogConfig;
import com.bokecc.common.utils.Tools;

import java.io.File;
import java.util.HashMap;

/**
 * CCCrashManager
 *
 * @author Zhang
 */
public class CCCrashManager {

    /**
     * 点播
     */
    public static final String BUSINESS_VOD = "1001";
    /**
     * 直播
     */
    public static final String BUSINESS_LIVE = "2001";
    /**
     * 班课
     */
    public static final String BUSINESS_CLASS = "3001";

    private boolean vodFirstReport, liveFirstReport, classFirstReport;

    private HashMap<Object, Object> businessParams;

    /**
     * 业务线标识
     */
    private String business;

    /**
     * 业务线sdk版本号
     */
    private String businessSdkVersion;
    /**
     * 各业务线sdk的版本号
     */
    private String liveSdkVersion, vodSdkVersion, classSdkVersion;

    private static volatile boolean UPLOAD_CRASH_STATUE = false;

    private CCCrashManager() {
    }

    public static CCCrashManager getInstance() {
        return CCCrashManager.CrashManagerHolder.INSTANCE;
    }

    /**
     * app启动的上报事件
     */
    public void reportLaunchApp() {
        new CCUsageTimeRequest(CCUsageTimeRequest.APP_INITIALIZE);
    }

    public void reportExecute() {
        if (ApplicationData.getDebugStatue()) {
            return;
        }
        if (businessParams != null) {
            Object businessParam = businessParams.get("business");
            if (businessParam != null) {
                this.business = (String) businessParams.get("business");
            }
        }
        if (TextUtils.equals(business, CCLogConfig.BUSINESS_VOD)) {
            if (!vodFirstReport) {
                reportInitialize();
                vodFirstReport = true;
            }
        } else if (TextUtils.equals(business, CCLogConfig.BUSINESS_LIVE)) {
            if (!liveFirstReport) {
                reportInitialize();
                liveFirstReport = true;
            }
        } else if (TextUtils.equals(business, CCLogConfig.BUSINESS_CLASS)) {
            if (!classFirstReport) {
                reportInitialize();
                classFirstReport = true;
            }
        }
        reportInvoke();
    }

    /**
     * 业务SDK首次初始化使用调用
     */
    private void reportInitialize() {
        new CCUsageTimeRequest(CCUsageTimeRequest.FIRST_EXECUTE);
    }

    /**
     * 业务SDK每次使用调用
     */
    private void reportInvoke() {
        new CCUsageTimeRequest(CCUsageTimeRequest.EXECUTE);
    }

    private static class CrashManagerHolder {
        private static final CCCrashManager INSTANCE = new CCCrashManager();
    }

    /**
     * crash上报
     */
    public synchronized void reportCrash() {
        if (UPLOAD_CRASH_STATUE) {
            return;
        }
        UPLOAD_CRASH_STATUE = true;
        synchronized (new Object()) {
            File parentFile = new File(Tools.getSdcardRootPath(ApplicationData.globalContext) + CCLogConfig.CRASH_FILE_PATH);
            if (!parentFile.exists()) {
                return;
            }
            File[] files = parentFile.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            for (File file : files) {
                if (file.length() > 512 * 1024) {
                    boolean delete = file.delete();
                    continue;
                }
                new CCCrashRequest(file);
            }
        }
    }

    /**
     * 单独提供business,appId,sdkVersion设置方法，无需重复设置
     *
     * @param business           business
     * @param businessSdkVersion businessSdkVersion
     */
    public void setBaseInfo(String business, String businessSdkVersion) {
        this.business = business;
        this.businessSdkVersion = businessSdkVersion;
        if (TextUtils.equals(business, CCLogConfig.BUSINESS_VOD)) {
            vodSdkVersion = businessSdkVersion;
        } else if (TextUtils.equals(business, CCLogConfig.BUSINESS_LIVE)) {
            liveSdkVersion = businessSdkVersion;
        } else if (TextUtils.equals(business, CCLogConfig.BUSINESS_CLASS)) {
            classSdkVersion = businessSdkVersion;
        }
    }

    public String getBusiness() {
        return business;
    }

    public String getLiveSdkVersion() {
        return liveSdkVersion;
    }

    public String getVodSdkVersion() {
        return vodSdkVersion;
    }

    public String getClassSdkVersion() {
        return classSdkVersion;
    }

    public HashMap<Object, Object> getBusinessParams() {
        return businessParams;
    }

    public void setBusinessParams(HashMap<Object, Object> businessParams) {
        if (this.businessParams == null) {
            this.businessParams = businessParams;
        } else {
            if (businessParams != null && !businessParams.isEmpty()) {
                this.businessParams.putAll(businessParams);
            }
        }
    }

    public String getBusinessSdkVersion() {
        return businessSdkVersion;
    }
}
