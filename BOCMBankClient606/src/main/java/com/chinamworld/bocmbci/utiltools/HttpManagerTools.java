package com.chinamworld.bocmbci.utiltools;

import java.util.List;

import com.chinamworld.bocmbci.abstracttools.BaseHttpManager;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

public class HttpManagerTools  extends BaseHttpManager{

	public HttpManagerTools()
	{
		Instance = this;
	}
	@Override
	public void requestBii(BiiRequestBody biiRequest,
			HttpObserver callbackObject, String successCallBackMethod) {
		HttpManager.requestBii(biiRequest, callbackObject, successCallBackMethod);
	}

	@Override
	public void requestOutlayBii(BiiRequestBody biiRequest,
			HttpObserver callbackObject, String successCallBackMethod) {
		HttpManager.requestOutlayBii(biiRequest, callbackObject, successCallBackMethod);
	}

	@Override
	public void requestBii(List<BiiRequestBody> biiRequestBodyList,
			HttpObserver callbackObject, String successCallBackMethod) {
		HttpManager.requestBii(biiRequestBodyList, callbackObject, successCallBackMethod);
		
	}

	@Override
	public boolean getcanGoBack() {
		// TODO Auto-generated method stub
		return BaseHttpEngine.canGoBack;
	}
	@Override
	public void setCanGoBack(boolean canGoBack) {
		// TODO Auto-generated method stub
		BaseHttpEngine.canGoBack=canGoBack;
	}

}
