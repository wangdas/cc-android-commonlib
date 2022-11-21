package com.bokecc.common.log.request;

import com.bokecc.common.http.listener.RequestListener;
import com.bokecc.common.log.model.CCLogInfo;
import com.bokecc.common.log.CCLogRequestCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 将日志文件上传到服务器
 */
public class CCUpdateLogFileRequest extends CCLogBaseRequest implements RequestListener {

    /**
     *
     * @param logInfo
     * @param file
     * @param fileName
     * @param requestCallback
     */
    public CCUpdateLogFileRequest(CCLogInfo logInfo, File file, String fileName, CCLogRequestCallback requestCallback){
        super(requestCallback);

        Map<String, String> parms = new HashMap<>();
        parms.put("OSSAccessKeyId", logInfo.getAccessid());
        parms.put("policy", logInfo.getPolicy());
        parms.put("signature", logInfo.getSignature());
        parms.put("key", logInfo.getDir() + "/" + fileName);
        parms.put("success_action_status", "200");
        String host = logInfo.getHost();

        Map<String, File> files = new HashMap<>();
        files.put(fileName,file);

        onPostFile(host,parms,files,this,"");
    }

    @Override
    public Object onParserBody(JSONObject jsonObject) {
        responseCode = SUCCESS_OPERATION;
        return "";
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
