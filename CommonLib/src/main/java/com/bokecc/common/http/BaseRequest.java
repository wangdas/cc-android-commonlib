package com.bokecc.common.http;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.bokecc.common.exception.NetworkException;
import com.bokecc.common.exception.SdcardException;
import com.bokecc.common.http.bean.ResponseInfo;
import com.bokecc.common.http.config.TipStrConfig;
import com.bokecc.common.http.listener.DownloadListener;
import com.bokecc.common.http.listener.RequestListener;
import com.bokecc.common.utils.Tools;

import org.json.JSONException;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * 联网抽象基类，其他联网类都继承此类
 * 主要实现：
 * （1）异步联网加载
 * （2）方法抽象
 * （3）回调主线程
 * 注意：当前层禁止出现任何业务相关代码
 * @author wangyue
 */
public abstract class BaseRequest<T> {

	private final String TAG = BaseRequest.class.getName();

	/**联网类型*/
	private ConnectionRequest.RequestStatus requestType = ConnectionRequest.RequestStatus.GET;

	/**联网底层类*/
	private ConnectionRequest remoteRequester = new ConnectionRequest();

	/**对话框显示的context*/
	private WeakReference<Context> contextReference = null;

	/**联网监听*/
	protected RequestListener requestListener;
	/**下载监听*/
	protected DownloadListener downloadListener;

	/** 请求头 */
//	private Map<String, Object> requestHeaders;
	/** 返回头 */
	protected Map<String, List<String>> responseHeaders;


	/**加载显示信息*/
	protected String dialogMessage = null;
	/**是否可以取消dialog*/
	protected boolean isCancelDialog = true;
	/**是否显示dialog*/
	protected boolean isShowDialog = true;
	/**取消标记*/
	private boolean cancel = false;


	/**url地址*/
	private String url = "";
	/**上传的参数*/
	private Map<String,Object> params;
	/**上传体*/
	private Map<String,Object> bodyParams;

	/**上传文件的参数*/
	private Map<String,File> files;
	/*以 byte 数组形式上传的参数*/
	private Map<String, byte[]> fileByteArrays;
	/**下载文件路径*/
	private String filePath = "";
	/**下载文件当前长度*/
	private long currentLength = 0;
	/**下载文件分段长度*/
	private long targetLength = 0;

	/**
	 * 构造方法
	 */
	public BaseRequest(){
		this.contextReference = new WeakReference<>(null);
//		requestHeaders = getRequestHeaders();
	}

	/**
	 * 构造方法
	 * @param context
	 */
	public BaseRequest(Context context){
		this.contextReference = new WeakReference<>(context);
//		requestHeaders = getRequestHeaders();
	}

	/**
	 * 获取header
	 * 业务子类实现
	 */
	protected abstract Map<String, Object> getRequestHeaders();


	/**
	 * 获取当前视图context
	 * @return
	 */
	protected Context getActivity(){
		if(contextReference != null && contextReference.get() != null){
			return contextReference.get();
		}
		return null;
	}

	/**
	 * 设置超时时间
	 * @param timeout
	 */
	protected void setTimeout(int timeout){
		remoteRequester.setTimeout(timeout);
	}

	/**
	 * 开始联网，get
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param requestListener 回调监听
	 */
	protected void onGet(String url, Map<String,Object> params,RequestListener requestListener){
		this.requestType = ConnectionRequest.RequestStatus.GET;
		this.requestListener = requestListener;
		startAsyncTask(url,params,null,dialogMessage);
	}

	/**
	 * 开始联网，get
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param requestListener 回调监听
	 * @param dialogMessage 提示信息，为空不显示联网提示
	 */
	protected final void onGet(String url, Map<String,Object> params,RequestListener requestListener,String dialogMessage){
		this.requestType = ConnectionRequest.RequestStatus.GET;
		this.requestListener = requestListener;
		startAsyncTask(url,params,null,dialogMessage);
	}

