package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit;

import android.os.Bundle;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.PledgeResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeDepositInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.deposit.PledgeLoanDepositInfoConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoConfirmFragment;

/**
 * 使用{@link PledgeLoanDepositInfoConfirmFragment#newInstance}静态方法来创建该fragment的一个实例
 */
public class PledgeLoanDepositInfoConfirmFragment
        extends PledgeLoanInfoConfirmFragment<PledgeDepositInfoFillViewModel> {

    public static PledgeLoanDepositInfoConfirmFragment newInstance(
            PledgeDepositInfoFillViewModel pledgeDepositInfoFillViewModel,
            VerifyBean verifyBean) {
        PledgeLoanDepositInfoConfirmFragment fragment = new PledgeLoanDepositInfoConfirmFragment();
        Bundle args = getBundleForNew(pledgeDepositInfoFillViewModel,verifyBean);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void afterSubmitSuccess(PledgeResultViewModel pledgeResultViewModel) {
        startWithPop(PledgeLoanDepositResultFragment.newInstance(pledgeResultViewModel));
    }

    @Override
    protected BaseConfirmContract.Presenter<PledgeDepositInfoFillViewModel> initPresenter() {
        return new PledgeLoanDepositInfoConfirmPresenter(this);
    }
}