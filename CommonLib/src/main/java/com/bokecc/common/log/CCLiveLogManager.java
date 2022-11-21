package com.bokecc.common.log;

import com.bokecc.common.log.request.CCEventReportRequest;

import java.util.HashMap;

/**
 * CCLiveLogManager
 * @author Zhang
 */
public class CCLiveLogManager extends CCLogManager{

    protected CCLiveLogManager(){

    }

    public static CCLiveLogManager getInstance() {
        return HdLiveAtlasClient.INSTANCE;
    }

    private static class HdLiveAtlasClient {
        private static final CCLiveLogManager INSTANCE = new CCLiveLogManager();
    }

    /**
     * sdk版本号
     */
    private String sdkVer;


    /**
     * base init
     * @param sdkVer   sdk版本号
     */
    public void init(String sdkVer){
        this.init(CCLogConfig.BUSINESS_LIVE,sdkVer);
    }

    /**
     * base init
     * @param business 业务类型
     * @param sdkVer   sdk版本号
     */
    @Override
    public void init(String business, String sdkVer) {
        this.business = CCLogConfig.BUSINESS_LIVE;
        this.sdkVer = sdkVer;
    }

    /**
     * 实时日志上报
     *
     * @param params params
     */
    @Override
    public void log(HashMap<String, Object> params) {
        if (params!=null||!params.isEmpty()){
            if (params.containsKey(BUSINESS)){
                params.put(BUSINESS,CCLogConfig.BUSINESS_LIVE);
            }
        }
        new CCEventReportRequest(CCLogConfig.BUSINESS_LIVE, sdkVer, baseParams, params, null);
    }

    /**
     * 实时日志上报
     *
     * @param params params
     */
    @Override
    public void log(HashMap<String, Object> params,int timeout) {
        if (params!=null||!params.isEmpty()){
            if (params.containsKey(BUSINESS)){
                params.put(BUSINESS,CCLogConfig.BUSINESS_LIVE);
            }
        }
        new CCEventReportRequest(timeout,CCLogConfig.BUSINESS_LIVE, sdkVer, baseParams, params, null);
    }

    /**
     * 实时日志上报
     *
     * @param params params
     */
    @Override
    public void log(HashMap<String, Object> params,int timeout,String path) {
        if (params!=null||!params.isEmpty()){
            if (params.containsKey(BUSINESS)){
                params.put(BUSINESS,CCLogConfig.BUSINESS_LIVE);
            }
        }
        new CCEventReportRequest(path,timeout,CCLogConfig.BUSINESS_LIVE, sdkVer, baseParams, params, null);
    }
}
