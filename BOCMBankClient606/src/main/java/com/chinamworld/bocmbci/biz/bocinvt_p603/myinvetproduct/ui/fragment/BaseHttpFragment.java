package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.support.v4.app.Fragment;

import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.IHttpRequestHandle;

public class BaseHttpFragment extends Fragment implements IHttpRequestHandle,
		HttpObserver {
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse resultObj) {

		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return false;
		}
		return activity.doBiihttpRequestCallBackPre(resultObj);
	}

	@Override
	public boolean doBiihttpRequestCallBackAfter(BiiResponse resultObj) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return false;
		}
		return activity.doBiihttpRequestCallBackAfter(resultObj);
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return false;
		}
		return activity.httpRequestCallBackPre(resultObj);
	}

	@Override
	public boolean httpRequestCallBackAfter(Object resultObj) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return false;
		}
		return activity.httpRequestCallBackAfter(resultObj);
	}

	@Override
	public boolean httpCodeErrorCallBackPre(String code) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return false;
		}
		return activity.httpCodeErrorCallBackPre(code);

	}

	@Override
	public boolean httpCodeErrorCallBackAfter(String code) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return false;
		}
		return activity.httpCodeErrorCallBackAfter(code);

	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return;
		}
		activity.commonHttpErrorCallBack(requestMethod);
	}

	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		BaseActivity activity = (BaseActivity) getActivity();
		if (activity == null) {
			return;
		}
		activity.commonHttpResponseNullCallBack(requestMethod);
	}
}
