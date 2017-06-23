package com.chinamworld.bocmbci.http;

import java.io.File;
import java.util.Map;

import android.graphics.Bitmap;

import com.chinamworld.bocmbci.bii.BiiResponse;

public class BaseHttpObserver  implements HttpObserver{

	/**
	 * 回调前拦截，主要是统一拦截错误信息给出提示
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		boolean stopFlag = false;
		if (resultObj instanceof BiiResponse) {
			// Bii请求前拦截
			stopFlag = doBiihttpRequestCallBackPre((BiiResponse) resultObj);
		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return stopFlag;
	}
	 

	/**
	 * 子类覆写
	 * @param resultObj
	 * @return
	 */
	public boolean doBiihttpRequestCallBackPre(BiiResponse resultObj) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean httpRequestCallBackAfter(Object resultObj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean httpCodeErrorCallBackPre(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean httpCodeErrorCallBackAfter(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		// TODO Auto-generated method stub
		
	}

}
