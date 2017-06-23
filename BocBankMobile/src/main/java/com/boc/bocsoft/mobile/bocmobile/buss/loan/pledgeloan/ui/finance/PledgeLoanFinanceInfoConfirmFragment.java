package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance;

import android.os.Bundle;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.PledgeResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeFinanceInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.finance.PledgeLoanFinanceInfoConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoConfirmFragment;

/**
 * 作者：XieDu
 * 创建时间：2016/8/20 15:21
 * 描述：
 */
public class PledgeLoanFinanceInfoConfirmFragment
        extends PledgeLoanInfoConfirmFragment<PledgeFinanceInfoFillViewModel> {

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     */
    public static PledgeLoanFinanceInfoConfirmFragment newInstance(
            PledgeFinanceInfoFillViewModel pledgeFinanceInfoFillViewModel, VerifyBean verifyBean) {
        PledgeLoanFinanceInfoConfirmFragment fragment = new PledgeLoanFinanceInfoConfirmFragment();
        Bundle args = getBundleForNew(pledgeFinanceInfoFillViewModel, verifyBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public BaseConfirmContract.Presenter<PledgeFinanceInfoFillViewModel> initPresenter() {
        return new PledgeLoanFinanceInfoConfirmPresenter(this);
    }

    @Override
    protected void afterSubmitSuccess(PledgeResultViewModel pledgeResultViewModel) {
        //TODO 进入结果页面，先显示倒计时
        startWithPop(PledgeLoanFinanceResultFragment.newInstance(pledgeResultViewModel));
    }
}
