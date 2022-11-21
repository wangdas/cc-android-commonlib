package com.bokecc.common.crash;

import android.text.TextUtils;

import com.bokecc.common.http.listener.RequestListener;
import com.bokecc.common.log.CCLogConfig;
import com.bokecc.common.log.request.CCLogBaseRequest;
import com.bokecc.common.utils.Tools;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * CCUsageTimeRequest
 * 分母统计
 *
 * @author Zhang
 */
public class CCUsageTimeRequest extends CCLogBaseRequest implements RequestListener {

    /**
     * 统计时机
     * 1.app初始化时                open
     */
    public static final String APP_INITIALIZE = "open";
    /**
     * 2.业务sdk首次初始化调用       firstExecute
     */
    public static final String FIRST_EXECUTE = "firstExecute";
    /**
     * 统计时机
     * 3.业务sdk每次功能使用调用     execute
     */
    public static final String EXECUTE = "execute";

    public CCUsageTimeRequest(String statisticalTiming) {
        HashMap<Object, Object> params = new HashMap<>();
        params.put("platform", "android-sdk");
        params.put("rid", System.currentTimeMillis());
        params.put("uuid", Tools.getAndroidID());
        params.put("ua", "systemVersion:" + Tools.getSystemVersion() + ",phoneModel:" + Tools.getPhoneModel());
        params.put("system", Tools.getSystemVersion());
//        params.put("cip", Tools.getLocalIpAddress());
        params.put("ver", CCLogConfig.VERSION_NAME);
        String business = CCCrashManager.getInstance().getBusiness();
        params.put("business", business);
        if (TextUtils.equals(business, CCCrashManager.BUSINESS_VOD)) {
            params.put("appVer", CCCrashManager.getInstance().getVodSdkVersion());
        } else if (TextUtils.equals(business, CCCrashManager.BUSINESS_LIVE)) {
            params.put("appVer", CCCrashManager.getInstance().getLiveSdkVersion());
        } else if (TextUtils.equals(business, CCCrashManager.BUSINESS_CLASS)) {
            params.put("appVer", CCCrashManager.getInstance().getClassSdkVersion());
        } else {
            params.put("appVer", CCCrashManager.getInstance().getBusinessSdkVersion());
        }
        params.put("app_package_name", Tools.getAppPackageName());
        params.put("app_version", Tools.getAppVersionName());
        HashMap<Object, Object> businessParams = CCCrashManager.getInstance().getBusinessParams();
        if (businessParams != null && !businessParams.isEmpty()) {
            params.putAll(businessParams);
        }
        params.put("event", statisticalTiming);
        onPost(CCLogConfig.BASE_URL + CCLogConfig.CRASH_REPORT, params, this);
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
    }

    @Override
    public void onRequestFailed(int errorCode, String errorMsg) {
    }

    @Override
    public void onRequestCancel() {
    }
}
