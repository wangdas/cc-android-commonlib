package com.bokecc.common.application;

import android.annotation.SuppressLint;
import android.content.Context;

import com.bokecc.common.crash.CCCrashManager;
import com.bokecc.common.exception.CrashException;
import com.bokecc.xlog.CCLog;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Application
 * 保存一个全局的Context
 * 启动CrashException
 *
 * @author wangyue
 */
public class ApplicationData {

    /***/
    public static CopyOnWriteArrayList<String> remindStrings = new CopyOnWriteArrayList<>();

    /**判断网络连接*/
    public static boolean updataState = false;

    /**全局上下文*/
    public static Context globalContext;

    /**
     * 不可new
     */
    private ApplicationData() {}

    /**
     * 获取单例
     *
     * @return ApplicationData
     */
    public static ApplicationData getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        public static ApplicationData instance = new ApplicationData();
    }

    /**
     * 初始化
     * 使用了context和初始化异常捕获等
     *
     * @param globalContext Context globalContext
     */
    @Deprecated
    public void init(Context globalContext) {
        initSdk(globalContext, false);
    }

    /**
     * 初始化
     *
     * @param globalContext    application对象
     * @param isDebug          是否是测试模式，测试模式打开控制台日志输出并且不上报崩溃日志
     */
    public void init(Context globalContext, boolean isDebug) {
        initSdk(globalContext, isDebug);
    }

    /**
     * 初始化
     *
     * @param globalContext    application对象
     * @param isDebug 是否是测试模式，测试模式打开控制台日志输出并且不上报崩溃日志
     * @param isXLog           是否使用xlog，配合gradle->exclude使用
     */
    @Deprecated
    public void init(Context globalContext, boolean isDebug, boolean isXLog) {
        initSdk(globalContext, isDebug);
    }

    /**
     * 初始化
     *
     * @param globalContext    application对象
     * @param isDebug 是否打开控制台日志输出
     */
    private void initSdk(Context globalContext, boolean isDebug) {
        ApplicationData.globalContext = globalContext;
        IS_DEBUG = isDebug;

        //初始化离线日志
        CCLog.initXLog(isDebug);

        //初始化崩溃日志
        if (!isDebug) {
            synchronized (this) {
                CrashException.getInstance();
                //上传收集到的crash信息
                CCCrashManager.getInstance().reportCrash();
                // 添加打开app的事件上报
                CCCrashManager.getInstance().reportLaunchApp();
            }
        }
    }

    /**
     * 退出调用
     */
    public void onTerminate() {
        CCLog.onTerminate();
    }



    /**当前是否为BuildConfig.Debug状态，不应该存在这个变量，*/
    @Deprecated
    private static boolean IS_DEBUG =false;

    /**
     * 当前BuildConfig是否为debug状态
     *
     * @param isDebug isDebug
     */
    @Deprecated
    public static void isDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    /**
     *
     * @return
     */
    @Deprecated
    public static boolean getDebugStatue(){
        return IS_DEBUG;
    }

//    /**日志路径*/
//    @Deprecated
//    public static String logPath = CCLogConfig.logPath;
//    /**日志名*/
//    @Deprecated
//    public static String fileName = CCLogConfig.fileName;

    /**
     * 设置日志路径
     *
     * @param logPath 日志路径
     * @param logName 日志名
     */
    @Deprecated
    public void setLogConfig(String logPath, String logName) {
//        ApplicationData.logPath = logPath;
//        ApplicationData.fileName = logName;
    }

}
