package com.bokecc.common.log.request;

import com.bokecc.common.http.listener.RequestListener;
import com.bokecc.common.log.CCLogConfig;
import com.bokecc.common.log.CCLogRequestCallback;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 上报接口
 * @author wy
 */
public class CCEventReportRequest extends CCLogBaseRequest implements RequestListener{

    /**
     * 请求网络
     * @param business business
     * @param sdkVer sdkVer
     * @param baseParams baseParams
     * @param businessParams businessParams
     */
    public CCEventReportRequest(String path,int timeout,String business, String sdkVer, HashMap<String, Object> baseParams, HashMap<String, Object> businessParams, CCLogRequestCallback ccLogRequestCallback) {
        super(ccLogRequestCallback);
        setTimeout(timeout);
        request(path,business, sdkVer, baseParams, businessParams, ccLogRequestCallback);
    }
    /**
     * 请求网络
     * @param business business
     * @param sdkVer sdkVer
     * @param baseParams baseParams
     * @param businessParams businessParams
     */
    public CCEventReportRequest(int timeout,String business, String sdkVer, HashMap<String, Object> baseParams, HashMap<String, Object> businessParams, CCLogRequestCallback ccLogRequestCallback) {
        super(ccLogRequestCallback);
        setTimeout(timeout);
        request(null,business, sdkVer, baseParams, businessParams, ccLogRequestCallback);
    }
    /**
     * 请求网络
     * @param business business
     * @param sdkVer sdkVer
     * @param baseParams baseParams
     * @param businessParams businessParams
     */
    public CCEventReportRequest(String business, String sdkVer, HashMap<String, Object> baseParams, HashMap<String, Object> businessParams, CCLogRequestCallback ccLogRequestCallback) {
        super(ccLogRequestCallback);
        request(null,business, sdkVer, baseParams, businessParams, ccLogRequestCallback);
    }

    private void request(String path,String business, String sdkVer, HashMap<String, Object> baseParams, HashMap<String, Object> businessParams, CCLogRequestCallback ccLogRequestCallback){
        HashMap<String, Object> params = new HashMap<>();
        //固定数据，不需要上层传递
        params.put("platform", "android-sdk");
        params.put("rid", System.currentTimeMillis());
        params.put("cc-secret",getSecret());

        //必传数据
        params.put("business",business);
        params.put("appVer", sdkVer);
        params.put("ver", sdkVer);
        //业务基础数据
        params.putAll(baseParams);
        //业务事件数据
        params.putAll(businessParams);

        if(path == null || path.length() == 0){
            path = CCLogConfig.EVENT;
            if(business.equals(CCLogConfig.BUSINESS_VOD)){
                path = CCLogConfig.EVENTVOD;
            }else if(business.equals(CCLogConfig.BUSINESS_LIVE)){
                path = CCLogConfig.EVENTLIVE;
            }else if(business.equals(CCLogConfig.BUSINESS_CLASS)){
                path = CCLogConfig.EVENT;
            }
        }

        onPost(CCLogConfig.BASE_URL + path, params, this);
    }

    @Override
    public Object onParserBody(JSONObject jsonObject) throws Exception {
        return jsonObject;
    }

    @Override
    public boolean onHandleCode(int responseCode, String responseMessage, Object o) {
        return false;
    }

    @Override
    public void onRequestSuccess(Object o) {
        if(callback != null){
            callback.onSuccess(o);
        }
    }

    @Override
    public void onRequestFailed(int errorCode,String errorMsg) {
        if(callback != null){
            callback.onFailure(errorCode,errorMsg);
        }
    }

    @Override
    public void onRequestCancel() {}

}
