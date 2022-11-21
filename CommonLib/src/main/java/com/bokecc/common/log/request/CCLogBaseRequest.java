package com.bokecc.common.log.request;

import android.content.Context;
import android.util.Base64;

import com.bokecc.common.http.BaseRequest;
import com.bokecc.common.log.CCLogRequestCallback;
import com.bokecc.common.utils.Tools;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务联网基类
 * 此类负责实现业务层网络访问的公用业务数据
 * （1）请求头的设置：getHeaders
 * （2）返回消息的解析，parserTask
 * （3）状态码处理：finishTask；
 * 可以直接使用，也可以继承重写
 * Created by wangyue on 17/1/23.
 */
public class CCLogBaseRequest<T> extends BaseRequest{

    /**token*/
    protected static String token = "";

    /** 操作成功的返回码 */
    public final int SUCCESS_OPERATION = 0;

    /**解析状态码标签*/
	private String keyCode = "code";
	/**解析状态提示标签*/
    private String keyMessage = "message";
	/**解析数据标签*/
    private String keyData = "data";

    /**联网返回的状态码*/
    protected int responseCode = -1;
    /**联网返回的状态信息*/
    private String responseMessage = "";

    protected CCLogRequestCallback<T> callback;

    /**
     * 构造方法
     * 在此方法中发起联网，调用如下方法
     *
     */
    public CCLogBaseRequest() {
        super();
    }

    /**
     * 构造方法
     * 在此方法中发起联网，调用如下方法
     *
     */
    public CCLogBaseRequest(CCLogRequestCallback<T> callback) {
        super();
        this.callback = callback;
    }

    /**
     * 构造方法
     * @param context 传此参数会有基础滚动条出现
     * @param callback
     */
    public CCLogBaseRequest(Context context, CCLogRequestCallback<T> callback) {
        super(context);
        this.callback = callback;
    }

    /**
     * 获取请求header
     * @return
     */
    @Override
    protected Map<String, String> getRequestHeaders() {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("phoneInfo", getPhoneInfo());
        return map;
    }

//    /**
//     * 获取手机信息
//     * @return
//     */
//    private String getPhoneInfo() {
//        //系统版本号
//        String sv = "";
//        try {
//            sv = URLEncoder.encode(Tools.getSystemVersion(), "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        //手机型号
//        String pm = "";
//        try {
//            pm = URLEncoder.encode(Tools.getPhoneModel(), "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        //屏幕尺寸
//        String ss = Tools.getScreenWidth() + "*" + Tools.getScreenHeight();
//
//        //手机唯一码
//        String did = Tools.getAndroidID();
//
//        String phoneInfo =
//                "sv=" + sv + ";"
//                        + "pm=" + pm + ";"
//                        + "ss=" + ss + ";"
//                        + "did="+ did+ ";"
//                ;
//        return phoneInfo;
//    }

    /**
     * 解析数据，如果解析的数据不止状态码，子类可以重写此方法
     * 线程：子线程
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected Object parserTask(String response) throws Exception{
        if(response != null && response.length() > 0){
            if(response.equals("ok")){
                responseCode = SUCCESS_OPERATION;
            }else{
                JSONObject jsonObject = new JSONObject(response);
                String result = jsonObject.optString("result");
                if(result != null && result.equals("FAIL")){
                    JSONObject errorJsonObject = jsonObject.optJSONObject("error");
                    if(errorJsonObject != null){
                        //解析状态码
                        responseCode = errorJsonObject.optInt(this.keyCode);
                        //解析状态信息
                        responseMessage = errorJsonObject.optString(this.keyMessage);
                    }else{//兼容：发现有FAIL返回后，没有error的情况
                        responseMessage = jsonObject.optString("error_msg");
                    }
                    return null;
                } else if(result != null && result.equals("OK")){
                    responseCode = SUCCESS_OPERATION;
                    //解析状态码之后的信息，子类实现
                    if (!jsonObject.isNull(this.keyData)) {
                        JSONObject object = jsonObject.optJSONObject(this.keyData);
                        if(object != null){//应该全部是data才对，但是发现有垃圾数据吧，部分接口不符合规范
                            return requestListener.onParserBody(jsonObject.getJSONObject(this.keyData));
                        }else{
                            return requestListener.onParserBody(jsonObject);
                        }
                    }else{
                        return requestListener.onParserBody(jsonObject);//兼容：有的数据没有包含在data中，直接返回整个结构
                    }
                }
            }
        }else{
            return requestListener.onParserBody(new JSONObject());
        }
        return response;
    }

    /**
     * 联网完成，回到主线程，处理状态码
     * 判断子类是否处理了状态码，没有的话基类统一处理状态码
     * 线程：主线程
     * @param t
     * @throws Exception
     */
    @Override
    protected void finishTask(Object t){
        if(requestListener != null && requestListener.onHandleCode(responseCode,responseMessage,t)){
            return;
        }else{
            switch(responseCode){
                case SUCCESS_OPERATION:
                    if(requestListener != null){
                        requestListener.onRequestSuccess(t);
                    }
                    break;
                default://处理非正常状态码
                    if(requestListener !=null){
                        requestListener.onRequestFailed(responseCode,responseMessage);
                    }else{
                        Tools.showToast(responseMessage,false);
                    }
                    break;
            }
        }
    }

    /**
     * 加密字符串，这里只生成一次，提高效率
     * @Version 0.1.23
     * @date 2021.11.12
     */
    private static String secretStr = null;
    /**
     * 获取加密数据块
     * @return
     */
    protected String getSecret(){
        try{
            if(secretStr == null){
                HashMap<String, Object> paramsSecret = new HashMap();
                paramsSecret.put("ua", "systemVersion:"+Tools.getSystemVersion() + ",phoneModel:" + Tools.getPhoneModel());
                paramsSecret.put("system", Tools.getSystemVersion());
                paramsSecret.put("uuid", Tools.getAndroidID());
//                paramsSecret.put("cip", Tools.getLocalIpAddress());
                String jsonStr = new JSONObject(paramsSecret).toString();
                secretStr = Base64.encodeToString(jsonStr.getBytes(),Base64.DEFAULT);
            }
            return secretStr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
