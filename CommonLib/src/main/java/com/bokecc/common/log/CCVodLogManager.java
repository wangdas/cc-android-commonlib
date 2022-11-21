package com.bokecc.common.log;

import java.util.HashMap;

/**
 * CCVodLogManager
 * @author Zhang
 */
public class CCVodLogManager extends CCLogManager{

    protected CCVodLogManager(){

    }

    public static CCVodLogManager getInstance() {
        return CCVodLogManager.HdVodAtlasClient.INSTANCE;
    }

    private static class HdVodAtlasClient {
        private static final CCVodLogManager INSTANCE = new CCVodLogManager();
    }

    /**
     * base init
     * @param sdkVer   sdk版本号
     */
    public void init(String sdkVer){
        this.init(CCLogConfig.BUSINESS_VOD,sdkVer);
    }

    /**
     * base init
     * @param business 业务类型
     * @param sdkVer   sdk版本号
     */
    @Override
    public void init(String business, String sdkVer) {
        this.business = CCLogConfig.BUSINESS_VOD;
        this.sdkVer = sdkVer;
    }

    @Override
    public void setBaseInfo(HashMap<String, Object> params) {
        super.setBaseInfo(params);
        if (params!=null && !params.isEmpty() && params.containsKey(BUSINESS)){
            params.put(BUSINESS,CCLogConfig.BUSINESS_VOD);
        }
    }

    @Override
    public void logReport(HashMap<String, Object> params) {
        super.logReport(params);
        if (params!=null && !params.isEmpty() && params.containsKey(BUSINESS)){
            params.put(BUSINESS,CCLogConfig.BUSINESS_VOD);
        }
    }
}
