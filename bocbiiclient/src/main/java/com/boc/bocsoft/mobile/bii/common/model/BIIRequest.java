package com.boc.bocsoft.mobile.bii.common.model;

import java.io.Serializable;

/**
 * @author lxw
 *
 */
public class BIIRequest<T> implements Serializable{

	/**
	 */
	public BIIRequest(String method){
		this(method, null);
	}
	
	/**
	 * @param params
	 */
	public BIIRequest(String method, T params){
		header = new BIIHeader();
		this.params = params;
		this.method=method;
	}
	
	private BIIHeader header;

	private T params;
	
	private String method;
	
	
	public BIIHeader getHeader() {
		return header;
	}

	public void setHeader(BIIHeader header) {
		this.header = header;
	}

	public T getParams() {
		return params;
	}

	public void setParams(T params) {
		this.params = params;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	
}
