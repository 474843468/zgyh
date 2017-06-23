package com.chinamworld.bocmbci.biz.remittance.interfaces;

/**
 * IBANK账号校验结果监听
 * 
 * @author Zhi
 */
public interface OnIbanFormatCheckListener {
	/**
	 * 校验结果回调方法
	 * 
	 * @param isPass
	 *            校验是否通过
	 */
	public void onIbanFormatCheck(boolean isPass);
}
