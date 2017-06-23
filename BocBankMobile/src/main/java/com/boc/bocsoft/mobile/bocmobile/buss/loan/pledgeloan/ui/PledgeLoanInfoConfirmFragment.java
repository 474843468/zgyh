package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeLoanInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.PledgeResultViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import java.util.LinkedHashMap;

public abstract class PledgeLoanInfoConfirmFragment<T extends IPledgeLoanInfoFillViewModel>
        extends BaseConfirmFragment<T, String> {

    @Override
    protected void setConfirmViewData() {
        String headName = String.format(getString(R.string.boc_pledge_info_confirm_head),
                PublicCodeUtils.getCurrency(mContext, mFillInfoBean.getCurrencyCode()));
        String headValue = MoneyUtils.transMoneyFormatNoLossAccuracy(mFillInfoBean.getAmount(),
                mFillInfoBean.getCurrencyCode());
        confirmInfoView.setHeadValue(headName, headValue);
        String period = String.format(getString(R.string.boc_pledge_info_period_month),
                mFillInfoBean.getLoanPeriod());
        String rate = MoneyUtils.transRatePercentTypeFormatTotal(mFillInfoBean.getLoanRate());
        String periodRate =
                String.format(getString(R.string.boc_pledge_info_peroid_rate_value), period, rate);
        String payCycle = mFillInfoBean.getPayCycleString();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(getString(R.string.boc_pledge_info_peroid_rate), periodRate);
        map.put(getString(R.string.boc_pledge_info_repay_type), mFillInfoBean.getPayTypeString());
        map.put(getString(R.string.boc_pledge_info_pay_cycle), payCycle);
        map.put(getString(R.string.boc_pledge_info_payee_account),
                NumberUtils.formatCardNumber(mFillInfoBean.getToActNum()));
        map.put(getString(R.string.boc_pledge_info_payer_account),
                NumberUtils.formatCardNumber(mFillInfoBean.getPayActNum()));
        confirmInfoView.addData(map, true);
    }

    @Override
    public void onSubmitSuccess(String transId) {
        closeProgressDialog();
        PledgeResultViewModel pledgeResultViewModel = new PledgeResultViewModel();
        pledgeResultViewModel.setTransId(transId);
        pledgeResultViewModel.setConversationId(mFillInfoBean.getConversationId());
        pledgeResultViewModel.setAmount(mFillInfoBean.getAmount());
        pledgeResultViewModel.setCurrencyCode(mFillInfoBean.getCurrencyCode());
        pledgeResultViewModel.setLoanPeriod(mFillInfoBean.getLoanPeriod());
        pledgeResultViewModel.setLoanRate(mFillInfoBean.getLoanRate());
        pledgeResultViewModel.setPayActNum(mFillInfoBean.getPayActNum());
        pledgeResultViewModel.setToActNum(mFillInfoBean.getToActNum());
        afterSubmitSuccess(pledgeResultViewModel);
    }

    protected abstract void afterSubmitSuccess(PledgeResultViewModel pledgeResultViewModel);
}