package com.bokecc.common.exception;

/**
 * 自定义异常基类
 * @author wangyue
 *
 */
public class CustomException extends Exception{
	
	private static final long serialVersionUID = 1L;
	public CustomException() {
		super();
	}
	public CustomException(String message) {
		super(message);
	}
	public CustomException(String message, Throwable throwable) {
		super(message, throwable);
	}
	public CustomException(Throwable throwable) {
		super(throwable);
	}
	
}
