package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model.BillInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.presenter.BillInstallmentsPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * 作者：lq7090
 * 创建时间：2016/11/29.
 * 用途：信用卡账单分期确认页
 */
public class BillInstallmentsConfirmFragment extends BaseConfirmFragment<BillInstallmentModel, PsnCrcdDividedPayBillSetResultResult>  {

    public static BillInstallmentsConfirmFragment newInstance(BillInstallmentModel billInstallmentModel,VerifyBean verifyBean) {
            Bundle args = getBundleForNew(billInstallmentModel, verifyBean);
        BillInstallmentsConfirmFragment confirmFragment =
                    new BillInstallmentsConfirmFragment();
        confirmFragment.setArguments(args);
            return confirmFragment;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_confirm);
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
     * 用于给ConFirmView 添加数据
     */
    @Override
    protected void setConfirmViewData() {
        /**
         * 获取从首页传来的金额 "1002*******5288"
         */
        confirmInfoView.setHeadValue(getResources().getString(R.string.boc_crcd_installment_amount) + "("
                + PublicCodeUtils.getCurrency(getContext(), mFillInfoBean.getCurrencyCode())
                + ")", MoneyUtils.transMoneyFormat(mFillInfoBean.getAmount(),
                mFillInfoBean.getCurrencyCode()));

        LinkedHashMap<String, CharSequence> nameAndVelue = new LinkedHashMap<>();
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_accountnum), NumberUtils.formatCardNumber(mFillInfoBean.getAcctNum()));
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_installment_divperiod), mFillInfoBean.getDivPeriod() + getResources().getString(R.string.boc_crcd_period));
        String instmtChargeVal = MoneyUtils.transMoneyFormat(mFillInfoBean.getInstmtCharge(),mFillInfoBean.getCurrencyCode());
        SpannableString instmtCharge = new SpannableString(instmtChargeVal + getResources().getString(R.string.boc_crcd_imstcharge_description));

        instmtCharge.setSpan(new CharacterStyle() {
            @Override
            public void updateDrawState(TextPaint tp) {
                tp.setColor(getResources().getColor(R.color.boc_text_color_red));
            }
        }, 0, instmtCharge.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_imstCharge), instmtCharge);
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_firstinamount), MoneyUtils.transMoneyFormat(mFillInfoBean.getFirstInAmount(), mFillInfoBean.getCurrencyCode()));
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_restPerTimeInAmount), MoneyUtils.transMoneyFormat(mFillInfoBean.getRestPerTimeInAmount(), mFillInfoBean.getCurrencyCode()));
        confirmInfoView.addData(nameAndVelue, true);
    }

    /**
     * 用于初始化presenter
     * @return
     */

    @Override
    protected BaseConfirmContract.Presenter<BillInstallmentModel> initPresenter() {
         return new BillInstallmentsPresenter(this);
    }

    @Override
    public void onSubmitSuccess(PsnCrcdDividedPayBillSetResultResult submitResult) {

        closeProgressDialog();

        start(new BillInstallmentsResultFragment().getInstance(mFillInfoBean));

    }
}
