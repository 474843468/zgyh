package com.boc.bocsoft.mobile.bocmobile.buss.account.base;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractWebView;

/**
 * 服务须知Fragment
 * Created by liuyang on 2016/6/30
 */
public abstract class BaseServiceBureauFragment extends BaseAccountFragment implements View.OnClickListener {
    /**账户的webview*/
    private ContractWebView wbContent;

    private Button btnKnow;

    private boolean isOnlyClosed;

    public BaseServiceBureauFragment(boolean isOnlyClosed) {
        this.isOnlyClosed = isOnlyClosed;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.base_fragment_service_bureau, null);
    }

    @Override
    public void initView() {
        wbContent = (ContractWebView) mContentView.findViewById(R.id.wb_content);
        btnKnow=(Button)mContentView.findViewById(R.id.btn_know);
    }

    @Override
    public void initData() {
        wbContent.setDefaultLoadUrl(getUrl());
        wbContent.setData(getContract());
    }

    protected ContractWebView.Contract getContract() {
        return null;
    }

    protected abstract String getUrl();

    /**
     * 隐藏标题栏
     * @return
     */
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void setListener() {
        btnKnow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(isOnlyClosed)
            pop();
    }
}
