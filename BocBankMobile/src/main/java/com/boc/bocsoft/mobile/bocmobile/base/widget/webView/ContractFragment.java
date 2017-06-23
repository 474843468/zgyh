package com.boc.bocsoft.mobile.bocmobile.base.widget.webView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;

/**
 * 作者：XieDu
 * 创建时间：2016/9/13 13:58
 * 描述：下边为我知道了的合同页面
 */
public class ContractFragment extends BussFragment {


    protected View rootView;
    protected Button btnKnow;
    protected BaseContractWebView mWebView;
    /** 合同的webview */
    //合同内容
    private BaseContractWebView.Contract contract;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_contract, null);
        return rootView;
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initView() {
        btnKnow = (Button) rootView.findViewById(R.id.btn_know);
        mWebView = (BaseContractWebView) rootView.findViewById(R.id.webview_contract);
    }

    public static ContractFragment newInstance(String url) {
        return newInstance(url,null);
    }

    public static ContractFragment newInstance(String url, Object content) {
        String jsonContent = GsonUtils.getGson().toJson(content);
        ContractFragment contractFragment = new ContractFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("content", jsonContent);
        contractFragment.setArguments(bundle);
        return contractFragment;
    }

    @Override
    public void initData() {

        String url = getArguments().getString("url");
        boolean loadWithOverviewMode = getArguments().getBoolean("loadWithOverviewMode", false);
        String content = getArguments().getString("content");
        contract = new BaseContractWebView.Contract(content);

        if (loadWithOverviewMode)
        mWebView.getWebView().getSettings().setLoadWithOverviewMode(true);

        mWebView.setInfoProxy(contract);
        mWebView.loadUrl(url);
    }

    @Override
    public void setListener() {
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    public void setUrl(String url) {
        mWebView.setDefaultLoadUrl(url);
    }
}
