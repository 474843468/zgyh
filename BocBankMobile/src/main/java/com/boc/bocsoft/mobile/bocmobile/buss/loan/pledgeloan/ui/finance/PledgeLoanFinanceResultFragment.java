package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.countdowntime.CountDownPollingView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.PledgeResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeFinanceQryResultBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.finance.PledgeLoanFinanceResultPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import rx.Observable;
import rx.functions.Func1;

/**
 * 使用{@link PledgeLoanFinanceResultFragment#newInstance}静态方法来创建该fragment的一个实例
 */
public class PledgeLoanFinanceResultFragment
        extends MvpBussFragment<PledgeLoanFinanceResultContract.Presenter>
        implements PledgeLoanFinanceResultContract.View, OperationResultBottom.HomeBtnCallback,
        CountDownPollingView.CountDownPollingTaskListener<PledgeFinanceQryResultBean> {

    private static final String PARAM = "param";
    protected CountDownPollingView<PledgeFinanceQryResultBean> viewCountDownTime;
    private View rootView;
    private BaseOperationResultView operationResultView;

    private PledgeResultViewModel mPledgeResultViewModel;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     *
     * @param param 参数
     * @return PledgeLoanFinanceResultFragment的一个实例
     */
    public static PledgeLoanFinanceResultFragment newInstance(PledgeResultViewModel param) {
        PledgeLoanFinanceResultFragment fragment = new PledgeLoanFinanceResultFragment();
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
        rootView = mInflater.inflate(R.layout.boc_fragment_pledge_loan_finance_result, null);
        return rootView;
    }

    @Override
    public void initView() {
        viewCountDownTime = (CountDownPollingView) rootView.findViewById(R.id.view_count_down_time);
        viewCountDownTime.setTotalTime(20)
                         .setPeriod(5)
                         .setTips(getString(R.string.boc_pledge_result_qry_tips));
        operationResultView = new BaseOperationResultView(mContext);
        operationResultView.setTopDividerVisible(true);
        operationResultView.isShowBottomInfo(false);
    }

    public void setListener() {
        viewCountDownTime.setCountDownTimeListener(this);
        operationResultView.setgoHomeOnclick(this);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mPledgeResultViewModel = getArguments().getParcelable(PARAM);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        countDownTime();
    }

    @Override
    protected PledgeLoanFinanceResultContract.Presenter initPresenter() {
        return new PledgeLoanFinanceResultPresenter(this);
    }

    private void countDownTime() {
        viewCountDownTime.startCountDownTime(
                new Func1<Integer, Observable<PledgeFinanceQryResultBean>>() {
                    @Override
                    public Observable<PledgeFinanceQryResultBean> call(Integer count) {
                        return getPresenter().qryPledgeFinanceResult(mPledgeResultViewModel,
                                count == 3);
                    }
                }, new Func1<PledgeFinanceQryResultBean, Boolean>() {
                    @Override
                    public Boolean call(PledgeFinanceQryResultBean pledgeFinanceQryResultBean) {
                        return !"D".equals(pledgeFinanceQryResultBean.getStatus());
                    }
                });
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
    public void onQryPledgeFinanceResultSuccess(
            PledgeFinanceQryResultBean pledgeFinanceQryResultBean) {
        onQryPledgeFinanceResult();
        if ("A".equals(pledgeFinanceQryResultBean.getStatus())) {
            operationResultView.updateHead(OperationResultHead.Status.SUCCESS,
                    getString(R.string.boc_pledge_result_submit_success));
        } else if ("D".equals(pledgeFinanceQryResultBean.getStatus())) {
            operationResultView.updateHead(OperationResultHead.Status.INPROGRESS,
                    getString(R.string.boc_pledge_result_submit_progress));
        } else {
            operationResultView.updateHead(OperationResultHead.Status.FAIL,
                    getString(R.string.boc_pledge_result_submit_failed));
        }
        if ("A".equals(pledgeFinanceQryResultBean.getStatus()) || "D".equals(
                pledgeFinanceQryResultBean.getStatus())) {
            String amount =
                    String.format(getString(R.string.boc_pledge_result_use_money_amount_value),
                            PublicCodeUtils.getCurrency(mContext, ApplicationConst.CURRENCY_CNY),
                            MoneyUtils.transMoneyFormatNoLossAccuracy(pledgeFinanceQryResultBean.getAmount(),
                                    ApplicationConst.CURRENCY_CNY));
            operationResultView.addDetailRow(
                    getString(R.string.boc_pledge_result_use_money_amount_title), amount, true,
                    false);
            String period = String.format(getString(R.string.boc_pledge_info_period_month),
                    pledgeFinanceQryResultBean.getLoanPeriod());
            String rate =
                    MoneyUtils.transRatePercentTypeFormatTotal(pledgeFinanceQryResultBean.getLoanRate());
            String periodRate =
                    String.format(getString(R.string.boc_pledge_info_peroid_rate_value), period,
                            rate);
            operationResultView.addDetailRow(getString(R.string.boc_pledge_info_peroid_rate),
                    periodRate, true, false);
            operationResultView.addDetailRow(getString(R.string.boc_pledge_info_payee_account),
                    NumberUtils.formatCardNumber(mPledgeResultViewModel.getToActNum()), true,
                    false);
            operationResultView.addDetailRow(getString(R.string.boc_pledge_info_payer_account),
                    NumberUtils.formatCardNumber(mPledgeResultViewModel.getPayActNum()), false,
                    false);
        } else {
            if (!StringUtils.isEmpty(pledgeFinanceQryResultBean.getReturnMsg())) {
                operationResultView.addDetailRow(getString(R.string.boc_common_fail_cause),
                        pledgeFinanceQryResultBean.getReturnMsg(), false, false);
            }
        }
    }

    private void onQryPledgeFinanceResult() {
        closeProgressDialog();
        mContentView.removeAllViews();
        operationResultView.detail.getLayoutDetailParent().removeAllViews();
        mContentView.addView(operationResultView);
    }

    @Override
    public void onQryPledgeFinanceResultFailed() {
        onQryPledgeFinanceResult();
        operationResultView.updateHead(OperationResultHead.Status.INPROGRESS,
                getString(R.string.boc_pledge_result_submit_progress));
        String amount = String.format(getString(R.string.boc_pledge_result_use_money_amount_value),
                PublicCodeUtils.getCurrency(mContext, ApplicationConst.CURRENCY_CNY),
                MoneyUtils.transMoneyFormatNoLossAccuracy(mPledgeResultViewModel.getAmount(),
                        ApplicationConst.CURRENCY_CNY));
        operationResultView.addDetailRow(
                getString(R.string.boc_pledge_result_use_money_amount_title), amount, true, false);
        String period = String.format(getString(R.string.boc_pledge_info_period_month),
                mPledgeResultViewModel.getLoanPeriod());
        String rate = MoneyUtils.transRatePercentTypeFormatTotal(mPledgeResultViewModel.getLoanRate());
        String periodRate =
                String.format(getString(R.string.boc_pledge_info_peroid_rate_value), period, rate);
        operationResultView.addDetailRow(getString(R.string.boc_pledge_info_peroid_rate),
                periodRate, true, false);
        operationResultView.addDetailRow(getString(R.string.boc_pledge_info_payee_account),
                NumberUtils.formatCardNumber(mPledgeResultViewModel.getToActNum()), true, false);
        operationResultView.addDetailRow(getString(R.string.boc_pledge_info_payer_account),
                NumberUtils.formatCardNumber(mPledgeResultViewModel.getPayActNum()), false, false);
    }

    @Override
    public boolean onBack() {
        popToAndReInit(LoanManagerFragment.class);
        return false;
    }

    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

    //    @Override
    //    public void onTaskTimeOut() {
    //        onTaskFailed();
    //    }

    @Override
    public void onTaskFailed() {
        onQryPledgeFinanceResultFailed();
    }

    @Override
    public void onTaskSuccess(PledgeFinanceQryResultBean pledgeFinanceQryResultBean) {
        onQryPledgeFinanceResultSuccess(pledgeFinanceQryResultBean);
    }
}