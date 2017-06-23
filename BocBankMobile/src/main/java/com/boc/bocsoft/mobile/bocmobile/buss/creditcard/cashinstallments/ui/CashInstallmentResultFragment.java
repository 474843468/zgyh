package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.model.CashInstallmentViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedHashMap;

/**
 * Created by cry7096 on 2016/11/25.
 */
public class CashInstallmentResultFragment extends BussFragment implements BaseResultView.HomeBackListener, ResultBottom.OnClickListener{
    private View rootView;
    private BaseResultView cashResult;
    private static final String PARAM = "param";
    private CashInstallmentViewModel mParam;
    public static final int CRCD_AGAIN = 1;

    public static CashInstallmentResultFragment newInstance(CashInstallmentViewModel param) {
        CashInstallmentResultFragment fragment = new CashInstallmentResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_crcd_cash_result, null);
        return rootView;
    }

    @Override
    public void initView() {
        cashResult = (BaseResultView) mContentView.findViewById(R.id.BaseResultView);
        cashResult.setOnHomeBackClick(this);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mParam = getArguments().getParcelable(PARAM);
            //提款成功
            cashResult.addStatus(ResultHead.Status.SUCCESS, getResources().getString(R.string.boc_crcd_cash_status_desc));
            //二级标题
            cashResult.addTitle(getResources().getString(R.string.boc_crcd_cash_status_detail) +  MoneyUtils.transMoneyFormat(mParam.getDivAmount(),"001"));

            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            //信用卡卡号
            map.put(getString(R.string.boc_crcd_credit_num),NumberUtils.formatCardNumber(mParam.getFromCardNo()));
            //提款金额
            map.put(getString(R.string.boc_crcd_div_amount), MoneyUtils.transMoneyFormat(mParam.getDivAmount(),"001"));
            //分期期数
            String divPeriod = mParam.getDivPeriod();
            if(divPeriod.equals(getResources().getStringArray(R.array.boc_crcd_div_period_model)[0]))
                map.put(getString(R.string.boc_crcd_div_period),
                        getResources().getStringArray(R.array.boc_crcd_div_period_show)[0]);
            else if(divPeriod.equals(getResources().getStringArray(R.array.boc_crcd_div_period_model)[1]))
                map.put(getString(R.string.boc_crcd_div_period),
                        getResources().getStringArray(R.array.boc_crcd_div_period_show)[1]);
            else if(divPeriod.equals(getResources().getStringArray(R.array.boc_crcd_div_period_model)[2]))
                map.put(getString(R.string.boc_crcd_div_period),
                        getResources().getStringArray(R.array.boc_crcd_div_period_show)[2]);
            else
                map.put(getString(R.string.boc_crcd_div_period), divPeriod + getString(R.string.boc_crcd_period_line));
            //分期方式
            if(mParam.getChargeMode().equals("0")) //一次性支付
                map.put(getString(R.string.boc_crcd_div_charge),getString(R.string.boc_crcd_full_pay));
            else //分期支付
                map.put(getString(R.string.boc_crcd_div_charge),getString(R.string.boc_crcd_installment_pay));

            if(!StringUtils.isEmpty(mParam.getDivRate())) {
                //手续费率
                map.put(getString(R.string.boc_crcd_div_charge_rate), getPercentValue(mParam.getDivRate()) + getString(R.string.boc_crcd_div_rate_line));
                //首期、每期应还
                BigDecimal firstPayAmountBD = new BigDecimal(mParam.getFirstPayAmount());
                BigDecimal perPayAmountBD = new BigDecimal(mParam.getPerPayAmount());
                BigDecimal divCharge = new BigDecimal(mParam.getDivCharge());
                String firstPayAmountLine;
                String perPayAmountLine;
                if (mParam.getChargeMode().equals("1")) {  //分期收取
                    BigDecimal firstPayChargeBD = new BigDecimal(mParam.getFirstCharge());
                    BigDecimal perPayChargeBD = new BigDecimal(mParam.getPerCharge());
                    //首期应还
                    firstPayAmountLine = MoneyUtils.transMoneyFormat(firstPayAmountBD.add(firstPayChargeBD).toString(),"001")
                            + getString(R.string.boc_crcd_div_charge_show1) +  MoneyUtils.transMoneyFormat(mParam.getFirstCharge(),"001") + getString(R.string.boc_crcd_div_charge_show2);
                    //其余每期应还
                    perPayAmountLine =  MoneyUtils.transMoneyFormat(perPayAmountBD.add(perPayChargeBD).toString(),"001")
                            + getString(R.string.boc_crcd_div_charge_show1) +  MoneyUtils.transMoneyFormat(mParam.getPerCharge(),"001") +
                            getString(R.string.boc_crcd_div_charge_show2);
                } else {   //一次性收取
                    //首期应还
                    firstPayAmountLine =  MoneyUtils.transMoneyFormat(firstPayAmountBD.add(divCharge).toString(),"001")
                            + getString(R.string.boc_crcd_div_charge_show1) +  MoneyUtils.transMoneyFormat(mParam.getDivCharge(),"001") + getString(R.string.boc_crcd_div_charge_show2);
                    //其余每期应还
                    perPayAmountLine =  MoneyUtils.transMoneyFormat(mParam.getPerPayAmount(),"001");
                }
                map.put(getString(R.string.boc_crcd_first_pay_amount), firstPayAmountLine);//首期应还
                map.put(getString(R.string.boc_crcd_per_pay_amount), perPayAmountLine);//每期应还
            }
            //收款卡号
            map.put(getString(R.string.boc_crcd_debit_card),NumberUtils.formatCardNumber(mParam.getToCardNo()));
            cashResult.addDetail(map);

            //查看详情
            cashResult.updateDetail(getResources().getString(R.string.boc_crcd_autopay_result_detail));

            //您可能需要
            cashResult.addNeedItem(getResources().getString(R.string.boc_crcd_bottom_line), CRCD_AGAIN);
        }
    }

    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

    /**
     * 系统返回键
     * @return
     */
    @Override
    public boolean onBack() {
        popToAndReInit(CashInstallmentFragment.class);
        return false;
    }

    @Override
    public void setListener() {
        cashResult.setNeedListener(this);
        cashResult.setOnHomeBackClick(this);
    }


    @Override
    public void onClick(int id) {
        switch (id){
            //查询账户余额
            case CRCD_AGAIN:
                start(new OverviewFragment());
                break;
        }
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(CashInstallmentFragment.class);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_sure_result);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 数值加百分号%
     * @param value
     * @return
     */
    public static String getPercentValue(String value) {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMaximumFractionDigits(3);
        try {
            return percentInstance.format(Float.parseFloat(value));
        } catch (Exception e) {
            return "--";
        }
    }

}


