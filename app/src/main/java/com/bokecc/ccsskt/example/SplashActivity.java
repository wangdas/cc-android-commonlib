package com.bokecc.ccsskt.example;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.bokecc.common.crash.CCCrashManager;
import com.bokecc.common.utils.Tools;

import java.util.HashMap;
import java.util.List;

/**
 * 闪屏界面
 *
 * @author wy
 */
@SuppressLint("NonConstantResourceId")
public class SplashActivity extends Activity implements View.OnClickListener {

    private List list;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
        }
        findViewById(R.id.nullPointer).setOnClickListener(this);
        findViewById(R.id.arrayIndexOutOfBounds).setOnClickListener(this);
//        Glide.with(this).load("www.baidu.com");
    }

   Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           Tools.loge("test", String.valueOf(list.size()));
       }
   };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nullPointer:
                CCCrashManager.getInstance().setBaseInfo("1001","test");
                HashMap<Object,Object> hashMap=new HashMap<>(16);
                hashMap.put("business","3001");
                hashMap.put("appid","appId");
                CCCrashManager.getInstance().setBusinessParams(hashMap);
                CCCrashManager.getInstance().reportExecute();
                int a =1/0;
                Message message= Message.obtain();
                message.what=1;
                message.obj=String.valueOf(list.size());
                handler.sendMessage(message);
                break;
            case R.id.arrayIndexOutOfBounds:
                Tools.loge("test","null");
                break;
            default:
                break;
        }
    }
}
