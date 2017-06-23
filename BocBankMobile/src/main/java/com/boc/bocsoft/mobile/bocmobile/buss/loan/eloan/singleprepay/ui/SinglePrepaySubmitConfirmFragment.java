package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.presenter.SinglePrepaySubmitConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * 贷款管理-中银E贷-单笔提前还款确认信息Fragment
 * Created by liuzc on 2016/9/2.
 */
public class SinglePrepaySubmitConfirmFragment extends BussFragment implements SinglePrepaySubmitConfirmContract.View{
    private static final String TAG = "SPrepaySubmitConfFrag";
    /**
     * view区
     */
    //确认页面
    protected ConfirmInfoView confirmInfoView;

    private SinglePrepaySubmitConfirmPresenter mPresenter = null;
    private SinglePrepaySubmitReq mSinglePrepaySubmitReq = null;

    private String availableBalance = null; //卡内可用余额
    private String charges = null; //提前还款手续费

    String currencyCode = ApplicationConst.CURRENCY_CNY; //币种

    private String conversationID = null;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
//        mRoot = mInflater.inflate(R.layout.fragment_eloan_prepay_confirminfo, null);
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }

    public void setConversationID(String value){
        conversationID = value;
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
        return getString(R.string.boc_eloan_prepayconfirmation_pagename);
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
    public void initView() {
        //初始化组件
        confirmInfoView.isShowSecurity(false);
    }


    @Override
    public void initData() {
        mPresenter = new SinglePrepaySubmitConfirmPresenter(this);
        mPresenter.setConversationId(conversationID);

        mSinglePrepaySubmitReq = (SinglePrepaySubmitReq)getArguments().getSerializable(EloanConst.ELON_PREPAY_COMMIT);
        charges = getArguments().getString(EloanConst.LOAN_PREPAY_CHARGES);
        availableBalance = getArguments().getString(EloanConst.LOAN_PREPAY_AVLAMOUNT);

        currencyCode = mSinglePrepaySubmitReq.getCurrency();

        BigDecimal bdTotalAmount = new BigDecimal(mSinglePrepaySubmitReq.getRepayAmount()).add(
                new BigDecimal(charges)); //还款总额，包含手续费
        confirmInfoView.setHeadValue(
                String.format("%s(%s)", getString(R.string.boc_eloan_prepay_total_desc2),
                        PublicCodeUtils.getCurrency(mContext, currencyCode)),
                MoneyUtils.transMoneyFormat(bdTotalAmount, currencyCode));

        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put( getString(R.string.boc_eloan_prepay_capital),
                MoneyUtils.transMoneyFormat(mSinglePrepaySubmitReq.getAdvanceRepayCapital(), currencyCode));
        datas.put(getString(R.string.boc_eloan_prepay_interest_desc2),
                MoneyUtils.transMoneyFormat(mSinglePrepaySubmitReq.getAdvanceRepayInterest(), currencyCode));
        datas.put(getString(R.string.boc_eloan_fee),
                DataUtils.getFormatCharges(MoneyUtils.transMoneyFormat(charges, currencyCode)));
        datas.put( getString(R.string.boc_eloan_prepay_repaymentAccount),
                NumberUtils.formatCardNumber(mSinglePrepaySubmitReq.getPayAccount()));
        confirmInfoView.addData(datas, false, false);
    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){
            @Override
            public void onClickConfirm() {
                showLoadingDialog(false);
                mPresenter.prepaySubmit(mSinglePrepaySubmitReq);
            }

            @Override
            public void onClickChange() {

            }
        });

    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }


    @Override
    public void singlePrepaySubmitSuccess(SinglePrepaySubmitRes result) {
        Log.i(TAG, "提前还款交易成功！");
        closeProgressDialog();
        SinglePrepaySubmitResultFragment resultsFragment = new SinglePrepaySubmitResultFragment();
        resultsFragment.setStatus(OperationResultHead.Status.SUCCESS);
        Bundle bundle=new Bundle();
        bundle.putSerializable(EloanConst.ELON_PREPAY_COMMIT,result);
        bundle.putString(EloanConst.LOAN_PREPAY_CHARGES, charges);
        bundle.putString(EloanConst.LOAN_PREPAY_AVLAMOUNT, availableBalance);
        bundle.putString(EloanConst.LOAN_PREPAY_CURRENCYCODE, currencyCode);
        resultsFragment.setArguments(bundle);
        start(resultsFragment);
    }

    @Override
    public void singlePrepaySubmitFail(ErrorException e) {
        closeProgressDialog();
//        SinglePrepaySubmitResultFragment resultsFragment = new SinglePrepaySubmitResultFragment();
//        resultsFragment.setStatus(OperationResultHead.Status.FAIL);
//        start(resultsFragment);
    }

    @Override
    public void setPresenter(SinglePrepaySubmitConfirmContract.Presenter presenter) {

    }
}
