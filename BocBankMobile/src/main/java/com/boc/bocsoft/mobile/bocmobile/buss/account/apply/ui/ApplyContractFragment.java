package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractWebView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;

/**
 * @author wangyang
 *         16/8/2 09:37
 *         申请定期合同界面
 */
public class ApplyContractFragment extends BaseAccountFragment{

    //webview
    private ContractWebView wvContract;

    //合同内容
    private ContractWebView.Contract contract;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_account_apply_contract, null);
    }

    @Override
    public void initView() {
        wvContract = (ContractWebView) mContentView.findViewById(R.id.wv_contract);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_contact_pagename);
    }
}
