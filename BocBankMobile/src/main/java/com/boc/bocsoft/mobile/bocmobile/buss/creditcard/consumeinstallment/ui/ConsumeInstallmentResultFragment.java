package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui;

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
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeInstallmentModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.util.LinkedHashMap;

/**
 * 作者：lq7090
 * 创建时间：2016/11/22.
 * 用途：信用卡消费分期结果页面
 */
public class ConsumeInstallmentResultFragment extends BussFragment implements BaseResultView.HomeBackListener {

    private BaseResultView resultview;
    private static ConsumeInstallmentResultFragment resultFragment;
    ConsumeInstallmentModel consumeInstallmentModel;
    private View rootView;

    public static ConsumeInstallmentResultFragment getInstance(ConsumeInstallmentModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("resultParams", model);
        resultFragment = new ConsumeInstallmentResultFragment();
        resultFragment.setArguments(bundle);
        return resultFragment;
    }


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        rootView = mInflater
                .from(mContext)
                .inflate(R.layout.crcd_consumeinstallment_result_fragment, null);
        return rootView;
    }


    @Override
    public void beforeInitView() {
        super.beforeInitView();
        consumeInstallmentModel =
                (ConsumeInstallmentModel) getArguments()
                        .getSerializable("resultParams");
    }

    @Override
    public void initView() {
        super.initView();
        resultview = (BaseResultView) rootView.findViewById(R.id.crcd_installment_result_view);
        resultview.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

        resultview.addStatus(ResultHead.Status.SUCCESS, getResources().getString(R.string.boc_crcd_consume_installment_success));
        resultview.addTitle("您本次分期人民币元 " + MoneyUtils.transMoneyFormat(consumeInstallmentModel.getAmount(), consumeInstallmentModel.getCurrencyCode()));

        if (consumeInstallmentModel == null)
            consumeInstallmentModel = new ConsumeInstallmentModel();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(getResources().getString(R.string.boc_crcd_installment_amount), MoneyUtils.transMoneyFormat(consumeInstallmentModel.getAmount(), consumeInstallmentModel.getCurrencyCode()));
        map.put(getResources().getString(R.string.boc_crcd_accountnum), NumberUtils.formatCardNumber(consumeInstallmentModel.getAcctNum()));
        map.put(getResources().getString(R.string.boc_crcd_installment_divperiod), consumeInstallmentModel.getDivPeriod() + getResources().getString(R.string.boc_crcd_period));
        map.put(getResources().getString(R.string.boc_crcd_imstCharge), MoneyUtils.transMoneyFormat(consumeInstallmentModel.getInstmtCharge(), consumeInstallmentModel.getCurrencyCode()) + getResources().getString(R.string.boc_crcd_imstcharge_description));
        map.put(getResources().getString(R.string.boc_crcd_firstinamount), MoneyUtils.transMoneyFormat(consumeInstallmentModel.getFirstInAmount(), consumeInstallmentModel.getCurrencyCode()));
        map.put(getResources().getString(R.string.boc_crcd_restPerTimeInAmount), MoneyUtils.transMoneyFormat(consumeInstallmentModel.getRestPerTimeInAmount(), consumeInstallmentModel.getCurrencyCode()));

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
        popToAndReInit(CreditCardHomeFragment.class);//返回并刷新数据

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
