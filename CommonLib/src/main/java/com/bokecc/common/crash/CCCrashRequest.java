package com.bokecc.common.crash;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.bokecc.common.http.listener.RequestListener;
import com.bokecc.common.log.CCLogConfig;
import com.bokecc.common.log.request.CCLogBaseRequest;
import com.bokecc.common.utils.FileUtil;
import com.bokecc.common.utils.Tools;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 上报crash信息
 * CCCrashRequest
 *
 * @author Zhang
 */
public class CCCrashRequest extends CCLogBaseRequest<Object> implements RequestListener {

    private final File file;

    public CCCrashRequest(File file) {
        synchronized (CCCrashRequest.this) {
            this.file = file;
            if (file == null || !file.exists()) {
                return;
            }
            if (file.length() == 0) {
                boolean delete = file.delete();
                return;
            }
            String crashInfo = "";
            try {
                crashInfo = FileUtil.readTxtFile(file);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (TextUtils.isEmpty(crashInfo)) {
                return;
            }
            HashMap<Object, Object> params = createBaseInfoParams();
            getCrashInfoParams(crashInfo, params);
            onPost(CCLogConfig.BASE_URL + CCLogConfig.CRASH_REPORT, params, this);
        }
    }


    /**
     * 共有或不需要业务sdk传递的值
     */
    private HashMap<Object, Object> createBaseInfoParams() {
        //固定数据，不需要上层传递
        HashMap<Object, Object> params = new HashMap<>();
        params.put("rid", System.currentTimeMillis());
        params.put("system", Tools.getSystemVersion());
        params.put("uuid", Tools.getAndroidID());
        params.put("platform", "android-sdk");
//        params.put("cip", Tools.getLocalIpAddress());
        params.put("ua", "systemVersion:" + Tools.getSystemVersion() + ",phoneModel:" + Tools.getPhoneModel());
        params.put("phone_brand", android.os.Build.BRAND);
        params.put("cpu_abi", Tools.getCpuAbi());
        params.put("os_sdk_version", String.valueOf(Build.VERSION.SDK_INT));
        return params;
    }


    private void getCrashInfoParams(String crashInfo, HashMap<Object, Object> hashMap) {
        if (crashInfo.contains("{")) {
            crashInfo = crashInfo.replace("{", "");
        }
        if (crashInfo.contains("}")) {
            crashInfo = crashInfo.replace("}", "");
        }
        String trim = crashInfo.trim();
        String[] strings = trim.split(",");
        for (String split : strings) {
            String[] info = split.split("=");
            if (info.length == 0) {
                return;
            }
            if (info.length == 1) {
                hashMap.put(info[0].trim(), "");
            } else if (info.length == 2) {
                hashMap.put(info[0].trim(), info[1].trim());
            }
        }
    }

    @Override
    public Object onParserBody(JSONObject jsonObject) {
        return null;
    }

    @Override
    public boolean onHandleCode(int responseCode, String responseMessage, Object o) {
        return false;
    }

    @Override
    public void onRequestSuccess(Object o) {
        if (file != null && file.exists()) {
            boolean delete = file.delete();
        }
    }

    @Override
    public void onRequestFailed(int errorCode, String errorMsg) {
    }

    @Override
    public void onRequestCancel() {
    }
}
