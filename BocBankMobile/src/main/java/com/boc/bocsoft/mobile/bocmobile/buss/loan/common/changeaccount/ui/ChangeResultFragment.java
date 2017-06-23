package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * Created by liuzc on 2016/8/24.
 */
public class ChangeResultFragment extends BussFragment
        implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    private View mRoot;
    private OperationResultHead.Status status;
    //结果页
    private BaseOperationResultView borvResult;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_change_results, null);
        return mRoot;
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);
    }

    public void setStatus(OperationResultHead.Status status) {
        this.status = status;
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
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initData() {
        borvResult.updateHead(status, getString(R.string.boc_loan_change_account_applied), getString(R.string.boc_loan_change_account_res_tips));

        String oldAccount=getArguments().getString("oldAccount");
        String newAccount=getArguments().getString("newAccount");
        borvResult.addDetailRow(getResources().getString(R.string.boc_loan_current_repayment_account),
                NumberUtils.formatCardNumberStrong(oldAccount));
        borvResult.addDetailRow(getResources().getString(R.string.boc_loan_account_changed),
                NumberUtils.formatCardNumberStrong(newAccount));
    }

    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

    @Override
    protected void titleLeftIconClick() {
        onClickBack();
    }

    @Override
    public boolean onBack() {
        onClickBack();
        return false;
    }

    /**
     * 返回事件处理
     */
    private void onClickBack(){
        if(getFragmentManager().findFragmentByTag(LoanManagerFragment.class.getName()) != null){
            popToAndReInit(LoanManagerFragment.class);
        }
        else{
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClickListener(View v) {

    }
}
