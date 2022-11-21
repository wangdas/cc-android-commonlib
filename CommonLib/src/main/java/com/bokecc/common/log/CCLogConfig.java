package com.bokecc.common.log;

import android.os.Environment;

import com.bokecc.common.BuildConfig;
import com.bokecc.common.application.ApplicationData;
import com.bokecc.common.utils.Tools;

/**
 * 日志相关常量
 */
public final class CCLogConfig {

    /**
     * crash上报的版本号
     */
    public static final String VERSION_NAME= BuildConfig.CommonLibVersion;

    /**
     * 点播
     */
    public static final String BUSINESS_VOD="1001";
    /**
     * 直播
     */
    public static final String BUSINESS_LIVE="2001";
    /**
     * 班课
     */
    public static final String BUSINESS_CLASS="3001";

    /**
     * 上传日志文件错误-文件不存在
     */
    public static final int ERR_Update_Log_File_NO = 9003;

    /**
     * 上传日志文件错误-输入的文件名不正确
     */
    public static final int ERR_Update_Log_First_file_Name_NO = 9004;

    /**直播包名*/
    public static final String LIVE_SDK = "com.bokecc.sdk.mobile.live";

    /**点播包名*/
    public static final String VOD_SDK = "com.bokecc.sdk.mobile";

    /**班课包名*/
    public static final String CLASS_SDK = "com.bokecc.sskt";

    /**用于区分当前崩溃来源*/
    public static final String ORIGIN="com.bokecc";

    /**Caused by*/
    public static final String CAUSED_BY="Caused by";

    /**BASE_URL*/
    public static final String BASE_URL = "https://logger.csslcloud.net";

    /**班课事件*/
    public static final String EVENT = "/event/v1/client";

    /**直播事件*/
    public static final String EVENTLIVE = "/event/live/v1/client";

    /**点播事件*/
    public static final String EVENTVOD = "/event/vod/v1/client";

    /**崩溃统计*/
    public static final String CRASH_REPORT = "/event/common/v1/crash";

    /**获取上传日志token*/
    public static final String UpdateLogFile = "/event/user/log/token";

//    /**
//     * 直播日志
//     */
//    public static final String logLivePath = "/bokecc/log/live";
//
//    /**
//     * 点播日志
//     */
//    public static final String logVodPath = "/bokecc/log/vod";
//
//    /**
//     * 云课堂日志
//     */
//    public static final String logClassPath = "/bokecc/log/class";
//
//    /**
//     * 默认日志
//     */
//    public static final String logDefaultPath = "/bokecc/log";

    /**日志路径*/
    public static final String logPath = "/bokecc/log";
    /**离线日志文件名*/
    public static final String fileName = "bokecc";

    /**错误日志文件名*/
    public static final String crashFileName = "/crashlog";

    /**日志后缀*/
    public static final String suffix = ".xlog";

//    /**
//     * 日志控制台打印是否开启
//     */
//    public static boolean isConsoleLogOpen = true;

//    /**
//     * 是否开启xlog
//     */
//    public static boolean isOpenXlog = true;

    /**
     * 收集到的本地crash日志
     */
    public static final String CRASH_FILE_PATH = "/" + ORIGIN + "/crash";
}
