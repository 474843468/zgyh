package com.chinamworld.bocmbci.biz.remittance.interfaces;

/**
 * 模板管理 其他操作监听器
 * 
 * @author Zhi
 */
public interface OnOtherOperationListener {
	/**
	 * 其他操作
	 * 
	 * @param position
	 *            点击的是第position项的"其他操作"按钮
	 */
	public void onOtherOperation(int position);
}
