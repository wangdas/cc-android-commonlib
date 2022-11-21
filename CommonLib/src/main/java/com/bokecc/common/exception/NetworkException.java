package com.bokecc.common.exception;

/**
 * 自定义网络错误
 * @author wangyue
 *
 */
public class NetworkException extends CustomException{

	private static final long serialVersionUID = 1L;

	public static final int TYPE_NOTNETWORK = 1;
	public static final int TYPE_CLOSE = 2;
	public static final int TYPE_TIMEOUT = 3;
	public static final int TYPE_HTTPCODEERROR = 4;
	public static final int TYPE_OTHER = 5;
	public static final int TYPE_UNKNOWHOST = 6;

	private int errorCode;

	public NetworkException(int errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public NetworkException(String errorMsg, int errorCode) {
		super(errorMsg);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
