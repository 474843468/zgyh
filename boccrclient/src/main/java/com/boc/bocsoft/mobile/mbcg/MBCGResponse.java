package com.boc.bocsoft.mobile.mbcg;

import com.boc.bocsoft.mobile.bocyun.common.model.YunHeader;
import java.io.Serializable;

/**
 *
 * @author dingeryue
 *
 */
public class MBCGResponse<T> implements Serializable {

	private String code;
	private String type;

	private String _isException_;

	private YunHeader header;

	//@JsonAdapter(JsonElementAdapter.class)
	private T result;

	public boolean isException() {
		return "true".equalsIgnoreCase(_isException_);
	}


	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public YunHeader getHeader() {
		return header;
	}

	@Override public String toString() {
		return "MBCGResponse{" +
				"code='" + code + '\'' +
				", type='" + type + '\'' +
				", _isException_='" + _isException_ + '\'' +
				", header=" + header +
				", result=" + result +
				'}';
	}
}
