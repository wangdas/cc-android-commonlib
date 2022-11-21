package com.bokecc.common.log;

import com.bokecc.common.log.request.CCEventReportRequest;

import java.util.HashMap;

/**
 * CCClassLogManager
 *
 * @author zww
 */
public class CCClassLogManager extends CCLogManager {

    /**
     * sdk版本号
     */
    private String sdkVer;

    protected CCClassLogManager() {

    }

    public static CCClassLogManager getInstance() {
        return CCClassLogManager.HdClassAtlasClient.INSTANCE;
    }

    private static class HdClassAtlasClient {
        private static final CCClassLogManager INSTANCE = new CCClassLogManager();
    }

    /**
     * @param sdkVer sdk版本号
     */
    public void init(String sdkVer) {
        this.init(CCLogConfig.BUSINESS_CLASS, sdkVer);
    }

    /**
     * @param business 业务类型
     * @param sdkVer   sdk版本号
     */
    @Override
    public void init(String business, String sdkVer) {
        this.business = CCLogConfig.BUSINESS_CLASS;
        this.sdkVer = sdkVer;
    }

    @Override
    public void setBaseInfo(HashMap<String, Object> params) {
        super.setBaseInfo(params);
        if (params != null && !params.isEmpty() && params.containsKey(BUSINESS)) {
            params.put(BUSINESS, CCLogConfig.BUSINESS_CLASS);
        }
    }

    /**
     * 实时日志上报
     *
     * @param event     event
     * @param code      code
     * @param startTime startTime
     * @param level     level
     * @param data      data
     */
    @Override
    public void log(String event, int code, long startTime, int level, Object data, CCLogRequestCallback logRequestCallback) {
        HashMap<String, Object> businessParams = new HashMap<>(16);
        businessParams.put("event", event);
        businessParams.put("code", code);
        if (startTime > 0) {
            businessParams.put("responseTime", (System.currentTimeMillis() - startTime));
        }
        businessParams.put("level", level);
        businessParams.put("data", data);
        new CCEventReportRequest(CCLogConfig.BUSINESS_CLASS, sdkVer, baseParams, businessParams, logRequestCallback);
    }

    /**
     * 实时日志上报
     *
     * @param params params
     */
    @Override
    public void log(HashMap<String, Object> params) {
        new CCEventReportRequest(CCLogConfig.BUSINESS_CLASS, this.sdkVer, this.baseParams, params, null);
    }

}
