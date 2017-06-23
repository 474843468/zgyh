package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.draw.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;

/**
 * Created by qiangchen on 2016/8/13.
 */
public class ReDrawOperatingResultsFragment extends BussFragment
        implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    private View mRoot;
    //结果页
    private BaseOperationResultView borvResult;
    private OperationResultHead.Status status;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_draw_operatingresults, null);
        return mRoot;
    }

    public void setStatus(OperationResultHead.Status status) {
        this.status = status;
    }

    @Override
    protected void titleLeftIconClick() {
//        super.titleLeftIconClick();
        popTo(LoanManagerFragment.class,false);
//    	start(new EloanStatusFragment());
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_draw_operatingresults_pagename);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        borvResult.updateHead(status,  "用款申请已提交！");

        borvResult.addDetailRow("用款金额", "5000");
        borvResult.addDetailRow("收款账户","6217******7654");
        borvResult.addDetailRow("期限 /利率","3个月/4.32%");
        borvResult.addDetailRow("还款账户", "6217******4567");
    }

    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void onHomeBack() {
       // popTo(EloanStatusFragment.class,false);
        ModuleActivityDispatcher.popToHomePage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClickListener(View v) {
    }

//    @Override
//    public boolean onBack() {
//        mActivity.finish();
//        return super.onBack();
//    }
}
