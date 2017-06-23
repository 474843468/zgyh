package com.boc.bocsoft.mobile.wfss.common.client;


import com.boc.bocsoft.mobile.common.client.model.RequestParams;

public interface WFSSConfigInterface {

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
