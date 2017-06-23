package com.boc.device.key;

/**
 * 音频key异常类
 * 
 * @author lxw
 * 
 */
public class KeyCodeException extends Exception {

	private static final long serialVersionUID = 1L;

	private int errorCode;

	/**
	 * 构造方法
	 * 
	 * @param errorCode	错误码
	 * @param message	错误消息
	 *            
	 **/
	public KeyCodeException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 取得错误码
	 * @return 错误码
	 **/
	public int getErrorCode() {
		return errorCode;
	}

}
