package com.bokecc.common.log;

import com.bokecc.common.application.ApplicationData;
import com.bokecc.common.log.model.CCLogInfo;
import com.bokecc.common.log.request.CCEventReportRequest;
import com.bokecc.common.log.request.CCEventRequest;
import com.bokecc.common.log.request.CCUpdateLogFileInfoRequest;
import com.bokecc.common.log.request.CCUpdateLogFileRequest;
import com.bokecc.common.utils.Tools;
import com.bokecc.xlog.CCLog;

import java.io.File;
import java.util.HashMap;

/**
 * log管理类
 */
public class CCLogManager {

    protected static final String BUSINESS = "business";

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    /**
     * 业务类型
     */
    protected String business = "";
    /**
     * sdk版本号
     */
    protected String sdkVer = "";
    protected String appId = "";
    protected String role = "";
    protected String roomId = "";
    protected String userId = "";
    protected String username = "";
    protected int servicePlatform = 0;
    protected String cdn = "";

    public static CCLogManager getInstance() {
        return HdAtlasClient.INSTANCE;
    }

    private static class HdAtlasClient {
        private static final CCLogManager INSTANCE = new CCLogManager();
    }

    protected CCLogManager() {}

    /**
     * @param business 业务类型
     * @param sdkVer   sdk版本号
     */
    public void init(String business, String sdkVer) {
        this.business = business;
        this.sdkVer = sdkVer;
    }

    protected HashMap<String, Object> baseParams = new HashMap<>();

    /**
     * 设置基础数据
     *
     * @param params
     */
    public void setBaseInfo(HashMap<String, Object> params) {
        this.baseParams = params;
    }

    /**
     * 实时日志上报
     *
     * @param event
     * @param code
     * @param startTime
     * @param level
     * @param data
     */
    public void log(String event, int code, long startTime, int level, Object data) {
        log(event, code, startTime, level, data, null);
    }

    /**
     * 实时日志上报
     *
     * @param event
     * @param code
     * @param startTime
     * @param level
     * @param data
     */
    public void log(String event, int code, long startTime, int level, Object data, CCLogRequestCallback logRequestCallback) {
        HashMap<String, Object> businessParams = new HashMap();
        //事件数据
        businessParams.put("event", event);
        businessParams.put("code", code);
        if (startTime > 0) {
            businessParams.put("responseTime", (System.currentTimeMillis() - startTime));
        }
        businessParams.put("level", level);
        businessParams.put("data", data);
        new CCEventReportRequest(business, sdkVer, baseParams, businessParams, logRequestCallback);
    }

    /**
     * 实时日志上报
     *
     * @param params
     */
    public void log(HashMap<String, Object> params) {
        new CCEventReportRequest(this.business, this.sdkVer, this.baseParams, params, null);
    }

    /**
     * 实时日志上报
     *
     * @param params params
     */
    public void log(HashMap<String, Object> params,int timeout) {
        new CCEventReportRequest(timeout,this.business, this.sdkVer, this.baseParams, params, null);
    }

    /**
     * 实时日志上报
     *
     * @param params params
     */
    public void log(HashMap<String, Object> params,int timeout,String path) {
        new CCEventReportRequest(path,timeout,this.business, this.sdkVer, this.baseParams, params, null);
    }

    /**
     * 离线日志上报
     *
     * @param firstFileName
     * @param callBack
     */
    public void reportLogInfo(final String firstFileName, final CCLogRequestCallback<String> callBack) {
        //日志写入
        Tools.log("LogUpdate",Tools.getCurrentDateTimeWithSS());
        CCLog.flush();

        //当天本地日志路径
        String filePath = Tools.getSdcardRootPath(ApplicationData.globalContext) + CCLogConfig.logPath + "/" + CCLogConfig.fileName;
        File tempFile = new File(filePath + "_" + Tools.getDate(Tools.getCurrentTimeMillis(), "yyyyMMdd") + CCLogConfig.suffix);
        if (!tempFile.exists()) {
            tempFile = new File(filePath);//兼容第二种日志方式
        }
        final File file = tempFile;
        //判断是否符合条件
        if (firstFileName == null || firstFileName.length() == 0) {
            if (callBack != null) {
                callBack.onFailure(CCLogConfig.ERR_Update_Log_First_file_Name_NO, "firstFileName == null!");
            }
        } else if (!file.exists()) {
            if (callBack != null) {
                callBack.onFailure(CCLogConfig.ERR_Update_Log_File_NO, "File does not exist!");
            }
        } else {
            new CCUpdateLogFileInfoRequest(new CCLogRequestCallback() {
                @Override
                public void onSuccess(Object response) {
                    String ossFileName = firstFileName + "_android_" + Tools.getCurrentDateTime() + CCLogConfig.suffix;//上传到阿里云平台的文件名
                    CCLogInfo logInfo = (CCLogInfo) response;
                    new CCUpdateLogFileRequest(logInfo, file, ossFileName, new CCLogRequestCallback() {
                        @Override
                        public void onSuccess(Object response) {
                            file.delete();//删除本地文件
                            callBack.onSuccess("");
                        }

                        @Override
                        public void onFailure(int errorCode, String errorMsg) {
                            callBack.onFailure(errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    callBack.onFailure(errorCode, errorMsg);
                }
            });
        }
    }

    /**
     * 设置基础信息
     *
     * @param appId           appId
     * @param role            用户角色
     * @param roomId          房间id
     * @param userId          用户id
     * @param username        用户名
     * @param serviceplatform 流平台
     * @param cdn             节点
     */
    @Deprecated
    public void setBaseInfo(String appId, String role, String roomId, String userId, String username, int serviceplatform, String cdn) {
        this.appId = appId;
        this.role = role;
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.servicePlatform = serviceplatform;
        this.cdn = cdn;
    }

    /**
     * 日志上报
     *
     * @param event
     * @param code
     */
    @Deprecated
    public void log(String event, int code, long startTime, Object data) {
        new CCEventRequest(business, appId, sdkVer, role, roomId, userId, username, servicePlatform, cdn, event, code, startTime, INFO, data);
    }

    /**
     * 实时日志上报，兼容v0.1.11
     *
     * @param params
     */
    @Deprecated
    public void logReport(HashMap<String, Object> params) {
        log(params);
    }

    /**
     * 离线日志上报
     *
     * @param file     文件
     * @param fileName 文件名
     * @param callBack 回调
     */
    @Deprecated
    public void reportLogInfo(final File file, final String fileName, final CCLogRequestCallback<String> callBack) {
        new CCUpdateLogFileInfoRequest(new CCLogRequestCallback() {

            @Override
            public void onSuccess(Object response) {
                if (file.exists()) {
                    CCLogInfo logInfo = (CCLogInfo) response;
                    new CCUpdateLogFileRequest(logInfo, file, fileName, new CCLogRequestCallback() {
                        @Override
                        public void onSuccess(Object response) {
                            callBack.onSuccess(fileName);
                        }

                        @Override
                        public void onFailure(int errorCode, String errorMsg) {
                            callBack.onFailure(errorCode, errorMsg);
                        }
                    });
                } else {
                    callBack.onFailure(CCLogConfig.ERR_Update_Log_File_NO, "File does not exist!");
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                callBack.onFailure(errorCode, errorMsg);
            }
        });
    }


}
