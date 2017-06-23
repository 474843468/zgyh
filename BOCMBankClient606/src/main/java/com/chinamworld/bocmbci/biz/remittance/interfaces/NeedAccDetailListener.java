package com.chinamworld.bocmbci.biz.remittance.interfaces;

/**
 * Activity请求回卡详情后，需要知道哪个类需要实时获取卡详情数据，此为注册接口<br>
 * 需要实时获取卡详情的类需要实例化一个实现了AccDetailListener接口的观察者注册到实现本接口的类中，如果此类获取了卡详情，会通知观察者
 * 
 * @author Zhi
 */
public interface NeedAccDetailListener {
	public void setNeedDetailView(AccDetailListnenr needDetailView);
}
