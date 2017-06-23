package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.BaseWebView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractWebView;


/**
 * 贷款管理-中银E贷-合同信息Fragment
 * Created by xintong on 2016/6/3.
 */
public class ContractContentFragment extends BussFragment {

    private View mRoot;
    /**合同的webview*/
    private ContractWebView mWebView;
    //合同内容
    private ContractWebView.Contract contract;
    private Button bt_know;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_apply_contractcontent, null);
        return mRoot;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_contact_pagename);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
    
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
    
    @Override
    public void initView() {
        mWebView = (ContractWebView) mRoot.findViewById(R.id.contractWebView);
        bt_know=(Button)mRoot.findViewById(R.id.bt_know);
    }

    @Override
    public void initData() {
        String type=getArguments().getString("type");
        if ("合同".equals(type)){
            String content=getArguments().getString("content");
            String cbiCustName= getArguments().getString("cbiCustName");
            String cbiCerNo=getArguments().getString("cbiCerNo");
            String cbiCustAccount=getArguments().getString("cbiCustAccount");
            mWebView.setData(new ContractWebView.Contract(content,cbiCustName,cbiCerNo,cbiCustAccount));
        }else if("协议".equals(type)){
            String content=getArguments().getString("content");
            mWebView.setData(new ContractWebView.Contract(content));
        }else if("变更还款合同".equals(type)){
        	String loanType=getArguments().getString("loanType");
        	String accNo=getArguments().getString("accNo");
        	mWebView.setDefaultLoadUrl(ContractWebView.CHANGEACCCONTRACT_URL);
        	mWebView.setData(new ContractWebView.Contract(loanType, accNo));
        }
    }

    @Override
    public void setListener() {
    	bt_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

}
