package com.chinamworld.bocmbci.biz.remittance.interfaces;

/**
 * 选择扣款账户之后要通知其他View做出改变，需要根据扣款账户的改变作出响应的页面或Fragment需要实现此接口
 * 
 * @author Zhi
 */
public interface AccChangeListener {
	/**
	 * 扣款账户发生改变时调用该方法
	 * 
	 * @param accountId
	 *            选择的accountId
	 */
	public void accChange(String accountId);
}
