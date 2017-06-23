package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.CreditCardHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model.BillInstallmentModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.util.LinkedHashMap;

/**
 * 作者：lq7090
 * 创建时间：2016/12/10.
 * 用途：信用卡账单分期结果页面
 */
public class BillInstallmentsResultFragment extends BussFragment implements BaseResultView.HomeBackListener {

    private BaseResultView resultview;
    private static BillInstallmentsResultFragment resultFragment;
    BillInstallmentModel billInstallmentModel;
    private View rootView;


    public static BillInstallmentsResultFragment getInstance(BillInstallmentModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("resultParams", model);
        resultFragment = new BillInstallmentsResultFragment();
        resultFragment.setArguments(bundle);
        return resultFragment;
    }


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        rootView = mInflater.from(mContext).inflate(R.layout.crcd_billinstallment_result_fragment, null);
        return rootView;
    }


    @Override
    public void beforeInitView() {
        super.beforeInitView();
        billInstallmentModel = (BillInstallmentModel) getArguments().getSerializable("resultParams");

    }

    @Override
    public void initView() {
        super.initView();
        resultview = (BaseResultView) rootView.findViewById(R.id.crcd_installment_result_view);

        resultview.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {

        resultview.addStatus(ResultHead.Status.SUCCESS, getResources().getString(R.string.boc_crcd_bill_installment_success));
        resultview.addTitle("您本次分期人民币元 " + MoneyUtils.transMoneyFormat(billInstallmentModel.getAmount(), billInstallmentModel.getCurrencyCode()));

        if (billInstallmentModel == null)
            billInstallmentModel = new BillInstallmentModel();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(getResources().getString(R.string.boc_crcd_installment_amount), MoneyUtils.transMoneyFormat(billInstallmentModel.getAmount(), billInstallmentModel.getCurrencyCode()));
        map.put(getResources().getString(R.string.boc_crcd_accountnum), NumberUtils.formatCardNumber(billInstallmentModel.getAcctNum()));
        map.put(getResources().getString(R.string.boc_crcd_installment_divperiod), billInstallmentModel.getDivPeriod() + getResources().getString(R.string.boc_crcd_period));
        map.put(getResources().getString(R.string.boc_crcd_imstCharge), MoneyUtils.transMoneyFormat(billInstallmentModel.getInstmtCharge(), billInstallmentModel.getCurrencyCode())+getResources().getString(R.string.boc_crcd_imstcharge_description));
        map.put(getResources().getString(R.string.boc_crcd_firstinamount), MoneyUtils.transMoneyFormat(billInstallmentModel.getFirstInAmount(), billInstallmentModel.getCurrencyCode()));
        map.put(getResources().getString(R.string.boc_crcd_restPerTimeInAmount), MoneyUtils.transMoneyFormat(billInstallmentModel.getRestPerTimeInAmount(), billInstallmentModel.getCurrencyCode()));
        map.put(getResources().getString(R.string.boc_crcd_restAmount),MoneyUtils.transMoneyFormat(billInstallmentModel.getRestAmount(), billInstallmentModel.getCurrencyCode()));

        resultview.addDetail(map);
        resultview.updateDetail(getResources().getString(R.string.boc_crcd_more_detail));


    }

    @Override
    public void setListener() {
        resultview.setOnHomeBackClick(this);
    }


    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(CreditCardHomeFragment.class);

    }

    @Override
    public boolean onBack() {
        ActivityManager.getAppManager().finishActivity();
        return true;
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
        return getResources().getString(R.string.boc_crcd_operate_result);
    }

    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }
}
