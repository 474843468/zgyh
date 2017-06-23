package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.deposit;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify.PsnLOANPledgeVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify.PsnLOANPledgeVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRateQuery.PsnLoanRateQueryParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.LoanRateQueryParams;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeDepositInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.PledgeLoanInfoFillPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoFillContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import rx.Observable;

public class PledgeLoanDepositInfoFillPresenter
        extends PledgeLoanInfoFillPresenter<PledgeDepositInfoFillViewModel> {

    public PledgeLoanDepositInfoFillPresenter(PledgeLoanInfoFillContract.View view) {
        super(view);
    }

    @Override
    protected Observable<String> getRate(LoanRateQueryParams loanRateQueryParams) {
        PsnLoanRateQueryParams params =
                BeanConvertor.toBean(loanRateQueryParams, new PsnLoanRateQueryParams());
        return mLoanService.psnLoanRateQuery(params);
    }

    @Override
    public void verify(final PledgeDepositInfoFillViewModel pledgeLoanInfoFillViewModel) {
        PsnLOANPledgeVerifyParams verifyParams = BeanConvertor.fromBean(pledgeLoanInfoFillViewModel,
                new PsnLOANPledgeVerifyParams());
        mLoanService.psnLOANPledgeVerify(verifyParams)
                    .compose(this.<PsnLOANPledgeVerifyResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnLOANPledgeVerifyResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnLOANPledgeVerifyResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(PsnLOANPledgeVerifyResult psnLOANPledgeVerifyResult) {
                            VerifyBean verifyBean =
                                    BeanConvertor.toBean(psnLOANPledgeVerifyResult,
                                            new VerifyBean());
                            mPledgeLoanInfoFillView.onVerifySuccess(verifyBean);
                        }
                    });
    }
}