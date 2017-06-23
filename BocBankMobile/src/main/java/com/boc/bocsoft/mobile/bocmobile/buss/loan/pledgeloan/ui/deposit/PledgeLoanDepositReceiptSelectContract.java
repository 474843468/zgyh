package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.LoanDepositMultipleQueryBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class PledgeLoanDepositReceiptSelectContract {

    public interface View {
        void onQryDepositPledgeParamsDataSuccess(
                LoanDepositMultipleQueryBean loanDepositMultipleQueryBean);
    }

    public interface Presenter extends BasePresenter {
        void qryDepositPledgeParamsData(final String accountId, final String currencyCode);
    }
}