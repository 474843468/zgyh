package com.boc.bocsoft.remoteopenacc.buss.activity;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;

/**
 * 远程开户协议
 * @author lxw
 *
 */
public class RemobeContractFragment extends BaseFragment{


	private WebView mWebView;
	private View mRoot;
	
	@Override
	protected View onCreateView(LayoutInflater mInflater) {
		mRoot = mInflater.inflate(R.layout.bocroa_fragment_contract, null);
		return mRoot;
	}

	@Override
	protected void initView() {
		mWebView = (WebView)mRoot.findViewById(R.id.contract_webview);
		mWebView.loadUrl("file:///android_asset/bocroa_www/contract.html");
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
	public String getMainTitleText() {
		
		return "个人电子账户服务协议";
	}

	@Override
	public void onTaskSuccess(Message result) {
		
		
	}

}
