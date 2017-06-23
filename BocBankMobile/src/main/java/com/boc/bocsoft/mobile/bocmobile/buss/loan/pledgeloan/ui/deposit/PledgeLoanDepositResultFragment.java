package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.PledgeResultViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 质押贷款结果页面
 */
public class PledgeLoanDepositResultFragment extends BussFragment
        implements OperationResultBottom.HomeBtnCallback {

    private BaseOperationResultView operationResultView;

    private static final String PARAM = "param";
    private PledgeResultViewModel mParam;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     *
     * @param param 参数
     * @return PledgeLoanDepositResultFragment的一个实例
     */
    public static PledgeLoanDepositResultFragment newInstance(PledgeResultViewModel param) {
        PledgeLoanDepositResultFragment fragment = new PledgeLoanDepositResultFragment();
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
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mParam = getArguments().getParcelable(PARAM);
            operationResultView.updateHead(OperationResultHead.Status.SUCCESS,
                    getString(R.string.boc_pledge_deposit_result_submit));
            String amount =
                    String.format(getString(R.string.boc_pledge_result_use_money_amount_value),
                            PublicCodeUtils.getCurrency(mContext, mParam.getCurrencyCode()),
                            MoneyUtils.transMoneyFormatNoLossAccuracy(mParam.getAmount(),
                                    mParam.getCurrencyCode()));
            operationResultView.addDetailRow(
                    getString(R.string.boc_pledge_result_use_money_amount_title), amount, true,
                    false);
            String period = String.format(getString(R.string.boc_pledge_info_period_month),
                    mParam.getLoanPeriod());
            String rate = MoneyUtils.transRatePercentTypeFormatTotal(mParam.getLoanRate());
            String periodRate =
                    String.format(getString(R.string.boc_pledge_info_peroid_rate_value), period,
                            rate);
            operationResultView.addDetailRow(getString(R.string.boc_pledge_info_peroid_rate),
                    periodRate, true, false);
            operationResultView.addDetailRow(getString(R.string.boc_pledge_info_payee_account),
                    NumberUtils.formatCardNumber(mParam.getToActNum()), true, false);
            operationResultView.addDetailRow(getString(R.string.boc_pledge_info_payer_account),
                    NumberUtils.formatCardNumber(mParam.getPayActNum()), false, false);
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