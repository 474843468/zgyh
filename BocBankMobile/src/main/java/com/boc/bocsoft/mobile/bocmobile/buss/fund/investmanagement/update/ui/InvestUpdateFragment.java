package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui.InvestFragmentView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.ui.RedeemFragmentView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestBuyDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestSellDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;

/**
 * Created by huixiaobo on 2016/12/2.
 * 定投修改首页
 */
public class InvestUpdateFragment extends BussFragment {
    /**交易类型*/
    private String mTransType;
    /**定申修改*/
    private static final String INVEST_TYPE = "0";
    /**定赎修改*/
    private static final String REDEEM_TYPE = "1";
    /**有效定申传值对象*/
    private ValidinvestModel.ListBean mValidData;
    /**定投详情对象*/
    private InvestBuyDetailModel mBuyDetail;
    /**定赎详情对象*/
    private InvestSellDetailModel mSellDetail;
    /**定申显示view*/
    private InvestFragmentView investView;
    /**定赎显示view*/
    private RedeemFragmentView redeemFragmentView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        initData();

        if (INVEST_TYPE.equals(mTransType)) {
            investView = new InvestFragmentView(getContext());
            investView.setInvestBuyData(mValidData);
            investView.attach(this);
            return investView;
        } else if (REDEEM_TYPE.equals(mTransType)) {
            redeemFragmentView = new RedeemFragmentView(getContext());
            redeemFragmentView.setRedeemData(mValidData);
            redeemFragmentView.attach(this);
            return redeemFragmentView;
        }
        return null;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mTransType = getArguments().getString(InvestConst.TRANS_TYPE);
        mValidData = (ValidinvestModel.ListBean) getArguments().getSerializable(InvestConst.VALID_DETAIL);
    }

    @Override
    public void setListener() {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "修改";
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onDestroy() {
        if (investView != null) {
            investView.onDestroy();
        } else if(redeemFragmentView != null) {
            redeemFragmentView.onDestroy();
        }
        super.onDestroy();
    }
}
