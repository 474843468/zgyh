package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.model.XpadVFGBailTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.presenter.ConfirmInformationPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 *@author hty7062
 * 保证金转入转出--确认信息页面
 */
public class ConfirmInformationFragment extends MvpBussFragment<ConfirmInformationContract.Presenter> implements ConfirmInformationContract.View, ConfirmInfoView.OnClickListener{

    private View rootView;
    private ConfirmInformationContract.Presenter mConfirmInfationPresenter;
    //private Button confirmButton;
    private ConfirmInfoView confirmInfoView;
    private String mAmount;
    private String settlementCurrency;
    private String cashRemit;
    private String currencyCode;
    private String settleMarginAccount;
    private Boolean modeTransfer;
    /*** pageId 从那个页面进入*/
    private Class<? extends BussFragment> pageClass;
    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_confirm_information, null);
        return rootView;
    }

    @Override
    public void initView() {
        confirmInfoView = (ConfirmInfoView) rootView.findViewById(R.id.confirm_info_view);
        confirmInfoView.isShowSecurity(false);
    }

    @Override
    public void initData() {
        LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        mAmount = getArguments().getString("MAMOUNT");
        currencyCode = getArguments().getString("CURRENCY_CODE");
        settlementCurrency = getArguments().getString("SETTLEMENT_CURRENCY");
        cashRemit = getArguments().getString("CASH_REMIT");
        settleMarginAccount = getArguments().getString("SETTLE_MARGIN_ACCOUNT");
        modeTransfer = getArguments().getBoolean("MODE_TRANSFER");
        pageClass = (Class<? extends BussFragment>)getArguments().getSerializable("pageClass");

        if (modeTransfer){
            if ("001".equals(currencyCode)){
                confirmInfoView.setHeadValue("存入金额" + "(" + settlementCurrency + ")", MoneyUtils.transMoneyFormat(mAmount, currencyCode));
            }else {
                confirmInfoView.setHeadValue("转出金额" + "(" + PublicCodeUtils.getCurrency(mContext,currencyCode) +"/" + settlementCurrency + ")", MoneyUtils.transMoneyFormat(mAmount, currencyCode));
            }
            datas.put("资金账户", NumberUtils.formatCardNumberStrong(settleMarginAccount));
            datas.put("操作方式", "资金账户转保证金账户");
        }
        else{
            if ("001".equals(currencyCode)) {
                confirmInfoView.setHeadValue("存入金额" + "(" + settlementCurrency + ")", MoneyUtils.transMoneyFormat(mAmount, currencyCode));
            }else{
                confirmInfoView.setHeadValue("转出金额" + "(" + PublicCodeUtils.getCurrency(mContext,currencyCode) +"/" + settlementCurrency + ")", MoneyUtils.transMoneyFormat(mAmount, currencyCode));
            }
            datas.put("资金账户", NumberUtils.formatCardNumberStrong(settleMarginAccount));
            datas.put("操作方式", "保证金账户转资金账户");
        }
        confirmInfoView.addData(datas, false, false);
    }

    @Override
    protected ConfirmInformationContract.Presenter initPresenter() {
        return new ConfirmInformationPresenter(this);
    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(this);
    }

    @Override
    public void onClickConfirm(){
        /** 010 保证金转入转出*/
        showLoadingDialog();
        XpadVFGBailTransferViewModel viewModel = new XpadVFGBailTransferViewModel();
        viewModel.setCurrencyCode(currencyCode);
        System.out.println("ConfirmInform------>>>>>" + currencyCode);
        viewModel.setCashRemit(cashRemit);
        if (modeTransfer){
            viewModel.setFundTransferDir("I");
        }else viewModel.setFundTransferDir("O");
        viewModel.setAmount(mAmount);
        getPresenter().psnVFGBailTransfer(viewModel);
    }

    @Override
    public void onClickChange() {
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
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
    public void psnVFGBailTransferSuccess(XpadVFGBailTransferViewModel xpadVFGBailTransferViewModel) {

        OperateResultFragment operateResultFragment = new OperateResultFragment();
        Bundle bundle = new Bundle();
        /**金额数*/
        bundle.putString("MAMOUNT", mAmount);
        /**操作方式*/
        bundle.putBoolean("MODE_TRANSFER", modeTransfer);
        /**网银交易账户*/
        bundle.putString("TRANSACTION_NO", xpadVFGBailTransferViewModel.getTransactionId());
        /**转入、出成功后保证金余额*/
        bundle.putString("STOCK_BALANCE", xpadVFGBailTransferViewModel.getStockBalance().toString());
        /**结算币种*/
        bundle.putString("SETTLEMENT_CURRENCY", settlementCurrency);
        bundle.putString("CASH_REMIT", cashRemit);
        bundle.putString("SETTLE_MARGIN_ACCOUNT", settleMarginAccount);
        bundle.putString("CURRENCY_CODE", currencyCode);
        bundle.putSerializable("pageClass",pageClass);

        operateResultFragment.setArguments(bundle);
        closeProgressDialog();
        start(operateResultFragment);
        Log.d("TransactionId", "--------->>>>" + xpadVFGBailTransferViewModel.getTransactionId());
    }

    @Override
    public void psnVFGBailTransferFail(BiiResultErrorException billResultErrorException) {
        closeProgressDialog();
    }
}