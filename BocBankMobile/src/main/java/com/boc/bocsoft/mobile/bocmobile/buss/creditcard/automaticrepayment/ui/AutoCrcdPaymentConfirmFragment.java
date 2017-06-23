package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.model.AutoCrcdPayModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.presenter.AutoCrcdPaymentContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.presenter.AutoCrcdPaymentPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * Name: liukai
 * Time：2016/11/25 17:03.
 * Created by lk7066 on 2016/11/25.
 * It's used to 信用卡自动还款信息确认页面
 */

public class AutoCrcdPaymentConfirmFragment extends MvpBussFragment<AutoCrcdPaymentContract.AutoCrcdPaymentPresenter> implements AutoCrcdPaymentContract.AutoCrcdPaymentView{

    private ConfirmInfoView autoPayConfirmInfoView;
    private AutoCrcdPayModel autoCrcdPayModel;
    private Bundle bundle;
    private AutoCrcdPaymentResultFragment autoCrcdPaymentResultFragment;
    private int fragmentStatus;
    private AutoCrcdPaymentContract.AutoCrcdPaymentPresenter mPresenter;
    private View mRootView = null;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        autoCrcdPayModel = new AutoCrcdPayModel();
        autoCrcdPayModel = getArguments().getParcelable("AutoPayConfirm");
        fragmentStatus = getArguments().getInt("AutoStatus");
        mRootView = mInflater.inflate(R.layout.fragment_autopayment_confirm, null);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_autopay_confirm_title);
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
    public void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void initView() {
        autoPayConfirmInfoView = (ConfirmInfoView) mRootView.findViewById(R.id.autopay_confirm_view_title);

        autoPayConfirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){

            @Override
            public void onClickConfirm() {
                bundle = new Bundle();
                autoCrcdPaymentResultFragment = new AutoCrcdPaymentResultFragment();
                submitData();
                getPresenter().setCrcdPaymentWay(autoCrcdPayModel);
                showLoadingDialog(false);
            }

            @Override
            public void onClickChange() {

            }

        });

    }

    /**
     * 确认页面
     * 数据根据状态写入上送字段值
     * */
    public void submitData(){
        if(1 == autoCrcdPayModel.getmCrcdStatus() && 0 != fragmentStatus){//已开通需要取消关闭
            autoCrcdPayModel.setRepayType("0");
            autoCrcdPayModel.setAutoRepayMode("-1");
            autoCrcdPayModel.setRepayCurSel("-1");
        } else if(0 == autoCrcdPayModel.getmCrcdStatus() && 1 == fragmentStatus){//未开通需要开通
            autoCrcdPayModel.setRepayType("1");

            if(1 == autoCrcdPayModel.getmPaymentModeStatus()){
                autoCrcdPayModel.setAutoRepayMode("FULL");
            } else {
                autoCrcdPayModel.setAutoRepayMode("MINP");
            }

            if(0 == autoCrcdPayModel.getmPaymentWay()){
                autoCrcdPayModel.setRepayCurSel("0");
                autoCrcdPayModel.setRepayAcctId(autoCrcdPayModel.getLocalAccountId());
            } else if(1 == autoCrcdPayModel.getmPaymentWay()) {
                autoCrcdPayModel.setRepayCurSel("1");
                autoCrcdPayModel.setRepayAcctId(autoCrcdPayModel.getLocalAccountId());
                autoCrcdPayModel.setForeignRepayAcctId(autoCrcdPayModel.getForeignAccountId());
            } else {
                autoCrcdPayModel.setRepayCurSel("2");
                autoCrcdPayModel.setSignForeignCurrencyAcctId(autoCrcdPayModel.getLocalAccountId());
            }
        } else if(1 == autoCrcdPayModel.getmCrcdStatus() && 0 == fragmentStatus){//已开通需要修改
            autoCrcdPayModel.setRepayType("1");

            if(1 == autoCrcdPayModel.getmPaymentModeStatus()){
                autoCrcdPayModel.setAutoRepayMode("FULL");
            } else {
                autoCrcdPayModel.setAutoRepayMode("MINP");
            }

            if(0 == autoCrcdPayModel.getmPaymentWay()){
                autoCrcdPayModel.setRepayCurSel("0");
                autoCrcdPayModel.setRepayAcctId(autoCrcdPayModel.getLocalAccountId());
            } else if(1 == autoCrcdPayModel.getmPaymentWay()) {
                autoCrcdPayModel.setRepayCurSel("1");
                autoCrcdPayModel.setRepayAcctId(autoCrcdPayModel.getLocalAccountId());
                autoCrcdPayModel.setForeignRepayAcctId(autoCrcdPayModel.getForeignAccountId());
            } else {
                autoCrcdPayModel.setRepayCurSel("2");
                autoCrcdPayModel.setSignForeignCurrencyAcctId(autoCrcdPayModel.getLocalAccountId());
            }

        }

    }

    @Override
    public void initData() {
        mPresenter = new AutoCrcdPaymentPresenter(this);

        if(0 == autoCrcdPayModel.getmCrcdStatus()){
            autoPayConfirmInfoView.updateButton(getResources().getString(R.string.boc_crcd_autopay_open));
        } else {
            autoPayConfirmInfoView.updateButton(getResources().getString(R.string.boc_crcd_autopay_confirmrighttext));
        }

        autoPayConfirmInfoView.isShowSecurity(false);
        LinkedHashMap<String, String> nameAndVelue=new LinkedHashMap<>();
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_crcdnumber), NumberUtils.formatCardNumber(autoCrcdPayModel.getAccountNumber()));

        if(1 == autoCrcdPayModel.getmPaymentModeStatus()){
            nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_paymentamount), getResources().getString(R.string.boc_crcd_autopay_full));
        } else {
            nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_paymentamount), getResources().getString(R.string.boc_crcd_autopay_minp));
        }

        if(1 == autoCrcdPayModel.getmCurrencyStatus()){
        } else {
            if(0 == autoCrcdPayModel.getmPaymentWay()){
                nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_paymentway), getResources().getString(R.string.boc_crcd_autopay_rmbrepay));
            } else if(1 == autoCrcdPayModel.getmPaymentWay()) {
                nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_paymentway), getResources().getString(R.string.boc_crcd_autopay_bothrepay));
            } else {
                nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_paymentway), getResources().getString(R.string.boc_crcd_autopay_foreignrepay));
            }
        }

        if(1 == autoCrcdPayModel.getmPaymentWay()){
            nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_paymentrmbaccount), NumberUtils.formatCardNumber(autoCrcdPayModel.getLocalCurrencyPaymentAccountNo()));
            nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_paymentforeignaccount), NumberUtils.formatCardNumber(autoCrcdPayModel.getForeignCurrencyAccountNo()));
        } else {
            nameAndVelue.put(getResources().getString(R.string.boc_crcd_autopay_paymentaccount), NumberUtils.formatCardNumber(autoCrcdPayModel.getLocalCurrencyPaymentAccountNo()));
        }

        autoPayConfirmInfoView.addData(nameAndVelue, true, true);

    }

    @Override
    protected AutoCrcdPaymentContract.AutoCrcdPaymentPresenter initPresenter() {
        return new AutoCrcdPaymentPresenter(this);
    }

    @Override
    public void setPresenter(AutoCrcdPaymentContract.AutoCrcdPaymentPresenter presenter) {

    }

    @Override
    public void crcdPaymentWaySuccess(PsnCrcdQueryCrcdPaymentWayResult mPaymentWayResult){

    }

    @Override
    public void crcdPaymentWayFailed(BiiResultErrorException exception){

    }

    @Override
    public void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQueryResult){

    }

    @Override
    public void crcdCurrencyQueryFailed(BiiResultErrorException exception){

    }

    @Override
    public void setCrcdPaymentWaySuccess(PsnCrcdPaymentWaySetupResult mPaymentWaySetResult){
        bundle.putParcelable("AutoPayResult", autoCrcdPayModel);
        if(0 == fragmentStatus){//修改
            bundle.putInt("ResultStatus", 2);
        } else {//开通
            bundle.putInt("ResultStatus", 0);
        }
        autoCrcdPaymentResultFragment.setArguments(bundle);
        closeProgressDialog();
        start(autoCrcdPaymentResultFragment);
    }

    @Override
    public void setCrcdPaymentWayFailed(BiiResultErrorException exception){

    }

}
