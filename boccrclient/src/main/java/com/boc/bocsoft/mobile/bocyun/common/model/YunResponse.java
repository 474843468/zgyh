package com.boc.bocsoft.mobile.bocyun.common.model;

import java.io.Serializable;

/**
 *
 * @author dingeryue
 *
 */
public class YunResponse<T> implements Serializable {

	private static final String YUN_RESPONE_ERROR_CODE = "00000000";

	private String returnCode;
	private String returnMessage;

	private YunHeader header;

	//@JsonAdapter(JsonElementAdapter.class)
	private T result;

	public boolean isException() {
		return !YUN_RESPONE_ERROR_CODE.equals(returnCode);
	}


	//	public JsonElement getResult() {
//		return result;
//	}
//
//	public void setResult(JsonElement result) {
//		this.result = result;
//	}


	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public YunHeader getHeader() {
		return header;
	}

	@Override public String toString() {
		return "YunResponse{" +
				"returnCode='" + returnCode + '\'' +
				", returnMessage='" + returnMessage + '\'' +
				", header=" + header +
				", result=" + result +
				'}';
	}
}
