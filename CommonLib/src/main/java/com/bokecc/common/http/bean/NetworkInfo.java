package com.bokecc.common.http.bean;

/**
 * 网络信息类
 * 
 * @author wangyue
 */
public class NetworkInfo {
	
	/**是否可以联网*/
	private boolean isConnectToNetwork = true;
	/**是否为移动网络*/
	private boolean isMobileNetwork = false;
	/**接入点名称*/
	private String proxyName = "";
	
	/**
	 * 是否可以联网
	 * @return true：可以联网，false 不可以
	 */
	public boolean isConnectToNetwork() {
		return isConnectToNetwork;
	}
	
	/**
	 * 设置是否可以联网
	 * @param isConnectToNetwork 是否可以联网（true：可以联网，false 不可以）
	 */
	public void setConnectToNetwork(boolean isConnectToNetwork) {
		this.isConnectToNetwork = isConnectToNetwork;
	}
	
	/**
	 * 是否需要代理
	 * @return true：需要，false：不需要
	 */
	public boolean isMobileNetwork() {
		return isMobileNetwork;
	}
	
	/**
	 * 设置是否需要代理
	 * @param isProxy 是否需要代理 （true：需要，false：不需要）
	 */
	public void setMobileNetwork(boolean isProxy) {
		this.isMobileNetwork = isProxy;
	}

	/**
	 * 获取接入点名称
	 * @return 接入点名称
	 */
	public String getProxyName() {
		return proxyName;
	}

	/**
	 * 设置接入点名称
	 * @param proxyName 接入点名称
	 */
	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

}
