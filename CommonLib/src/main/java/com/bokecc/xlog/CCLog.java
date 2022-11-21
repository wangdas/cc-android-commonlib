package com.bokecc.xlog;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.bokecc.common.application.ApplicationData;
import com.bokecc.common.log.CCLogConfig;
import com.bokecc.common.utils.Tools;

import java.io.File;

/**
 * 日志打印
 * 1.日志打印
 * 2.离线日志生成方式：两种：
 * （1）xlog；（主选）
 * （2）直播现有日志打印系统；(已删除)
 * 3. v/d级别只打印到控制台，i/w/e会写入离线日志
 * 4.离线日志默认自动上传；
 */
public class CCLog {

    /**控制台打印开关*/
    private static boolean mConsoleLogOpen = false;
    /**文件路径*/
    private static String path;

    public static void initXLog(boolean isConsoleLogOpen){
        try{
            mConsoleLogOpen = isConsoleLogOpen;
            path = Tools.getSdcardRootPath(ApplicationData.globalContext) + CCLogConfig.logPath;

            System.loadLibrary("c++_shared");
            System.loadLibrary("ccxlog");

            //init xlog
            Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, "", path, CCLogConfig.fileName, 0, "");
            //是否会把日志打印到 logcat 中， 默认不打印，true打印，false不打印
            Xlog.setConsoleLogOpen(isConsoleLogOpen);
            //
            com.bokecc.xlog.Log.setLogImp(new Xlog());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 打印一些最为繁琐、意义不大的日志信息
     * 只打印不写入本地日志
     * @param tag
     * @param message
     */
    public static void v(String tag,String message) {
        if(mConsoleLogOpen) {
            Log.v(tag, message);
        }
    }

    /**
     * 打印一些调试信息
     * 只打印不写入本地日志
     * @param tag
     * @param message
     */
    public static void d(String tag,String message) {
        try{
            if(mkdirs()){
                com.bokecc.xlog.Log.d(generateTag(tag), message);
            }else{
                if(mConsoleLogOpen) {
                    Log.i(tag, message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 打印一些比较重要的数据，可帮助你分析用户行为数据
     * 打印并写入日志文件
     * @param tag
     * @param message
     */
    public static void i(String tag,String message) {
        try{
            if(mkdirs()){
                com.bokecc.xlog.Log.i(generateTag(tag), message);
            }else{
                if(mConsoleLogOpen) {
                    Log.i(tag, message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 打印一些警告信息，提示程序该处可能存在的风险
     * 打印程序中的错误信息
     * @param tag
     * @param message
     */
    public static void w(String tag,String message) {
        try{
            if(mkdirs()){
                com.bokecc.xlog.Log.w(generateTag(tag), message);
            }else{
                if(mConsoleLogOpen) {
                    Log.w(tag, message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 打印错误信息
     * @param tag
     * @param message
     */
    public static void e(String tag,String message) {
        try{
            if(mkdirs()){
                com.bokecc.xlog.Log.e(generateTag(tag), message);
            }else{
                if(mConsoleLogOpen) {
                    Log.e(tag, message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 结束调用
     */
    public static void onTerminate(){
        try{
            com.bokecc.xlog.Log.appenderClose();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 刷新数据
     */
    public static void flush(){
        try{
            com.bokecc.xlog.Log.appenderFlush(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     */
    private static boolean mkdirs(){
        File logFile = new File(path);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        return logFile.exists();
    }

    /**
     * 获取类名
     * @param customTagPrefix
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static String generateTag(String customTagPrefix) {
        try{
            StackTraceElement caller = new Throwable().getStackTrace()[3];
            String tag = "%s.%s(L:%d)";
            String callerClazzName = caller.getClassName();
            callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
            tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
            tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
            return tag;
        }catch (Exception e){
            e.printStackTrace();
        }
        return customTagPrefix;
    }

}
