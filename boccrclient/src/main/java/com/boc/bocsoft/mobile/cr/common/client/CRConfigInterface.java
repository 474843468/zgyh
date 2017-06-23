package com.boc.bocsoft.mobile.cr.common.client;


import com.boc.bocsoft.mobile.common.client.model.RequestParams;

public interface CRConfigInterface {

	/**
	 * 获取新接口bii地址
	 * @return 新接口bii地址
	 */
	String getUrl();

	/**
	 * 获取通用参数
	 * @return 通用参数
	 */
	RequestParams getCommonParams();


	boolean isDemo();
}
