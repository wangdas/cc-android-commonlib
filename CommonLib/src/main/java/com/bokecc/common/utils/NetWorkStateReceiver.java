package com.bokecc.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.bokecc.common.application.ApplicationData;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 网络监听广播
 * 取消注册需要清除监听器 call {@link #clearListener()}
 * Created by Carson_Ho on 16/10/31.
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    private CopyOnWriteArrayList<Timer> timers = new CopyOnWriteArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        Tools.log("网络状态发生变化");
        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
            }
        } else {

            String msg = "";
            final Timer timer = new Timer();
            timers.add(timer);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (onTimerConnectoListener != null) {
                        onTimerConnectoListener.onTimerDisConnected();
                    }
                    cancelTask(timer);//连网后也取消定时
                }
            };
//            if (TextUtils.isEmpty(sb.toString())) {
            if (!NetUtils.isNetworkAvailable(context)) {
                if (!NetUtils.isMobileConnected(context) && !NetUtils.isWifiConnected(context)) {
                    if (onConnectoListener != null) {
                        onConnectoListener.onDisConnected();
                    }
                    msg = "网络已经断开,请检查网络";
                    ApplicationData.remindStrings.add(msg);
                    ApplicationData.updataState = true;
                    timer.schedule(timerTask, 8000);//断网一定时间后执行
                }
            } else {
                if (NetUtils.isMobileConnected(context) || NetUtils.isWifiConnected(context)) {
                    if (onConnectoListener != null) {
                        onConnectoListener.onConnected();
                    }
                    msg = "网络已链接";
                    ApplicationData.remindStrings.add(msg);
                    for (Timer timerTemp : timers) {
                        cancelTask(timerTemp);//连网后也取消定时
                    }
                }
            }
            if (ApplicationData.remindStrings.size() == 2) {//少于两次不需两次比对一下
                if (ApplicationData.remindStrings.get(0).equals(ApplicationData.remindStrings.get(1))) {
                    ApplicationData.updataState = false;
                } else {
                    ApplicationData.updataState = true;
                }
            }
            if (ApplicationData.remindStrings.size() > 2) {//大于两次比较后两次的
                ApplicationData.remindStrings.remove(0);
                if (ApplicationData.remindStrings.get(0).equals(ApplicationData.remindStrings.get(1))) {
                    ApplicationData.updataState = false;
                } else {
                    ApplicationData.updataState = true;
                }
            }
            if (ApplicationData.updataState) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                if (msg.equals("网络已链接")) {
                    ApplicationData.updataState = false;
                }
            }
        }
    }


    //网络连接回调
    private OnConnectoListener onConnectoListener;

    public void setOnConnectoListener(OnConnectoListener onConnectoListener) {
        this.onConnectoListener = onConnectoListener;
    }

    public interface OnConnectoListener {

        void onConnected();

        void onDisConnected();
    }

    //网络连接定时回调
    private OnTimerConnectoListener onTimerConnectoListener;

    public void setOnTimerConnectoListener(OnTimerConnectoListener onTimerConnectoListener) {
        this.onTimerConnectoListener = onTimerConnectoListener;
    }

    public interface OnTimerConnectoListener {
        void onTimerDisConnected();
    }

    //取消定时器
    private void cancelTask(Timer timer) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timers.remove(timer);
        }
    }

    private void cancelTimerTask(TimerTask timerTask) {
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    /**
     * 取消注册需要清除监听
     */
    public void clearListener(){
        onTimerConnectoListener = null;
        for (Timer timerTemp : timers) {
            cancelTask(timerTemp);
        }
    }
}
