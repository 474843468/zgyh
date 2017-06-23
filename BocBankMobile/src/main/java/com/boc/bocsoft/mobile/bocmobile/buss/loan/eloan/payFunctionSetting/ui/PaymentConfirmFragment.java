package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import java.util.LinkedHashMap;

/**
 *
 */
public abstract class PaymentConfirmFragment
        extends BaseConfirmFragment<PaymentSignViewModel, String> {

    @Override
    protected void setConfirmViewData() {
        boolean mLoanFlag = "F".equals(mFillInfoBean.getQuoteFlag());
        String signAccount = NumberUtils.formatCardNumber(mFillInfoBean.getSignAccountNum());
        String usePref = "BOR".equals(mFillInfoBean.getUsePref()) ? getString(
                R.string.boc_eloan_payment_loan_first)
                : getString(R.string.boc_eloan_payment_deposit_first);
        String loanPeriod = "", payType = "";
        if (mLoanFlag) {
            loanPeriod =
                    String.format(getString(R.string.boc_eloan_payment_pledge_info_period_month),
                            Integer.parseInt(mFillInfoBean.getSignPeriod()));
            payType = mFillInfoBean.getPayTypeString();
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(getString(R.string.boc_eloan_payment_sign_account), signAccount);
        map.put(getString(R.string.boc_eloan_payment_preference), usePref);
        if (mLoanFlag) {
            map.put(getString(R.string.boc_eloan_payment_loan_period), loanPeriod);
            map.put(getString(R.string.boc_eloan_payment_pay_type_title), payType);
        }
        map.put(getString(R.string.boc_eloan_payment_confirm_amount_least),
                String.format(getContext().getString(R.string.boc_eloan_payment_amount_least_value),
                        PublicCodeUtils.getCurrency(getContext(), ApplicationConst.CURRENCY_CNY),
                        MoneyUtils.transMoneyFormat("1000.00", ApplicationConst.CURRENCY_CNY)));
        confirmInfoView.addData(map, true);
    }

    @Override
    public void onSubmitSuccess(String submitResult) {
        closeProgressDialog();
        //跳转到结果页面
        startWithPop(PaymentSignInfoResultFragment.newInstance(mFillInfoBean));
    }
}