package com.bokecc.common.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.bokecc.common.application.ApplicationData;
import com.bokecc.xlog.CCLog;
import com.bokecc.common.http.bean.NetworkInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 全局共用的常用方法类
 *
 * @author wangyue
 */
public class Tools {

    /**
     * 按级别打印日志
     *
     * @param level   0 verbose
     *                1 debug
     *                2 info
     *                3 warn
     *                4 error
     *                5 assert
     * @param message
     */
    public static void log(int level, String message) {
        switch (level) {
            case Log.VERBOSE:
                CCLog.v("colossus", message);
                break;
            case Log.DEBUG:
                CCLog.d("colossus", message);
                break;
            case Log.INFO:
                CCLog.i("colossus", message);
                break;
            case Log.WARN:
                CCLog.w("colossus", message);
                break;
            case Log.ERROR:
                CCLog.e("colossus", message);
                break;
            default:
                CCLog.e("colossus", message);
                break;
        }
    }

    /**
     * 定义一个全局的Log日志输出
     */
    public static void log(String message) {
        CCLog.i("colossus", message);
    }

    /**
     * 定义一个全局的Log日志输出
     */
    public static void log(String tag, String message) {
        CCLog.i(tag, message);
    }

    /**
     * 定义一个全局的Log日志输出
     */
    public static void logd(String tag, String message) {
        CCLog.d(tag, message);
    }

    /**
     * 定义一个全局的Log日志输出
     */
    public static void logi(String tag, String message) {
        CCLog.i(tag, message);
    }

    /**
     * 定义一个全局的Log日志输出
     */
    public static void logw(String tag, String message) {
        CCLog.w(tag, message);
    }

    /**
     * 定义一个全局的Log日志输出
     */
    public static void loge(String tag, String message) {
        CCLog.e(tag, message);
    }

    /**
     * 获取字符串
     *
     * @return
     */
    public static String getString(int id) {
        return ApplicationData.globalContext.getString(id);
    }

    /**
     * 获取软件包名
     *
     * @return
     */
    public static String getPackageName() {
        if (ApplicationData.globalContext != null) {
            return ApplicationData.globalContext.getPackageName();
        } else {
            return "com.cokecc.common";
        }
    }


