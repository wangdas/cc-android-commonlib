package com.bokecc.common.exception;

/**
 * 自定义sdcard的一些错误
 * @author wangyue
 *
 */
public class SdcardException extends CustomException{

	private static final long serialVersionUID = 1L;
	
	public static final int SDCARD_ERROR = 1;
	public static final int OTHE＿ERROR = 2;
	public static final int SDCARD_FULL = 3;
	
	private int errorCode;

	public SdcardException(int errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
