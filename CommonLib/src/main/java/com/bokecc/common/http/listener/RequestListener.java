package com.bokecc.common.http.listener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求监听者
 * 
 * RequestListener
 */
public interface RequestListener {

	/**
	 * 解析数据
	 * 线程：子线程
	 * 可以进行耗时操作，如操作数据库，读写文件，不可以放UI相关代码
	 * @param jsonObject 可解析对象
	 * @return 解析完成的对象，最后返回到onHandleCode和onRequestSuccess中
	 * @throws JSONException 解析异常
	 */
	Object onParserBody(JSONObject jsonObject) throws Exception;

	/**
	 * 业务状态码处理
	 * 线程：主线程
	 * 返回true，父类不处理状态码，之后的成功失败方法也不再执行
	 * 返回false，父类处理状态码
	 * @param responseCode 状态码
	 * @param responseMessage 状态提示信息
	 * @param o 解析的对象
	 * @return
	 */
	boolean onHandleCode(int responseCode,String responseMessage,Object o);

	/**
	 * 联网解析完成，处理一些UI事件，严禁放入耗时操作，如联网，操作数据库，操作本地文件
	 * @param o 解析对象
	 */
	void onRequestSuccess(Object o);

	/**
	 * 联网错误
	 * @param errorCode 错误码
	 * @param errorMsg 错误信息
	 * @return
	 */
	void onRequestFailed(int errorCode,String errorMsg);

	/**
	 * 取消联网，
	 * 用户如果取消联网，自行处理取消的逻辑
	 */
	void onRequestCancel();

}
