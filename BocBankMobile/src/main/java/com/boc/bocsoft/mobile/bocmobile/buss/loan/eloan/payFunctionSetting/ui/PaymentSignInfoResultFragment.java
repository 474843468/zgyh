package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 作者：XieDu
 * 创建时间：2016/9/20 10:05
 * 描述：
 */
public class PaymentSignInfoResultFragment extends BussFragment
        implements OperationResultBottom.HomeBtnCallback {

    private BaseOperationResultView operationResultView;

    private static final String PARAM = "param";
    private PaymentSignViewModel mParam;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     *
     * @param param 参数
     * @return PledgeLoanDepositResultFragment的一个实例
     */
    public static PaymentSignInfoResultFragment newInstance(PaymentSignViewModel param) {
        PaymentSignInfoResultFragment fragment = new PaymentSignInfoResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        operationResultView = new BaseOperationResultView(mContext);
        return operationResultView;
    }

    @Override
    public void initView() {
        operationResultView.setTopDividerVisible(true);
        operationResultView.isShowBottomInfo(false);
        operationResultView.setDetailsName(getString(R.string.boc_see_detail));
    }

    @Override
    public void initData() {
        if (getArguments() != null && (mParam = getArguments().getParcelable(PARAM)) != null) {
            String headTitle = "";
            switch (mParam.getSignType()) {
                case 1://签约
                    headTitle = getString(R.string.boc_eloan_payment_result_submit_sign);
                    break;
                case 2://修改
                    headTitle = getString(R.string.boc_eloan_payment_result_submit_sign_change);
                    break;
                case 3://解约
                    headTitle = getString(R.string.boc_eloan_payment_result_submit_unsign);
                    break;
            }
            operationResultView.updateHead(OperationResultHead.Status.SUCCESS, headTitle);
            String signAccount = NumberUtils.formatCardNumber(mParam.getSignAccountNum());
            String usePref = "BOR".equals(mParam.getUsePref()) ? getString(
                    R.string.boc_eloan_payment_loan_first)
                    : getString(R.string.boc_eloan_payment_deposit_first);
            String leastAmount = String.format(
                    getContext().getString(R.string.boc_eloan_payment_amount_least_value),
                    PublicCodeUtils.getCurrency(getContext(), ApplicationConst.CURRENCY_CNY),
                    MoneyUtils.transMoneyFormat("1000.00", ApplicationConst.CURRENCY_CNY));
            operationResultView.addDetailRow(getString(R.string.boc_eloan_payment_sign_account),
                    signAccount, true, false);
            operationResultView.addDetailRow(getString(R.string.boc_eloan_payment_preference),
                    usePref, true, false);
            if ("F".equals(mParam.getQuoteFlag())) {//中银E贷
                String loanPeriod = String.format(
                        getString(R.string.boc_eloan_payment_pledge_info_period_month),
                        Integer.parseInt(mParam.getSignPeriod()));
                String payType = mParam.getPayTypeString();
                operationResultView.addDetailRow(getString(R.string.boc_eloan_payment_loan_period),
                        loanPeriod, true, false);
                operationResultView.addDetailRow(
                        getString(R.string.boc_eloan_payment_pay_type_title), payType, true, false);
            }

            operationResultView.addDetailRow(
                    getString(R.string.boc_eloan_payment_result_amount_least), leastAmount, false,
                    false);
        }
    }

    @Override
    public void setListener() {
        operationResultView.setgoHomeOnclick(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fragment_result_title);
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
    public boolean onBack() {
        popToAndReInit(LoanManagerFragment.class);
        return false;
    }

    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }
}
