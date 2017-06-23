package com.chinamworld.bocmbci.biz.epay;

import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.http.HttpObserver;

public abstract class EpayBaseDialog implements HttpObserver {
	protected BaseActivity context;

	public EpayBaseDialog(BaseActivity context) {
		this.context = context;
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		return context.httpRequestCallBackPre(resultObj);
	}

	@Override
	public boolean httpRequestCallBackAfter(Object resultObj) {
		return context.httpRequestCallBackAfter(resultObj);
	}

	@Override
	public boolean httpCodeErrorCallBackPre(String code) {
		return context.httpCodeErrorCallBackPre(code);
	}

	@Override
	public boolean httpCodeErrorCallBackAfter(String code) {
		return context.httpCodeErrorCallBackAfter(code);
	}

	@Override
	public void commonHttpErrorCallBack(String code) {
		context.commonHttpErrorCallBack(code);
	}

	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		context.commonHttpResponseNullCallBack(requestMethod);
	}

}
