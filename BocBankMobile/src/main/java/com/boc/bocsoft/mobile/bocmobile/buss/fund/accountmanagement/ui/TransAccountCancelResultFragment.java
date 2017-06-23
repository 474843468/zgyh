package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundProductHomeFragment;

/**
 * 基金-账户管理-交易账户注销结果页
 * Created by lyf7084 on 2016/12/07.
 */

public class TransAccountCancelResultFragment extends BussFragment {

    private BaseResultView resultView;    // 继承BaseResultView
    private View rootView;    // 交易账户注销结果页View

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_trans_account_cancel_result, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        resultView = (BaseResultView) rootView.findViewById(R.id.transAccountCancel);
    }

    @Override
    public void initData() {
        resultView.addStatus(ResultHead.Status.SUCCESS, getResources().getString(R.string.boc_fund_cancel_success));
    }

    @Override
    public void setListener() {
        resultView.setOnHomeBackClick(new BaseResultView.HomeBackListener() {
            @Override
            public void onHomeBack() {
                ModuleActivityDispatcher.popToHomePage();
            }
        });
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(FundProductHomeFragment.class);
    }

    @Override
    public boolean onBack() {
        popToAndReInit(FundProductHomeFragment.class);
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_fragment_result_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

}