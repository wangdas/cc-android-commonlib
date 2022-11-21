package com.bokecc.common.log.request;

import com.bokecc.common.http.listener.RequestListener;
import com.bokecc.common.log.model.CCLogInfo;
import com.bokecc.common.log.CCLogConfig;
import com.bokecc.common.log.CCLogRequestCallback;
import com.bokecc.common.utils.Sha1Util;
import com.bokecc.common.utils.Tools;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 获取上报日志的token
 */
public class CCUpdateLogFileInfoRequest extends CCLogBaseRequest implements RequestListener {

    public CCUpdateLogFileInfoRequest(CCLogRequestCallback requestCallback){
        super(requestCallback);

        HashMap params = new HashMap();
        String nonce = Tools.getUuid();
        long timestamp = System.currentTimeMillis();
        String secret = "Ibasdfdsafdoj09ni9ucal209sa2";
        params.put("nonce", nonce);
        params.put("timestamp", timestamp);
        params.put("signature", Sha1Util.encryptToSHA(nonce+"&"+timestamp+"&"+secret));

        onGet(CCLogConfig.BASE_URL + CCLogConfig.UpdateLogFile, params,this);
    }

    @Override
    public Object onParserBody(JSONObject jsonObject) {
        CCLogInfo logInfo = new CCLogInfo();
        logInfo.setMedia_cdnurl(jsonObject.optString("media_cdnurl"));
        logInfo.setCallback(jsonObject.optString("callback"));
        logInfo.setSignature(jsonObject.optString("signature"));
        logInfo.setPolicy(jsonObject.optString("policy"));
        logInfo.setAccessid(jsonObject.optString("accessid"));
        logInfo.setHost(jsonObject.optString("host"));
        logInfo.setExpire(jsonObject.optString("expire"));
        logInfo.setDir(jsonObject.optString("dir"));
        return logInfo;
    }

    @Override
    public boolean onHandleCode(int responseCode, String responseMessage, Object o) {
        return false;
    }

    @Override
    public void onRequestSuccess(Object o) {
        callback.onSuccess(o);
    }

    @Override
    public void onRequestFailed(int errorCode,String errorMsg) {
        callback.onFailure(errorCode,errorMsg);
    }

    @Override
    public void onRequestCancel() {

    }
}
