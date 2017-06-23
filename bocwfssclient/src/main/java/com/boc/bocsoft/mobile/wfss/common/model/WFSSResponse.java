package com.boc.bocsoft.mobile.wfss.common.model;

import java.io.Serializable;

/**
 *
 * @author lxw
 *
 */
public class WFSSResponse<T> implements Serializable{

	private WFSSResponseHeader head;

	private T body;


	/**
	 * 异常类型
	 */
	private String type;


	public WFSSResponseHeader getHead() {
		return head;
	}

	protected void setHead(WFSSResponseHeader head) {
		this.head = head;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	@Override
	public String toString() {
		return "CRResponse{" +
				"head=" + head +
				", body=" + body +
				", type='" + type + '\'' +
				'}';
	}
}
