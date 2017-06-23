package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.presenter.ConsumeInstallmentPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * 作者：lq7090
 * 创建时间：2016/11/22.
 * 用途：信用卡消费分期确认页
 */
public class ConsumeInstallmentConfirmFragment extends BaseConfirmFragment<ConsumeInstallmentModel, PsnCrcdDividedPayConsumeResultResult> {

    public static ConsumeInstallmentConfirmFragment getInstance(ConsumeInstallmentModel model, VerifyBean verifyBean) {
        Bundle args = getBundleForNew(model, verifyBean);
        ConsumeInstallmentConfirmFragment confirmFragment =
                new ConsumeInstallmentConfirmFragment();
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

    @Override
    protected void setConfirmViewData() {


        /**
         * 获取从首页传来的金额 "1002*******5288"
         *
         */
        confirmInfoView.setHeadValue(getResources().getString(R.string.boc_crcd_installment_amount) + " ("
                + PublicCodeUtils.getCurrency(getContext(), mFillInfoBean.getCurrencyCode())
                + ")", MoneyUtils.transMoneyFormat(mFillInfoBean.getAmount(),
                mFillInfoBean.getCurrencyCode()));

        LinkedHashMap<String, CharSequence> nameAndVelue = new LinkedHashMap<>();
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_accountnum), NumberUtils.formatCardNumber(mFillInfoBean.getAcctNum()));
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_installment_divperiod), mFillInfoBean.getDivPeriod() + getResources().getString(R.string.boc_crcd_period));
        String instmtChargeVal = MoneyUtils.transMoneyFormat(mFillInfoBean.getInstmtCharge(), mFillInfoBean.getCurrencyCode());
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

    @Override
    public void onSubmitSuccess(PsnCrcdDividedPayConsumeResultResult submitResult) {
        closeProgressDialog();
        start(new ConsumeInstallmentResultFragment().getInstance(mFillInfoBean));

    }

    @Override
    protected BaseConfirmContract.Presenter<ConsumeInstallmentModel> initPresenter() {
        return new ConsumeInstallmentPresenter(this);
    }
}