	/**
	 * 开始联网，post
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param requestListener 回调监听
	 * @param requestListener 回调监听
	 */
	protected void onPost(String url, Map<String,Object> params,RequestListener requestListener){
		this.requestType = ConnectionRequest.RequestStatus.POST;
		this.requestListener = requestListener;
		startAsyncTask(url,null,params,dialogMessage);
	}

	/**
	 * 开始联网，post
	 * @param url
	 * @param params
	 * @param bodyParams
	 * @param requestListener
	 */
	protected void onPost(String url, Map<String,Object> params, Map<String,Object> bodyParams,RequestListener requestListener){
		this.requestType = ConnectionRequest.RequestStatus.POST;
		this.requestListener = requestListener;
		startAsyncTask(url,params,bodyParams,dialogMessage);
	}

	/**
	 * 开始联网，post
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param requestListener 回调监听
	 * @param dialogMessage 联网提示信息，为空不显示联网提示
	 */
	protected final void onPost(String url, Map<String,Object> params,RequestListener requestListener,String dialogMessage){
		this.requestType = ConnectionRequest.RequestStatus.POST;
		this.requestListener = requestListener;
		startAsyncTask(url,null,params,dialogMessage);
	}

	/**
	 * 开始联网，head
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param requestListener 回调监听
	 * @param dialogMessage 联网提示信息，为空不显示联网提示
	 */
	protected final void onHead(String url, Map<String,Object> params,RequestListener requestListener,String dialogMessage){
		this.requestType = ConnectionRequest.RequestStatus.HEAD;
		this.requestListener = requestListener;
		startAsyncTask(url,null,params,dialogMessage);
	}

	/**
	 * 开始联网，PUT
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param requestListener 回调监听
	 * @param dialogMessage 联网提示信息，为空不显示联网提示
	 */
	protected final void onPut(String url, Map<String,Object> params,RequestListener requestListener,String dialogMessage){
		this.requestType = ConnectionRequest.RequestStatus.PUT;
		this.requestListener = requestListener;
		startAsyncTask(url,params,null,dialogMessage);
	}

	/**
	 * 开始联网，PUT
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param requestListener 回调监听
	 * @param dialogMessage 联网提示信息，为空不显示联网提示
	 */
	protected final void onPut(String url, Map<String,Object> params,Map<String,Object> bodyParams,RequestListener requestListener,String dialogMessage){
		this.requestType = ConnectionRequest.RequestStatus.PUT;
		this.requestListener = requestListener;
		startAsyncTask(url,params,bodyParams,dialogMessage);
	}

	/**
	 * 开始联网，DELETE
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param requestListener 回调监听
	 * @param dialogMessage 联网提示信息，为空不显示联网提示
	 */
	protected final void onDelete(String url, Map<String,Object> params,RequestListener requestListener,String dialogMessage){
		this.requestType = ConnectionRequest.RequestStatus.DELETE;
		this.requestListener = requestListener;
		startAsyncTask(url,null,params,dialogMessage);
	}

	/**
	 * 表单上传文件
	 * @param url 联网地址
	 * @param params 联网参数
	 * @param files 文件参数
	 * @param requestListener 回调监听
	 * @param dialogMessage 联网提示信息，为空不显示联网提示
	 */
	protected final void onPostFile(String url, Map<String,Object> params, Map<String,File> files,RequestListener requestListener,String dialogMessage){
		this.requestType = ConnectionRequest.RequestStatus.POSTFILE;
		this.requestListener = requestListener;
		this.files = files;
		startAsyncTask(url,null,params,dialogMessage);
	}

	/**
	 * 上传文件，以 byte 数组形式
	 * @param url
	 * @param params
	 * @param files
	 * @param requestListener 回调监听
	 * @param dialogMessage
	 */
	protected final void onPostBytes(String url, Map<String,Object> params, Map<String,byte[]> files,RequestListener requestListener,String dialogMessage){
		this.requestType = ConnectionRequest.RequestStatus.POSTFILEBYTE;
		this.requestListener = requestListener;
		this.fileByteArrays = files;
		startAsyncTask(url,null,params,dialogMessage);
	}

