package com.boc.bocsoft.mobile.cr.common.model;

import java.io.Serializable;

/**
 *
 * @author lxw
 *
 */
public class CRResponse<T> implements Serializable{
	private CRHeader header;


	//@JsonAdapter(JsonElementAdapter.class)
	private T result;

	private boolean _isException_;

	/**
	 * 错误信息描述
	 */
	private String message;
	/**
	 * 异常类型
	 */
	private String type;
	/**
	 * 错误码
	 */
	private String code;

	protected CRHeader getHeader() {
		return header;
	}

	protected void setHeader(CRHeader header) {
		this.header = header;
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

	public boolean get_isException_() {
		return _isException_;
	}

	public void set_isException_(boolean _isException_) {
		this._isException_ = _isException_;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "CRResponse{" +
				"header=" + header +
				", result=" + result +
				", _isException_=" + _isException_ +
				", message='" + message + '\'' +
				", type='" + type + '\'' +
				", code='" + code + '\'' +
				'}';
	}
}
