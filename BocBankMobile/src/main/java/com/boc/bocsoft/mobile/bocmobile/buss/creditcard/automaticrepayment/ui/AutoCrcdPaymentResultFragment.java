package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.model.AutoCrcdPayModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.presenter.AutoCrcdPaymentContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.presenter.AutoCrcdPaymentPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui.CrcdDetailFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * Name: liukai
 * Time：2016/11/25 17:12.
 * Created by lk7066 on 2016/11/25.
 * It's used to 信用卡自动还款结果页面
 */

public class AutoCrcdPaymentResultFragment extends MvpBussFragment<AutoCrcdPaymentContract.AutoCrcdPaymentPresenter> implements AutoCrcdPaymentContract.AutoCrcdPaymentView, BaseResultView.HomeBackListener{

    private BaseResultView resultview;
    private AutoCrcdPayModel autoCrcdPayModel;
    private int resultStatus;
    private View rootView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        autoCrcdPayModel = new AutoCrcdPayModel();
        autoCrcdPayModel = getArguments().getParcelable("AutoPayResult");
        resultStatus = getArguments().getInt("ResultStatus");
        rootView = mInflater.from(mContext).inflate(R.layout.fragment_autopayment_result, null);
        return rootView;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    public void initView() {
        super.initView();
        resultview = (BaseResultView) rootView.findViewById(R.id.autopay_result_view);
        resultview.setOnHomeBackClick(this);
    }

    @Override
    public void initData() {
        if(1 != resultStatus){
            if(2 == resultStatus){//修改
                resultview.addStatus(ResultHead.Status.SUCCESS, getResources().getString(R.string.boc_crcd_autopay_result_change));
            } else {
                resultview.addStatus(ResultHead.Status.SUCCESS, getResources().getString(R.string.boc_crcd_autopay_result_open));
            }

            resultview.addTitle(getResources().getString(R.string.boc_crcd_autopay_result_open_addtitle));

            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put(getResources().getString(R.string.boc_crcd_autopay_crcdnumber), NumberUtils.formatCardNumber(autoCrcdPayModel.getAccountNumber()));
            map.put(getResources().getString(R.string.boc_crcd_autopay_paymentamount), queryPaymentMode());
            if(1 != autoCrcdPayModel.getmCurrencyStatus()){
                map.put(getResources().getString(R.string.boc_crcd_autopay_paymentway), queryPaymentWay());
            }
            if(1 == autoCrcdPayModel.getmPaymentWay()){
                map.put(getResources().getString(R.string.boc_crcd_autopay_paymentrmbaccount), NumberUtils.formatCardNumber(autoCrcdPayModel.getLocalCurrencyPaymentAccountNo()));
                map.put(getResources().getString(R.string.boc_crcd_autopay_paymentforeignaccount), NumberUtils.formatCardNumber(autoCrcdPayModel.getForeignCurrencyAccountNo()));
            } else {
                map.put(getResources().getString(R.string.boc_crcd_autopay_paymentaccount), NumberUtils.formatCardNumber(autoCrcdPayModel.getLocalCurrencyPaymentAccountNo()));
            }
            resultview.addDetail(map);
            resultview.updateDetail(getResources().getString(R.string.boc_crcd_autopay_result_detail));
            resultview.setHint(getResources().getString(R.string.boc_crcd_autopay_result_info));
        } else {
            resultview.addStatus(ResultHead.Status.SUCCESS, getResources().getString(R.string.boc_crcd_autopay_result_shut));
            resultview.addTitle(getResources().getString(R.string.boc_crcd_autopay_result_shut_addtitle));
        }

    }

    public String queryPaymentMode(){
        if(1 == autoCrcdPayModel.getmPaymentModeStatus()){
            return getResources().getString(R.string.boc_crcd_autopay_full);
        } else {
            return getResources().getString(R.string.boc_crcd_autopay_minp);
        }
    }

    public String queryPaymentWay(){
        if(0 == autoCrcdPayModel.getmPaymentWay()){
            return getResources().getString(R.string.boc_crcd_autopay_rmbrepay);
        } else if(1 == autoCrcdPayModel.getmPaymentWay()) {
            return getResources().getString(R.string.boc_crcd_autopay_bothrepay);
        } else {
            return getResources().getString(R.string.boc_crcd_autopay_foreignrepay);
        }
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(CrcdDetailFragment.class);
    }

    @Override
    public boolean onBack() {
        popToAndReInit(CrcdDetailFragment.class);
        return true;
    }

    @Override
    public void setPresenter(AutoCrcdPaymentContract.AutoCrcdPaymentPresenter presenter) {

    }

    @Override
    protected AutoCrcdPaymentContract.AutoCrcdPaymentPresenter initPresenter() {
        return new AutoCrcdPaymentPresenter(this);
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
    }

    @Override
    public void setCrcdPaymentWayFailed(BiiResultErrorException exception){

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
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_autopay_result_title);
    }

    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

}