    public static String getPackageNameByClassName(String className) {
        String packageName = "";
        try {
            Class<?> glass = Class.forName(className);
            if (glass.getPackage() != null) {
                packageName = glass.getPackage().getName();
            }
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        return packageName;
    }


    public static String getAppPackageName(){
      return ApplicationData.globalContext.getPackageName();
    }

    public static String getAppVersionName(){
        String appPackageName = getAppPackageName();
        PackageManager packageManager = ApplicationData.globalContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(appPackageName, 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取versionCode（ANDROID版本号）
     *
     * @return
     */
    public static int getVersionCode() {
        int versioncode = 0;
        try {
            PackageInfo pinfo = ApplicationData.globalContext.getPackageManager().getPackageInfo(getPackageName(), 0);
            versioncode = pinfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioncode;
    }

    /**
     * 获取versionName
     *
     * @return versionName
     */
    public static String getVersionName() {
        String versionName = "";
        try {
            PackageInfo pinfo = ApplicationData.globalContext.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取网络信息
     *
     * @return NetworkInfo
     */
    public static NetworkInfo getNetworkInfo() {
        NetworkInfo myNetworkInfo = new NetworkInfo();
        try {
            ConnectivityManager manager = (ConnectivityManager) ApplicationData.globalContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            android.net.NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                myNetworkInfo.setConnectToNetwork(true);
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    myNetworkInfo.setMobileNetwork(false);
                    myNetworkInfo.setProxyName(networkInfo.getTypeName());
                } else {
                    myNetworkInfo.setMobileNetwork(true);
                    myNetworkInfo.setProxyName(networkInfo.getExtraInfo());
                }
            } else {
                myNetworkInfo.setConnectToNetwork(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myNetworkInfo;
    }

    private static String ip = null;

    /**
     * 获取IP
     *
     * @return LocalIpAddress
     */
    public static String getLocalIpAddress() {
        try {
            if (ip == null) {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                            ip = inetAddress.getHostAddress().toString();
                            if (ip.startsWith("10.")) {
                                return "";
                            } else if (ip.startsWith("192.168.")) {
                                return "";
                            } else if (ip.startsWith("176") && (Integer.valueOf(ip.split(".")[1]) >= 16)
                                    && (Integer.valueOf(ip.split(".")[1]) <= 31)) {
                                return "";
                            } else {
                                return ip;
                            }

                        }
                    }
                }
            } else {
                return ip;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取android_id
     * 2022.8.30 androidID修改为只获取一次，之后从配置文件中获取
     * @return android_id
     */
    public static String getAndroidID() {
        try {
            if(ApplicationData.globalContext == null){
                return "";
            }
            String AID = SharedPreferencesUtils.getString(ApplicationData.globalContext,"AID","");
            if(AID == null || AID.length() == 0){
                AID = Settings.Secure.getString(ApplicationData.globalContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                SharedPreferencesUtils.saveString(ApplicationData.globalContext,"AID",AID);
            }
            if (null != AID) {
                return AID;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取cpu 架构
     *
     * @return cpu 架构
     */
    public static String getCpuAbi() {
        String cpuAbi = "armeabi";
        try {
            cpuAbi = SharedPreferencesUtils.getString(ApplicationData.globalContext,"CPU","");
            if(cpuAbi == null || cpuAbi.length() == 0){
                String osCpuAbi = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine();
                if (osCpuAbi.contains("x86")) {
                    cpuAbi = "x86";
                } else if (osCpuAbi.contains("armeabi-v7a")) {
                    cpuAbi = "armeabi-v7a";
                } else if(osCpuAbi.contains("arm64-v8a")){
                    cpuAbi = "arm64-v8a";
                } else {
                    cpuAbi = "armeabi";
                }
                SharedPreferencesUtils.saveString(ApplicationData.globalContext,"CPU",cpuAbi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cpuAbi;
    }


    /**
     * 获得当前日期和时间 格式 yyyy-MM-dd HH:mm
     */
    public static String getCurrentDateTimeNoSS() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 获得当前日期和时间 格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 获得当前日期和时间 格式yyyy-MM-dd HH:mm:ss:SS
     */
    public static String getCurrentDateTimeWithSS() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 获得当前时间
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 获得当前时间
     */
    public static String getCurrentTimeMM() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("mm", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 返回当前时间，单位毫秒
     *
     * @return CurrentTimeMillis
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取时间 string 2 long
     *
     * @param date time
     * @return long
     */
    public static long getDateTimeStringToLong(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt2 = sdf.parse(date);
            //继续转换得到秒数的long型
            return dt2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取时间 string 2 long
     *
     * @param date time String
     * @return long
     */
    public static long getDateStringToLong(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt2 = sdf.parse(date);
            //继续转换得到秒数的long型
            return dt2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得当前日期
     */
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String current_time = sdf.format(date);
        return current_time;
    }

    /**
     * 获取指定时间的字符
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String current_time = sdf.format(date);
        return current_time;
    }

    /**
     * 获取指定时间的天
     *
     * @param time
     * @return
     */
    public static String getDateDay(long time) {
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        String day = sdf.format(date);
        return day;
    }

    /**
     * 对时间进行处理
     *
     * @param time 毫秒
     * @return
     */
    public static String getTimeLongToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date dt = new Date(time);
        String sDateTime = sdf.format(dt); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    /**
     * 对时间进行处理
     *
     * @param time
     * @return
     */
    public static String timeLongToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date dt = new Date(time * 1000L);
        String sDateTime = sdf.format(dt); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    /**
     * 获取格式化时间：mm:ss
     *
     * @param timestamp
     * @return
     */
    public static String getTimeStrmmss(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    /**
     * 获取指定时间的字符
     *
     * @param time
     * @param type
     * @return
     */
    public static String getDate(long time, String type) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.getDefault());
        String current_time = sdf.format(date);
        return current_time;
    }

    /**
     * 时间补零
     *
     * @param c
     * @return
     */
    public static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
    }

    /**
     * 获得天数
     */
    public static int getDayNum(long millisTime) {
        int day = (int) (millisTime / (1000 * 60 * 60 * 24));
        if (millisTime % (1000 * 60 * 60 * 24) != 0) {
            return day + 1;
        }
        return day;
    }

    /**
     * 获取倒计时间
     * 格式:00:00
     *
     * @param difftime 秒
     * @return
     */
    public static String getCountdownTime(int difftime) {
        return getHourFillGap(difftime / 3600) + getTimeFillGap((difftime % 3600) / 60) + ":" + getTimeFillGap((difftime % 3600) % 60);
    }

    /**
     * 时间(分/秒)补位
     *
     * @param difftime
     * @return
     */
    private static String getTimeFillGap(int difftime) {
        String re = "00";
        if (difftime < 10) {
            re = "0" + difftime;
        } else if (difftime < 60) {
            re = "" + difftime;
        }
        return re;
    }

    /**
     * 时间(小时)补位
     *
     * @param difftime
     * @return
     */
    private static String getHourFillGap(int difftime) {
        String re = "";
        if (difftime > 0) {
            re = difftime + ":";
        }
        return re;
    }

    /**
     * 返回两次的时间差的显示方式
     *
     * @param startTime
     * @param nowTime
     * @return
     */
    public static String showRuleTime(long startTime, long nowTime) {
        String re = "";
        long difftime = nowTime - startTime;
        if (difftime < 0) {
            re = "0秒前";
        } else if (difftime < 60 * 1000) {
            // 小于60s
            re = difftime / 1000 + "秒前";
        } else if (difftime < 60 * 60 * 1000) {
            // 小于60min
            re = (difftime / 1000) / 60 + "分钟前";
        } else {
            Date date_start = new Date(startTime);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String nowDay = formatter.format(new Date(nowTime));
            String yesterDay = formatter.format(new Date(nowTime - 24 * 60 * 60 * 1000));
            String startDay = formatter.format(date_start);
            if (startDay.equals(nowDay)) {
                SimpleDateFormat myFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
                re = "今天  " + myFormatter.format(date_start);
            } else if (startDay.equals(yesterDay)) {
                SimpleDateFormat myFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
                re = "昨天  " + myFormatter.format(date_start);
            } else {
                SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                re = myFormatter.format(date_start);
            }
        }
        return re;
    }

    /**
     * 判断两个更新时间差
     *
     * @param beforeTime  上一次的时间
     * @param nowTime     本次的时间
     * @param defaultDiff 需要的差距
     * @return
     */
    public static boolean dateDiff(String beforeTime, String nowTime, long defaultDiff) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date_before = formatter.parse(beforeTime);
            Date date_after = formatter.parse(nowTime);
            long now_time = date_after.getTime();
            long before_time = date_before.getTime();
            long diff = now_time - before_time;
            if (diff - defaultDiff > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 秒转分钟或小时字符串
     *
     * @param second        总秒数
     * @param isKeepSeconds 是否保留秒
     * @return
     */
    public static String secondToMin(long second, boolean isKeepSeconds) {
        String timeStr = 0 + "分";
        try {
            if (second > 0) {
                int minute = (int) (second / 60);
                int seconds = (int) (second % 60);
                if (minute > 0) {
                    if (seconds > 0) {
                        if (isKeepSeconds) {
                            if (seconds < 10) {
                                timeStr = minute + "分0" + seconds + "秒";
                            } else {
                                timeStr = minute + "分" + seconds + "秒";
                            }
                        } else {
                            if (seconds >= 30) {// 超过30秒+1分钟
                                timeStr = (minute + 1) + "分";
                            } else {// 不足30秒忽略掉
                                timeStr = minute + "分";
                            }
                        }
                    } else {
                        timeStr = minute + "分" + "00秒";
                    }
                } else {
                    timeStr = seconds + "秒";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    /**
     * 限制特殊字符 密码输入等处需要做判断
     */
    public static boolean limitSpecialCharacters(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ 　]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return !m.replaceAll("").equalsIgnoreCase(str);
    }

    /**
     * 获取当前根路径
     *
     * @return
     */
    public static String getRootPath() {
        String rootPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Environment.getExternalStorageDirectory().canWrite()) {
            rootPath = getSdcardRootPath(ApplicationData.globalContext);
        } else {
            rootPath = getDataRootPath();
        }
        return rootPath;
    }

    /**
     * 获取外部存储的根路径
     *
     * @return
     */
    @Deprecated
    public static String getSdcardRootPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取外部存储的根路径
     *
     * @param context
     * @return
     */
    public static String getSdcardRootPath(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            File file = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (file != null) {
                return file.getPath();
            }
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 手机内部存储根路径
     *
     * @return
     */
    public static String getDataRootPath() {
        return Environment.getDataDirectory() + "/data/" + Tools.getPackageName() + "/files";
    }

    /**
     * dip转成pixel
     *
     * @param dip
     * @return
     */
    public static int dipToPixel(float dip) {
        return (int) (dip * ApplicationData.globalContext.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * sp转成pixel
     *
     * @param spValue
     * @return
     */
    public static float spToPixel(float spValue) {
        final float fontScale = ApplicationData.globalContext.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }

    /**
     * 读取图片资源
     *
     * @param resId 资源id
     * @return
     */
    public static Bitmap readBitmap(int resId) {
        InputStream stream = null;
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Config.ARGB_8888;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            stream = ApplicationData.globalContext.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(stream, null, opt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 图片的不等比缩放
     *
     * @param src        源图片
     * @param destWidth  缩放的宽度
     * @param destHeigth 缩放的高度
     * @return
     */
    public static Bitmap bitmapScale(Bitmap src, int destWidth, int destHeigth) {
        try {
            if (src == null) {
                return null;
            }
            int w = src.getWidth();// 源文件的大小
            int h = src.getHeight();
            float scaleWidth = ((float) destWidth) / w;// 宽度缩小比例
            float scaleHeight = ((float) destHeigth) / h;// 高度缩小比例
            Matrix m = new Matrix();// 矩阵
            m.postScale(scaleWidth, scaleHeight);// 设置矩阵比例
            Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, m, true);// 直接按照矩阵的比例把源文件画入进行
            return resizedBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 等比缩放图片
     *
     * @param src
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap bitmapIsometricScale(Bitmap src, int targetWidth, int targetHeight) {
        if (src != null) {
            int width = src.getWidth();
            int height = src.getHeight();
            if (width * targetHeight > targetWidth * height) {
                targetHeight = targetWidth * height / width;
            } else if (width * targetHeight < targetWidth * height) {
                targetWidth = targetHeight * width / height;
            }
            float scaleWidth = ((float) targetWidth) / width;// 宽度缩小比例
            float scaleHeight = ((float) targetHeight) / height;// 高度缩小比例
            Matrix m = new Matrix();// 矩阵
            m.postScale(scaleWidth, scaleHeight);// 设置矩阵比例
            Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, width, height, m, true);// 直接按照矩阵的比例把源文件画入进行
            return resizedBitmap;
        }
        return null;
    }

    /**
     * 等比缩放图片
     *
     * @param src
     * @param scale
     * @return
     */
    public static Bitmap bitmapIsometricScale(Bitmap src, float scale) {
        if (src != null) {
            int width = src.getWidth();
            int height = src.getHeight();
            Matrix m = new Matrix();// 矩阵
            m.postScale(scale, scale);// 设置矩阵比例
            Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, width, height, m, true);// 直接按照矩阵的比例把源文件画入进行
            return resizedBitmap;
        }
        return null;
    }

    /**
     * 获取图片
     *
     * @param imageId 数据库中的图片_id
     * @return
     */
    public static Bitmap getBitmapFormId(int imageId) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(ApplicationData.globalContext.getResources(), imageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 从指定路径读取图片（原图读取 不会改变大小）
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getBitmapFormPath(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return null;
        }
        try {
            Bitmap bitmap = null;
            File file = new File(imagePath);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(imagePath);
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从sdcard或data文件夹读取图片(会在原图基础上进行压缩)
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getBitmapFormSdcardOrData(String imagePath) {
        if (null == imagePath) {
            return null;
        }
        InputStream stream = null;
        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                return null;
            }
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(imagePath), null, o);

            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imagePath), null, o2);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从sdcard或data文件夹读取图片
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getBitmapFormSdcardOrData(String imagePath, int height, int width) {
        if (null == imagePath) {
            return null;
        }
        InputStream stream = null;
        try {
            File file = new File(imagePath);
            if (file.exists() || file.canRead() || file.isFile()) {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(imagePath), null, o);
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1;
                while (width_tmp / scale > width && height_tmp / scale > height) {
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale++;
                }
                o.inJustDecodeBounds = false;
                o.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imagePath), null, o);
                return bitmap;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 图片圆角处理
     *
     * @param bitmap  源图片
     * @param roundPx 圆角角度
     * @return
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        return getRoundBitmap(bitmap, roundPx, 1.0f, 1.0f);
    }

    /**
     * 图片圆角处理
     *
     * @param bitmap      源图片
     * @param roundPx     圆角角度
     * @param widthScale  处理后的图片宽与原图宽比例 不需要改变宽度直接传1.0f
     * @param heightScale 处理后的图片高度与原图高度比例 不需要改变高度直接传1.0f
     * @return
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap, int roundPx, float widthScale, float heightScale) {
        Bitmap roundBitmap = null;
        try {
            roundBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(roundBitmap);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            int width = (int) (bitmap.getWidth() * widthScale);
            int height = (int) (bitmap.getHeight() * heightScale);
            final Rect rect = new Rect(0, 0, width, height);
            final RectF rectF = new RectF(rect);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (Exception e) {
            e.printStackTrace();
            roundBitmap = bitmap;
        }
        return roundBitmap;
    }

    /**
     * 从assets文件夹读取图片
     *
     * @param imagePath
     * @return
     */
    public static Bitmap createBitmapFormAssets(String imagePath) {
        InputStream stream = null;
        try {
            if (imagePath != null) {
                stream = ApplicationData.globalContext.getAssets().open(imagePath);
            }
            if (stream != null) {
                return Bitmap.createBitmap(BitmapFactory.decodeStream(stream));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取详细地址
     *
     * @param location
     * @return
     */
    public static String getAddress(Location location) {
        try {
            if (location != null) {
                Geocoder geo = new Geocoder(ApplicationData.globalContext, Locale.getDefault());
                List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (!addresses.isEmpty()) {
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        String addressName = address.getAddressLine(0);
                        if (addressName == null || addressName.length() <= 3) {
                            addressName = address.getLocality();
                        }
                        return addressName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "定位失败";
    }

    /**
     * 获取城市名
     *
     * @param location
     * @return
     */
    public static String getCurrentCity(Location location) {
        try {
            if (location != null) {
                Geocoder geo = new Geocoder(ApplicationData.globalContext, Locale.getDefault());
                List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (!addresses.isEmpty()) {
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        String addressName = address.getLocality();
                        return addressName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "北京";
    }

    /**
     * 获得手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        try {
            String phoneVersion = Build.MODEL;
            if (null != phoneVersion) {
                return phoneVersion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 调用系统短信
     *
     * @param activity Activity自身
     * @param body     信息内容
     */
    public static void sendSms(Activity activity, String phoneNumber, String body) {
        try {
            Uri smsToUri = Uri.parse("smsto:" + phoneNumber);// 联系人地址
            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            intent.putExtra("address", phoneNumber);
            intent.putExtra("sms_body", body);// 短信的内容
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统电话
     *
     * @param activity
     * @param phoneNum
     */
    public static void openSystemPhone(Activity activity, String phoneNum) {
        try {
            Intent it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
            activity.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统浏览器
     *
     * @param activity
     * @param url
     */
    public static void openSystemBrowser(Activity activity, String url) {
        try {
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统网络设置
     *
     * @param activity
     */
    public static void openSystemNetworkSetting(Activity activity) {
        try {
            Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统地图
     *
     * @param activity
     */
    public static void openSystemMap(Activity activity, String latitude, String longitude) {
        try {
            Uri mUri = Uri.parse("geo:" + latitude + "," + longitude);
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            activity.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm;
        dm = ApplicationData.globalContext.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resource = context.getResources();
        int rid = resource.getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            int resourceId = resource.getIdentifier("navigation_bar_height", "dimen", "android");
            return resource.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 获得屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight() {
        // 适配 miui
        try {
            // TODO: 2018/10/10 测试一下非MIUI手机
            // 如果是手势模式则获取屏幕实际高度，虚拟按键模式时获取的是刨去虚拟按键的高度
            boolean miuiGesture = Settings.Global.getInt(ApplicationData.globalContext.getContentResolver(), "force_fsg_nav_bar", 0) != 0;
            if (miuiGesture) {
                DisplayMetrics dm = new DisplayMetrics();
                ((WindowManager) ApplicationData.globalContext.getSystemService(Context.WINDOW_SERVICE)).
                        getDefaultDisplay().getRealMetrics(dm);
                return dm.heightPixels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DisplayMetrics dm;
        dm = ApplicationData.globalContext.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取使用内存大小
     *
     * @return
     */
    public static int getMemory() {
        int pss = 0;
        ActivityManager myAM = (ActivityManager) ApplicationData.globalContext.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = ApplicationData.globalContext.getPackageName();
        List<RunningAppProcessInfo> appProcesses = myAM.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                int pids[] = {appProcess.pid};
                Debug.MemoryInfo self_mi[] = myAM.getProcessMemoryInfo(pids);
                pss = self_mi[0].getTotalPss();
            }
        }
        return pss;
    }

    /**
     * 获得CPU使用率
     *
     * @return
     */
    public static int getCpuInfo() {
        int cpu = 0;
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String[] toks = load.split(" ");
            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            reader.seek(0);
            load = reader.readLine();
            reader.close();
            toks = load.split(" ");
            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
            cpu = (int) (100 * (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cpu;
    }


    /**
     * 全屏切换
     *
     * @param activity
     * @param isNotFullScreen true非全屏 false全屏
     */
    public static void setFullScreen(Activity activity, boolean isNotFullScreen) {
        try {
            if (isNotFullScreen) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            } else {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            activity.getMenuInflater();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示隐藏状态栏，全屏不变，只在有全屏时有效
     *
     * @param enable
     */
    public static void setStatusBarVisibility(Activity activity, boolean enable) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (enable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        activity.getWindow().setAttributes(lp);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    /**
     * 得到系统亮度
     *
     * @return
     */
    public static int getSystemBrightness() {
        int brightness = 0;
        try {
            brightness = Settings.System.getInt(ApplicationData.globalContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
            brightness = brightness * 100 / 255;
        } catch (SettingNotFoundException ex) {
            ex.printStackTrace();
        }
        return brightness >= 5 ? brightness : 5;
    }

    /**
     * 调节屏幕亮度
     *
     * @param value
     */
    public static void setBackLight(Activity activity, int value) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.screenBrightness = (float) (value * (0.01));
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 屏幕亮度跟随系统
     */
    public static void setBackLightNormalPolicy(Activity activity) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调节屏幕亮度，渐变的。
     *
     * @param value
     */
    public static void setBackLightGradual(final Activity activity, final int value) {
        try {
            final WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(lp.screenBrightness, (float) (value * (0.01)));
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setDuration(1600);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    lp.screenBrightness = (float) animation.getAnimatedValue();
                    activity.getWindow().setAttributes(lp);
                }
            });
            valueAnimator.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取sdcard或data的剩余空间
     */
    public static long getSdcardFreeSize(String rootPath) {
        // 取得sdcard文件路径
        StatFs statFs = new StatFs(rootPath);
        // 获取block的SIZE
        long blocSize = statFs.getBlockSize();
        // 可使用的Block的数量
        long availaBlock = statFs.getAvailableBlocks();
        // 剩余空间大小
        long freeSize = availaBlock * blocSize;
        return freeSize;
    }

    /**
     * 获得系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        try {
            String release = Build.VERSION.RELEASE;
            if (null != release) {
                return release;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 小提示
     *
     * @param content 提示内容
     */
    public static void showToast(String content) {
        showToast(content, false);
    }

    /**
     * 小提示
     *
     * @param content        提示内容
     * @param isLongTimeShow 是否长时间提醒
     */
    public static void showToast(int content, boolean isLongTimeShow) {
        showToast(getString(content), isLongTimeShow);
    }

    /**
     * 小提示
     *
     * @param content        提示内容
     * @param isLongTimeShow 是否长时间提醒
     */
    public static void showToast(String content, boolean isLongTimeShow) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        Context context = ApplicationData.globalContext;
        if (content != null && context != null) {
            int timer = Toast.LENGTH_SHORT;
            if (isLongTimeShow) {
                timer = Toast.LENGTH_LONG;
            } else {
                timer = Toast.LENGTH_SHORT;
            }

            Toast mToast = Toast.makeText(context, null, timer);
            mToast.setText(content);
            mToast.show();
        }
    }

    /**
     * 判断当前是否符合桌面显示的对话框
     *
     * @param context
     * @return
     */
    public static boolean pushDeskFlag(Context context) {
        boolean deskFlag = false;
        String taskNameTop = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(100);
        if (tasksInfo.size() > 0) {
            taskNameTop = tasksInfo.get(0).topActivity.getPackageName();
        } else {
            return true;
        }
        for (int i = 0; i < tasksInfo.size(); i++) {
            if (context.getPackageName().equals(tasksInfo.get(i).topActivity.getPackageName())) {
                return false;
            }
        }
        List<String> names = getAllTheLauncher(context);
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(taskNameTop)) {
                deskFlag = true;
            }
        }
        return deskFlag;
    }

    /**
     * 获取所有的launcher信息
     *
     * @param context
     * @return
     */
    private static List<String> getAllTheLauncher(Context context) {
        List<String> names = null;
        PackageManager pkgMgt = context.getPackageManager();
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> ra = pkgMgt.queryIntentActivities(it, 0);
        if (ra.size() != 0) {
            names = new ArrayList<String>();
        }
        for (int i = 0; i < ra.size(); i++) {
            String packageName = ra.get(i).activityInfo.packageName;
            names.add(packageName);
        }
        return names;
    }

    /**
     * 判断手机是否有发送短信权限
     *
     * @param context
     * @return
     */
    public static boolean isUseSendSMSPermission(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_PERMISSIONS);
            String[] permissions = pInfo.requestedPermissions;
            for (String s : permissions) {
                if (!s.trim().equals(android.Manifest.permission.SEND_SMS)) {
                    continue;
                }
                return true;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断字符串是否为数字(不能判断float类型)
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (str.matches("\\d*")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否为数字(包含float类型)
     *
     * @param str
     */
    public static boolean isNumeric(String str) {
        if (str.matches("\\d+(.\\d+)?")) {
            return true;
        } else {// 不是数字
            return false;
        }
    }

    /**
     * 设置Selector
     */
    public static StateListDrawable newSelector(Context context, int[] state) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = state[0] == -1 ? null : context.getResources().getDrawable(state[0]);
        Drawable pressed = state[1] == -1 ? null : context.getResources().getDrawable(state[1]);
        Drawable focused = state[1] == -1 ? null : context.getResources().getDrawable(state[1]);
        Drawable unable = state[0] == -1 ? null : context.getResources().getDrawable(state[0]);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 切换软键盘
     */
    public static void switchKeyBoardCancle(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    /**
     * 关闭软键盘
     *
     * @param activity
     */
    public static void closeKeyBoard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(activity.getWindow().getDecorView().getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭软键盘
     *
     * @param fragment
     */
    public static void closeKeyBoard(Fragment fragment) {
        InputMethodManager im = (InputMethodManager) fragment.getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(fragment.getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * MD5加密
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.substring(0, md5StrBuff.length()).toString();
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static String getRandomNumber() {
        return new DecimalFormat("0000000000").format(new Random().nextInt(1000000000));
    }

    /**
     * 获取指定区间的随机数
     *
     * @return
     */
    public static int getRandomNumber(int start, int end) {
        Random rand = new Random();
        int randNum = rand.nextInt(end) + start;
        return randNum;
    }

    /**
     * 截取并按规则组合字符串
     *
     * @return
     */
    public static String subAndCombinationString(String str, int subLength, boolean isReduction) {
        if (isReduction) {
            String str1 = str.substring(0, subLength);
            String str2 = str.replace(str1, "");
            String result = str2 + str1;
            return result;
        } else {
            String temp = str.substring(0, str.length() - subLength);
            String str1 = temp.substring(0, subLength);
            String str2 = temp.replace(str1, "");
            String str3 = str.replace(temp, "");
            String result = str3 + str1 + str2;
            return result;
        }
    }

    /**
     * 调用照相机获取图片
     *
     * @param activity
     * @param code
     */
    public static void getSystemCamera(Activity activity, int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        activity.startActivityForResult(intent, code);
    }

    /**
     * Fragment调用系统相机获取图片原图
     *
     * @param fragment
     * @param code
     * @param imageFile 照片要保存的文件
     */
    public static void getSystemCamera(Fragment fragment, int code, File imageFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri u = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        fragment.startActivityForResult(intent, code);
    }

    /**
     * Activity调用系统相机获取图片原图
     *
     * @param fragment
     * @param code
     * @param imageFile 照片要保存的文件
     */
    public static void getSystemCamera(Activity fragment, int code, File imageFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri u = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        fragment.startActivityForResult(intent, code);
    }

    /**
     * 获取系统图片
     *
     * @param activity
     * @param code
     */
    public static void getSystemPhoto(Activity activity, int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        activity.startActivityForResult(intent, code);
    }

    /**
     * 保存一张图片到sd卡
     *
     * @param bitmapPath
     * @param bitmapName
     * @param mBitmap
     */
    public static void saveBitmapToSdcard(String bitmapPath, String bitmapName, Bitmap mBitmap) {
        FileOutputStream fOut = null;
        try {
            File f = new File(bitmapPath, bitmapName);
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            f.createNewFile();

            fOut = new FileOutputStream(f);

            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.flush();
                    fOut.close();
                    fOut = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * string转boolean
     *
     * @param s
     * @return
     */
    public static boolean stringToBoolean(String s) {
        if (s != null) {
            if (s.equals("1")) {
                return true;
            } else if (s.equals("0")) {
                return false;
            } else {
                return Boolean.parseBoolean(s);
            }
        }
        return false;
    }

    /**
     * 获取uuid
     *
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 退出软件
     */
    public static void exitApp() {
        Context context = ApplicationData.globalContext;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.restartPackage(context.getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 保存数据到sd卡
     *
     * @param content
     * @param fileName
     * @param filePath
     * @param isGzip   true压缩保存
     * @param isAppend true续写文件，false重新写文件
     * @return 0:成功，1：sd卡错误，2：其他错误,3:存储卡已满
     */
    public synchronized static void writeDataToSdcard(byte[] content, String filePath, String fileName, boolean isGzip, boolean isAppend) throws Exception {
        FileOutputStream fos = null;
        GZIPOutputStream gzin = null;
        try {
            // 判断当前的可用盘符，优先使用sdcard
            File testDir = new File(filePath);
            if (!testDir.exists()) {
                testDir.mkdirs();
            }

            File file = new File(filePath + fileName);
            if (isAppend) {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } else {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            }

            if (file.exists() && file.canWrite()) {
                fos = new FileOutputStream(file, isAppend);
                if (isGzip) {
                    gzin = new GZIPOutputStream(fos);
                    gzin.write(content);
                    gzin.flush();
                } else {
                    fos.write(content);
                    fos.flush();
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (gzin != null) {
                    gzin.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定画笔的文字高度
     *
     * @param paint
     * @return
     */
    public static float getFontHeight(Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * 获取全部图片的_id和路径
     *
     * @return
     */
    public static LinkedHashMap<Long, String> getAllImages(Context context) {
        LinkedHashMap<Long, String> images = new LinkedHashMap<Long, String>();
        try {
            // 得到数据库所有图片的Cursor
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Uri uri = intent.getData();
            String[] proj = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");
            while (cursor.moveToNext()) {
                // 获取图片的_id
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                // 获取图片的路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                images.put(id, path);
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    /**
     * 的到图片的缩略图
     *
     * @param context
     * @param imageId 数据库中的图片_id
     * @return
     */
    public static Bitmap getImageThumbnail(Context context, long imageId) {
        Bitmap bitmap = null;
        // 根据ID获取缩略图
        bitmap = Thumbnails.getThumbnail(context.getContentResolver(), imageId, Thumbnails.MICRO_KIND, null);
        return bitmap;
    }

    /**
     * 获取全部文件夹下的图片的_id和路径
     *
     * @return
     */
    public static LinkedHashMap<Long, String> getAllImages(Context context, String folderPath) {
        LinkedHashMap<Long, String> images = new LinkedHashMap<Long, String>();
        try {
            // 得到数据库所有图片的Cursor
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Uri uri = intent.getData();
            String[] proj = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");
            while (cursor.moveToNext()) {
                // 获取图片的_id
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                // 获取图片的路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                int lastIndexOf = path.lastIndexOf(File.separator);
                String substring = path.substring(0, lastIndexOf);
                if (folderPath.equals(substring)) {
                    images.put(id, path);
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    /**
     * 获取视频第一帧图片
     *
     * @param videoPath 视频地址
     * @return
     */
    public static Bitmap getVideoFirstFrame(String videoPath) {
        Bitmap bitmap = null;
        try {
            if (!TextUtils.isEmpty(videoPath)) {
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(videoPath);
                bitmap = media.getFrameAtTime(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取系统状态栏的高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBar(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.top;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 是否匹配第一位是1共11位数字的正则表达式
     *
     * @param no
     * @return
     */
    public static boolean matchesPhoneNo(String no) {
        String pattern = "^[1]\\d{10}$";
        return Pattern.compile(pattern).matcher(no).matches();
    }

    /**
     * 判断手机位数是否正确
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        return !TextUtils.isEmpty(mobiles) && mobiles.length() == 11 && TextUtils.isDigitsOnly(mobiles);
    }

    public static int toInt(String value) {
        int intValue = 0;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return intValue;
    }

    /**
     * 拷贝到粘贴板
     *
     * @param str
     */
    public static void copyToClipboard(String str) {
        ClipboardManager myClipboard = (ClipboardManager) ApplicationData.globalContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", str);
        myClipboard.setPrimaryClip(myClip);
        showToast("复制成功！", false);
    }

    /**
     * 上次点击的时间
     */
    private static long lastClickTime = 0;

    /**
     * 按钮是不是连续被按下
     *
     * @return
     */
    public static boolean isFastDoubleClick(int timeDifference) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < timeDifference) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 点击次数
     */
    private static int clickNum = 0;

    /**
     * 是否连续点击
     */
    public static boolean isContinuousClick() {
        long time = System.currentTimeMillis();
        long timeDif = time - lastClickTime;
        //判断两次时间是否相差3s
        if (timeDif > 2000 && lastClickTime != 0) {
            clickNum = 0;
        } else {
            clickNum++;
        }
        lastClickTime = time;
        if (clickNum > 6) {
            clickNum = 0;
            lastClickTime = 0;
            return true;
        }
        return false;
    }

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<String, Object>(0);
        if (TextUtils.isEmpty(param)) {
            return map;
        } else if (param.indexOf("?") == -1) {
            return map;
        }

        param = param.substring(param.indexOf("?") + 1);
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 颜色转换string to int16
     *
     * @param value
     * @return
     */
    public static int getColorStringToInt(String value) {
        try {
            if (value != null && value.length() > 0) {
                if (!value.startsWith("#")) {
                    value = "#" + value;
                }
                int color = Color.parseColor(value);
                return color;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取颜色值
     *
     * @param id
     * @return
     */
    public static int getColorToInt(int id) {
        return ApplicationData.globalContext.getResources().getColor(id);
    }

    /**
     * 判断是否为http链接
     *
     * @param url
     * @return
     */
    public static boolean isHttpLink(String url) {
        return URLUtil.isNetworkUrl(url);
    }

    /**
     * 截取后缀
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            return fileName.substring(index + 1);
        }
        return "";
    }

    /**
     * 获取当前电量
     *
     * @param context context
     * @return 电量 （0.0 ~ 1.0f）
     */
    public static float getPowerPercent(Context context) {
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return level / (float) scale;
    }

    /**
     * 启动QQ并发起会话
     *
     * @param qqNumber
     */
    public static void launchQQChat(Activity activity, String qqNumber) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNumber;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setAction(Intent.ACTION_VIEW);
            activity.startActivity(intent);
        } catch (Exception e) {
            Tools.showToast("您的手机暂未安装QQ客户端", false);
        }

    }

    /**
     * 启动微信客户端
     *
     * @param context
     */
    public static void launchWechat(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (Exception e) {
            Tools.showToast("您的手机暂未安装微信客户端", false);
        }
    }

    /**
     * 打印异常堆栈信息
     *
     * @param ex ex
     * @return String
     */
    private static String getStackTraceString(Throwable ex) {
        StackTraceElement[] traceElements = ex.getStackTrace();

        StringBuilder traceBuilder = new StringBuilder(ex.toString() + "\n");

        if (traceElements != null && traceElements.length > 0) {
            for (StackTraceElement traceElement : traceElements) {
                traceBuilder.append(traceElement.toString());
                traceBuilder.append("\n");
            }
        }

        return traceBuilder.toString();
    }

    /**
     * 调用隐藏系统默认的输入法
     */
    public static void hideSoftInput(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            return;
        }
    }

    public static void handleException(String tag, Throwable ex) {
        loge(tag, getStackTraceString(ex));

    }

    public static void handleException(Throwable ex) {
        loge("ExceptionTag", getStackTraceString(ex));

    }

    public static void handleException(String ex) {
        loge("ExceptionTag", ex);

    }

    /**
     * 获取drawable 边框的自定义方法，不用每次去新建drawable文件
     * create by Swh
     *
     * @param solidColor       实体颜色
     * @param cornerRadiusInDp dp表示的边角半径
     * @param strokeWidthInDp  dp表示的边线宽度
     * @param strokeColor      边线颜色
     * @return GradientDrawable
     */
    public static GradientDrawable getGradientDrawable(int solidColor, int cornerRadiusInDp, float strokeWidthInDp, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        if (cornerRadiusInDp <= 0) {
        } else {
            drawable.setCornerRadius(dipToPixel(cornerRadiusInDp));
        }
        drawable.setStroke(dipToPixel(strokeWidthInDp), strokeColor);
        drawable.setColor(solidColor);
        drawable.setDither(true);
        drawable.setFilterBitmap(true);
        return drawable;
    }

    /**
     * 获取drawable 边框的自定义方法，适用于边线颜色与实体颜色相同的，不用每次去新建drawable文件
     * create by Swh
     *
     * @param solidColor       实体颜色
     * @param cornerRadiusInDp dp表示的边角半径
     * @return GradientDrawable
     */
    public static Drawable getGradientDrawable(int solidColor, int cornerRadiusInDp) {
        return getGradientDrawable(solidColor, cornerRadiusInDp, 0, solidColor);
    }

    public static String intToHex(int n) {
        //StringBuffer s = new StringBuffer();
        StringBuilder sb = new StringBuilder(8);
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            sb = sb.append(b[n % 16]);
            n = n / 16;
        }
        a = sb.reverse().toString();
        return a;
    }

    /**
     * 获取CPU型号
     */
    public static String getCPUName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text;
            String last = "";
            while ((text = br.readLine()) != null) {
                last = text;
            }
            //一般机型的cpu型号都会在cpuinfo文件的最后一行
            if (last.contains("Hardware")) {
                String[] hardWare = last.split(":\\s+", 2);
                return hardWare[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Build.HARDWARE;
    }

    /**
     * 是否是 mtk cup
     *
     * @return 是 true 否 false
     */
    public static boolean isMtkCpu() {
        boolean flag = false;
        String cpuName = getCPUName();
        if (!TextUtils.isEmpty(cpuName)) {
            flag = cpuName.startsWith("mt") || cpuName.startsWith("MT");
        }
        return flag;
    }

    /**
     * 获取 url 中的参数键值对
     *
     * @param url url
     * @return map
     */
    public static Map<String, String> getQueryParams(String url) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }
                    params.put(key, value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    /**
     * 获取指定个数的随机数
     *
     * @param digits 几位数
     * @return int
     */
    public static int getRandom(int digits) {
        if (digits <= 0) {
            return 0;
        } else {
            int max = (int) Math.pow(10, digits);
            int num = new Random().nextInt(max);
            while (String.valueOf(num).length() < digits) {
                num = new Random().nextInt(max);
            }
            return num;
        }
    }

    /**
     * 获取虚拟机名
     * @return
     */
    public static String getVMName(){
        try{
            String vmname = System.getProperty("java.vm.name");
            return vmname;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取虚拟机名
     * @return
     */
    public static String getVMVersion(){
        try{
            String version = System.getProperty("java.vm.version");
            return version;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取设备制造商
     * @return
     */
    public static String getMANUFACTURER(){
        try{
            String MANUFACTURER = Build.MANUFACTURER;
            return MANUFACTURER;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
