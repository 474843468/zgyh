package com.boc.bocsoft.mobile.mbcg;

import java.io.Serializable;

/**
 * @author lxw
 *
 */
public class MBCGRequest<T> implements Serializable {


	/**
	 * @param params
	 */
	public MBCGRequest( T params){
		header = new MBCGHeader();
		this.params = params;
	}


	private String method;

	private MBCGHeader header;

	private T params;

	public void setMethod(String method) {
		this.method = method;
	}

	public MBCGHeader getHeader() {
		return header;
	}

	public void setHeader(MBCGHeader header) {
		this.header = header;
	}

	public T getParams() {
		return params;
	}

	public void setParams(T params) {
		this.params = params;
	}

}
