package com.boc.bocsoft.remoteopenacc.buss.activity;


import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;

/**
 * 设置账户密码
 * @author lxw
 *
 */
public class RemobeOpenAccSettingPasswordFragment extends BaseFragment{

	private View mRoot;
	private WebView mWebView;
	
	@Override
	public View onCreateView(LayoutInflater inflater) {
		mRoot = inflater.inflate(R.layout.bocroa_fragment_remote_open_acc_set_password, null, false);
		mWebView = (WebView)mRoot.findViewById(R.id.wv_set_password);
		mWebView.loadUrl("file:///android_asset/www/index.html");
		return mRoot;
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskSuccess(Message result) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getMainTitleText() {
		// TODO Auto-generated method stub
		return "在线开户";
	}


	
	
}
