package com.chinamworld.bocmbci.biz.remittance.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 账户详情返回时调用实现该接口的类的方法
 * 
 * @author Zhi
 */
public interface AccDetailListnenr {
	public void detailCallBack(List<Map<String, Object>> detailList);
}
