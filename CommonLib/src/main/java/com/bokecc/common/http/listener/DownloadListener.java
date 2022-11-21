package com.bokecc.common.http.listener;

/**
 * 下载请求监听者
 * 
 * RequestListener
 */
public interface DownloadListener{

	/**
	 * 文件上传或下载百分比
	 * @param totalSize 总大小
	 * @param currentSize 当前大小
     */
	void uploadProgress(long totalSize,long currentSize);

	/**
	 * 成功的回调
	 */
	void onRequestSuccess();

	/**
	 * 失败的回调
	 * @param code
	 * @param errorMsg
	 */
	void onRequestFailed(int code,String errorMsg);

}
