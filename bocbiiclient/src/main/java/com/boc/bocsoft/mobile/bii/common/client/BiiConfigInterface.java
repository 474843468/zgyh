package com.boc.bocsoft.mobile.bii.common.client;


import com.boc.bocsoft.mobile.common.client.model.RequestParams;

public interface BiiConfigInterface {

	/**
	 * 获取新接口bii地址
	 * @return 新接口bii地址
	 */
	String getBiiUrl();


	/**
	 * 获取验证码地址
	 * @return 验证码地址
	 */
	String getVaryficationCodeUrl();
	
	/**
	 * 获取版本更新地址
	 * @return 版本更新地址
	 */
	String getMbcmUrl();

	/**
	 * 获取通用参数
	 * @return 通用参数
	 */
	RequestParams getCommonParams();


	boolean isDemo();

	/**
	 * 中银理财
	 * @return
     */
	String getBMPSUrl();
}
