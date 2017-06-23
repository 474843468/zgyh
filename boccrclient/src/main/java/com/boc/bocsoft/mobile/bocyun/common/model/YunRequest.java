package com.boc.bocsoft.mobile.bocyun.common.model;

import java.io.Serializable;

/**
 * @author lxw
 *
 */
public class YunRequest<T> implements Serializable {



	/**
	 * @param params
	 */
	public YunRequest( T params){
		header = new YunHeader();
		this.params = params;
	}
	
	private YunHeader header;

	private T params;

	
	
	public YunHeader getHeader() {
		return header;
	}

	public void setHeader(YunHeader header) {
		this.header = header;
	}

	public T getParams() {
		return params;
	}

	public void setParams(T params) {
		this.params = params;
	}


	
}
