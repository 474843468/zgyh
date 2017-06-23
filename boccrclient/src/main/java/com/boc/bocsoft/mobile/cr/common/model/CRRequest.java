package com.boc.bocsoft.mobile.cr.common.model;

import java.io.Serializable;

/**
 * @author lxw
 *
 */
public class CRRequest<T> implements Serializable{

	/**
	 */
	public CRRequest(String method){
		this(method, null);
	}
	
	/**
	 * @param params
	 */
	public CRRequest(String method, T params){
		header = new CRHeader();
		this.params = params;
		this.method=method;
	}
	
	private CRHeader header;

	private T params;
	
	private String method;
	
	
	public CRHeader getHeader() {
		return header;
	}

	public void setHeader(CRHeader header) {
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
