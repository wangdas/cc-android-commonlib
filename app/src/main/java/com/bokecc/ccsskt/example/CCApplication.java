package com.bokecc.ccsskt.example;

import android.support.multidex.MultiDexApplication;

import com.bokecc.common.application.ApplicationData;
import com.bokecc.common.crash.CCCrashManager;

/**
 * Application
 */
public class CCApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //公共库初始化
        CCCrashManager.getInstance().setBaseInfo("1001","test");
        ApplicationData.getInstance().init(this,true);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        ApplicationData.getInstance().onTerminate();
    }

}
