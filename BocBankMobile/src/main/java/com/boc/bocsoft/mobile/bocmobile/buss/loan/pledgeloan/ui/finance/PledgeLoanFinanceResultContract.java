package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.PledgeResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeFinanceQryResultBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import rx.Observable;

public class PledgeLoanFinanceResultContract {

    public interface View {
        void onQryPledgeFinanceResultSuccess(PledgeFinanceQryResultBean pledgeFinanceQryResultBean);
        void onQryPledgeFinanceResultFailed();

    }

    public interface Presenter extends BasePresenter {
        Observable<PledgeFinanceQryResultBean> qryPledgeFinanceResult(PledgeResultViewModel pledgeResultViewModel,boolean isLast);
    }
}