package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.finance;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeQry.PsnLoanXpadPledgeQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeQry.PsnLoanXpadPledgeQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.PledgeResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeFinanceQryResultBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance.PledgeLoanFinanceResultContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.Observable;
import rx.functions.Func1;

public class PledgeLoanFinanceResultPresenter extends RxPresenter
        implements PledgeLoanFinanceResultContract.Presenter {

    private PsnLoanService mLoanService;
    private PledgeLoanFinanceResultContract.View mPledgeLoanFinanceResultView;

    public PledgeLoanFinanceResultPresenter(PledgeLoanFinanceResultContract.View view) {
        mPledgeLoanFinanceResultView = view;
        mLoanService = new PsnLoanService();
    }

    @Override
    public Observable<PledgeFinanceQryResultBean> qryPledgeFinanceResult(
            final PledgeResultViewModel pledgeResultViewModel, final boolean isLast) {
        PsnLoanXpadPledgeQryParams psnLoanXpadPledgeQryParams = new PsnLoanXpadPledgeQryParams();
        psnLoanXpadPledgeQryParams.setTransId(pledgeResultViewModel.getTransId());
        psnLoanXpadPledgeQryParams.setConversationId(pledgeResultViewModel.getConversationId());
        psnLoanXpadPledgeQryParams.setLoanType("FIN-LOAN");
        psnLoanXpadPledgeQryParams.setIsLastFlag(isLast ? "Y" : "N");
        return mLoanService.psnLoanXpadPledgeQry(psnLoanXpadPledgeQryParams)
                           .compose(this.<PsnLoanXpadPledgeQryResult>bindToLifecycle())
                           .map(new Func1<PsnLoanXpadPledgeQryResult, PledgeFinanceQryResultBean>() {
                               @Override
                               public PledgeFinanceQryResultBean call(
                                       PsnLoanXpadPledgeQryResult psnLoanXpadPledgeQryResult) {
                                   return BeanConvertor.toBean(psnLoanXpadPledgeQryResult,
                                           new PledgeFinanceQryResultBean());
                               }
                           });
    }
}