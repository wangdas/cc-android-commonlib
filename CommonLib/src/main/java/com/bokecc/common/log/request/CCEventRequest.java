package com.bokecc.common.log.request;

import com.bokecc.common.http.listener.RequestListener;
import com.bokecc.common.log.CCLogConfig;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 实时日志上报接口
 * @author wy
 */
@Deprecated
public class CCEventRequest extends CCLogBaseRequest implements RequestListener{

    public CCEventRequest(String business,String appid,String sdkVer,String role,String roomId,
                          String userId, String username,int serviceplatform,String cdn,
                          String event,int code,long startTime, int level,Object data) {
        super();

        HashMap<String, Object> params = new HashMap<>();
        //固定数据，不需要上层传递
        params.put("platform", "android-sdk");
        params.put("rid", System.currentTimeMillis());
        params.put("cc-secret",getSecret());

        //基本数据
        params.put("business", business);
        params.put("appid", appid);
        params.put("appVer", sdkVer);
        params.put("ver", sdkVer);

        params.put("role", role);
        params.put("roomid", roomId);
        params.put("userid", userId);
        params.put("username", username);
        params.put("serviceplatform", serviceplatform);
//        params.put("device", device);
        params.put("cdn", cdn);

        //事件数据
        params.put("event", event);
        params.put("code", code);
        if (startTime > 0) {
            params.put("responseTime", (System.currentTimeMillis() - startTime));
        }
        params.put("level", level);
        params.put("data", data);

        onPost(CCLogConfig.BASE_URL + CCLogConfig.EVENT, params, this);
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
    }

    @Override
    public void onRequestFailed(int errorCode, String errorMsg) {
    }

    @Override
    public void onRequestCancel() {
    }

}
