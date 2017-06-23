package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDiv.PsnCrcdApplyCashDivResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.model.CashInstallmentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.presenter.CashInstallmentConfirmPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedHashMap;

/**
 * Created by cry7096 on 2016/12/1.
 */
public class CashInstallmentConfirmFragment extends BaseConfirmFragment<CashInstallmentViewModel, PsnCrcdApplyCashDivResult> {

    public static CashInstallmentConfirmFragment newInstance(
            CashInstallmentViewModel cashInstallmentViewModel, VerifyBean verifyBean) {
        Bundle args = getBundleForNew(cashInstallmentViewModel, verifyBean);
        CashInstallmentConfirmFragment cashInstallmentConfirmFragment =
                new CashInstallmentConfirmFragment();
        cashInstallmentConfirmFragment.setArguments(args);
        return cashInstallmentConfirmFragment;
    }

    @Override
    protected BaseConfirmContract.Presenter<CashInstallmentViewModel> initPresenter() {
        return new CashInstallmentConfirmPresenter(this);
    }

    @Override
    protected void setConfirmViewData() {
        LinkedHashMap<String, CharSequence> map = new LinkedHashMap<>();
        confirmInfoView.setHeadValue(getString(R.string.boc_crcd_div_amount) +
                        getString(R.string.boc_crcd_money_yuan),
                MoneyUtils.transMoneyFormat(mFillInfoBean.getDivAmount(), "001"), false);
        //信用卡卡号
        map.put(getString(R.string.boc_crcd_credit_account_num), NumberUtils.formatCardNumber(mFillInfoBean.getFromCardNo()));
        //分期期数
        String divPeriod = mFillInfoBean.getDivPeriod();
        if (divPeriod.equals(getResources().getStringArray(R.array.boc_crcd_div_period_model)[0]))
            map.put(getString(R.string.boc_crcd_div_period), getResources().getStringArray(R.array.boc_crcd_div_period_show)[0]);//分期期数
        else if (divPeriod.equals(getResources().getStringArray(R.array.boc_crcd_div_period_model)[1]))
            map.put(getString(R.string.boc_crcd_div_period), getResources().getStringArray(R.array.boc_crcd_div_period_show)[1]);//分期期数
        else if (divPeriod.equals(getResources().getStringArray(R.array.boc_crcd_div_period_model)[2]))
            map.put(getString(R.string.boc_crcd_div_period), getResources().getStringArray(R.array.boc_crcd_div_period_show)[2]);//分期期数
        else
            map.put(getString(R.string.boc_crcd_div_period), divPeriod + getString(R.string.boc_crcd_period_line));
        //分期方式
        if (mFillInfoBean.getChargeMode().equals("1"))
            map.put(getString(R.string.boc_crcd_div_charge), getString(R.string.boc_crcd_installment_pay));//分期方式
        else
            map.put(getString(R.string.boc_crcd_div_charge), getString(R.string.boc_crcd_full_pay));//一次性支付

        if (!StringUtils.isEmpty(mFillInfoBean.getFirstPayAmount())) {
            //手续费率
            String divRatePercent = getPercentValue(mFillInfoBean.getDivRate());
            String divRateLine = divRatePercent + getString(R.string.boc_crcd_div_rate_line);
            SpannableString instmtCharge = new SpannableString(divRateLine);
            instmtCharge.setSpan(new CharacterStyle() {
                @Override
                public void updateDrawState(TextPaint tp) {
                    tp.setColor(getResources().getColor(R.color.boc_text_color_red));
                }
            }, divRatePercent.length(), divRateLine.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            map.put(getString(R.string.boc_crcd_div_charge_rate), instmtCharge);

            BigDecimal firstPayAmountBD = new BigDecimal(mFillInfoBean.getFirstPayAmount());
            BigDecimal perPayAmountBD = new BigDecimal(mFillInfoBean.getPerPayAmount());
            BigDecimal divCharge = new BigDecimal(mFillInfoBean.getDivCharge());
            //分期收取
            String firstPayAmountLine;
            String perPayAmountLine;
            if (mFillInfoBean.getChargeMode().equals("1")) {
                BigDecimal firstPayChargeBD = new BigDecimal(mFillInfoBean.getFirstCharge());
                BigDecimal perPayChargeBD = new BigDecimal(mFillInfoBean.getPerCharge());
                //首期应还
                firstPayAmountLine = MoneyUtils.transMoneyFormat(firstPayAmountBD.add(firstPayChargeBD)
                        .toString(), "001") + getString(R.string.boc_crcd_div_charge_show1) + MoneyUtils.transMoneyFormat(mFillInfoBean.getFirstCharge(), "001")
                        + getString(R.string.boc_crcd_div_charge_show2);
                //其余每期应还
                perPayAmountLine = MoneyUtils.transMoneyFormat(perPayAmountBD.add(perPayChargeBD).toString(), "001")
                        + getString(R.string.boc_crcd_div_charge_show1) + MoneyUtils.transMoneyFormat(mFillInfoBean.getPerCharge(), "001") + getString(R.string.boc_crcd_div_charge_show2);
            } else {//一次性收取
                //首期应还
                firstPayAmountLine = MoneyUtils.transMoneyFormat(firstPayAmountBD.add(divCharge).toString(), "001")
                        + getString(R.string.boc_crcd_div_charge_show1) + MoneyUtils.transMoneyFormat(mFillInfoBean.getDivCharge(), "001") + getString(R.string.boc_crcd_div_charge_show2);
                //其余每期应还
                perPayAmountLine = MoneyUtils.transMoneyFormat(mFillInfoBean.getPerPayAmount(), "001");
            }
            map.put(getString(R.string.boc_crcd_first_pay_amount), firstPayAmountLine);//首期应还
            map.put(getString(R.string.boc_crcd_per_pay_amount), perPayAmountLine);//其余每期应还
        }
        //收款卡号
        map.put(getString(R.string.boc_crcd_debit_card), NumberUtils.formatCardNumber(mFillInfoBean.getToCardNo()));
        confirmInfoView.addData(map, true);
    }

    @Override
    public void onSubmitSuccess(PsnCrcdApplyCashDivResult submitResult) {
        closeProgressDialog();
        mFillInfoBean.setTransactionId(submitResult.getTransactionId());
        //跳转到结果页面
        startWithPop(CashInstallmentResultFragment.newInstance(mFillInfoBean));
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

    /**
     * 金额去掉小数点后的0
     * @param input
     * @return
     */
    private static String format(String input) {
        if (input.indexOf(".") > 0) {
            String s = input.replaceAll("0+?$", "");
            return s.replaceAll("[.]$", "");
        }
        return input;
    }
}