	/**
	 * 异步下载文件
	 * @param url 联网地址
	 * @param filePath 联网参数
	 * @param dialogMessage 联网提示信息，为空不显示联网提示
	 */
	protected final void onStartAsyncTaskGetFile(String url, String filePath,String dialogMessage,DownloadListener downloadListener){
		this.requestType = ConnectionRequest.RequestStatus.ASYNCDOWNLOADFILE;
		this.filePath = filePath;
		this.downloadListener = downloadListener;
		startAsyncTask(url,null,null,dialogMessage);
	}

	/**
	 * 同步下载文件
	 * @param url 联网地址
	 * @param filePath 文件保存地址
	 * @param downloadListener 下载监听
	 * @return
	 */
	public final boolean onStartSyncTaskGetFile(String url,String filePath,DownloadListener downloadListener) throws NetworkException, SdcardException {
		try {
			remoteRequester.doDownloadFile(url,filePath,currentLength,targetLength,downloadListener,getRequestHeaders());
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 取消联网请求
	 */
	public final void cancleRequest(){
		cancel = true;
		remoteRequester.cancel();
		if(requestListener != null){
			requestListener.onRequestCancel();
		}
	}

	/**
	 * 开始异步联网
	 * @param url
	 * @param params
	 * @param dialogMessage
	 */
	private void startAsyncTask(String url, Map<String,Object> params,Map<String,Object> bodyParams,String dialogMessage) {
		this.url = url;
		this.params = params;
		this.bodyParams = bodyParams;
		this.dialogMessage = dialogMessage;

		//设置联网默认加载视图
//		Context mActivity = getActivity();
//		if(mActivity != null && dialogMessage != null && dialogMessage.length() > 0){
//			isShowDialog = true;
//			if(pd != null && pd.isShowing()){
//				pd.dismiss();
//				pd = null;
//			}
//			pd = new CustomProgressDialog(mActivity, dialogMessage,isCancelDialog,new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if(pd != null && pd.isShowing()){
//						pd.dismiss();
//					}
//					cancleRequest();
//				}
//			});
//			pd.setOnCancelListener(new OnCancelListener() {
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					cancleRequest();
//				}
//			});
//		}else{
//			isShowDialog = false;
//			pd = null;
//		}

		//开启异步联网
		new AsyncTask<String,Integer,T>() {
			private Exception myException = null;

			@Override
			protected T doInBackground(String... params) {
				try {
					Object o = requestTask();
					//假如不是下载，解析返回的数据
					if(requestType != ConnectionRequest.RequestStatus.ASYNCDOWNLOADFILE){
						return parserTask((String)o);
					}
				} catch (Exception e) {
					myException = e;
				}
				return null;
			}

			protected void onPostExecute(final T t) {
				if(!cancel){
					try {
//						if(pd != null && pd.isShowing()){
//							pd.dismiss();
//						}

						if (myException==null) {
							if(requestType == ConnectionRequest.RequestStatus.ASYNCDOWNLOADFILE){
								if(downloadListener != null){
									downloadListener.onRequestSuccess();
								}
							}else{
								finishTask(t);
							}
						}else{
							failedTask(myException);
						}
					} catch (Exception e) {
						failedTask(e);
					}
				}
			};
//		}.execute();//任务顺序执行，只有一个在运行
//		}.executeOnExecutor();//可以自定义线程的运行规则
//		}.executeOnExecutor(Executors.newCachedThreadPool());//没有限制的线程池，一直开线程，最终OOM
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//最多只有5个线程同时运行,最多128个排队，超出后RejectedExecutionException
//		}.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);//保证任务执行的顺序，与execute()一样，OOM
//		}.executeOnExecutor(Executors.newFixedThreadPool(10));//同时运行10个，OOM
	}

	/**
	 * 发起联网请求
	 * @return
	 * @throws Exception
	 */
	private Object requestTask() throws Exception {
		Object o = null;
		//根据不同的类型选择不同的处理方式
		switch (requestType) {
			case GET:
				o = remoteRequester.doGet(url,params,bodyParams,getRequestHeaders());
				break;
			case PUT:
				o = remoteRequester.doPut(url,params,bodyParams,getRequestHeaders());
				break;
			case POST:
				o = remoteRequester.doPost(url,params,bodyParams,getRequestHeaders());
				break;
			case HEAD:
				o = remoteRequester.doHead(url,params,bodyParams,getRequestHeaders());
				break;
			case DELETE:
				o = remoteRequester.doDelete(url,params,bodyParams,getRequestHeaders());
				break;
			case POSTFILE:
				o = remoteRequester.doPostFile(url, bodyParams, files,getRequestHeaders());
				break;
			case POSTFILEBYTE:
				o = remoteRequester.doPostFileByte(url, bodyParams, fileByteArrays, getRequestHeaders());
				break;
			case ASYNCDOWNLOADFILE:
				o = remoteRequester.doDownloadFile(url,filePath,currentLength,targetLength,downloadListener,getRequestHeaders());
				break;
		}

		if (o instanceof ResponseInfo) {
			ResponseInfo response = (ResponseInfo) o;
			responseHeaders = response.getHeaders();
			o = response.getBody();
		}
		return o;
	}

	/**
	 * 解析数据，业务基类实现
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected abstract T parserTask(String response) throws Exception;

	/**
	 * 联网完成，业务基类实现
	 * @param t
	 * @throws Exception
	 */
	protected abstract void finishTask(T t)throws Exception;

	/**
	 * 处理联网异常等失败
	 * 如：网络连接超时，联网失败，解析错误等
	 * @param e
	 */
	private void failedTask(Exception e) {
		//获取错误信息
		int errCode = -1;
		String errorMsg = "";
		if(e instanceof NetworkException){
			NetworkException ex = (NetworkException)e;
			int errorCode = ex.getErrorCode();
			switch(errorCode){
				case NetworkException.TYPE_CLOSE:
					return;//主动取消联网返回异常，不做任何处理
				case NetworkException.TYPE_NOTNETWORK:
					errorMsg = TipStrConfig.error_no_network;
					break;
				case NetworkException.TYPE_TIMEOUT:
					errCode = RequestConfig.request_time_out;
					errorMsg = TipStrConfig.error_timeout;
					break;
				case NetworkException.TYPE_HTTPCODEERROR:
					errorMsg = TipStrConfig.error_network +"("+e.getMessage()+")";
					break;
				case NetworkException.TYPE_UNKNOWHOST:
					errorMsg = TipStrConfig.error_unknowhost;
					break;
				case NetworkException.TYPE_OTHER:
					String msg = e.getMessage();
					if(!TextUtils.isEmpty(msg)){
						errorMsg = TipStrConfig.error_unknownetwork+"\n("+e.getMessage()+")";
					}else{
						errorMsg = TipStrConfig.error_unknownetwork;
					}
					break;
			}
		} else if (e instanceof SdcardException) {
			SdcardException ex = (SdcardException) e;
			String message;
			if (ex.getErrorCode() == SdcardException.SDCARD_ERROR) {
				message = TipStrConfig.error_write_sdcard;
			} else if (ex.getErrorCode() == SdcardException.SDCARD_FULL) {
				message = TipStrConfig.error_sdcard_full;
			}else {
				message = TipStrConfig.error_sdcard_other;
			}

			errorMsg = message;
		} else if (e instanceof JSONException) {
			errorMsg = TipStrConfig.error_parser;
		}else{
			errorMsg = TipStrConfig.error_unknown+e.getMessage();
			e.printStackTrace();
		}
		Tools.log(TAG,"url="+url+",error="+errorMsg);
		//子类处理
		if(requestListener !=null){
			requestListener.onRequestFailed(errCode,errorMsg);
		}
		//下载类回调
		if(downloadListener != null){
			downloadListener.onRequestFailed(errCode,errorMsg);
		}
	}

}