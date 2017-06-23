package com.chinamworld.bocmbci.biz.remittance.interfaces;

/**
 * 模板管理 发起汇款按钮监听器
 * 
 * @author Zhi
 */
public interface OnStartRemittanceListener {
	/**
	 * 发起汇款
	 * 
	 * @param position
	 *            用第position项模板发起汇款
	 */
	public void onStartRemittance(int position);
}
