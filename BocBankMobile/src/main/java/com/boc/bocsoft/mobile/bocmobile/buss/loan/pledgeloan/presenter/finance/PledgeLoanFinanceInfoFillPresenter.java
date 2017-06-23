package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.finance;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeRateQry.PsnLoanXpadPledgeRateQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify.PsnLoanXpadPledgeVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify.PsnLoanXpadPledgeVerifyResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.LoanRateQueryParams;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeFinanceInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.PledgeLoanInfoFillPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoFillContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import rx.Observable;

/**
 * 作者：XieDu
 * 创建时间：2016/8/20 14:11
 * 描述：
 */
public class PledgeLoanFinanceInfoFillPresenter
        extends PledgeLoanInfoFillPresenter<PledgeFinanceInfoFillViewModel> {

    public PledgeLoanFinanceInfoFillPresenter(PledgeLoanInfoFillContract.View view) {
        super(view);
    }

    @Override
    protected Observable<String> getRate(LoanRateQueryParams loanRateQueryParams) {
        PsnLoanXpadPledgeRateQryParams params =
                BeanConvertor.toBean(loanRateQueryParams, new PsnLoanXpadPledgeRateQryParams());
        return mLoanService.psnLoanXpadPledgeRateQry(params);
    }

    @Override
    public void verify(final PledgeFinanceInfoFillViewModel pledgeLoanInfoFillViewModel) {
        PsnLoanXpadPledgeVerifyParams verifyParams =
                BeanConvertor.fromBean(pledgeLoanInfoFillViewModel,
                        new PsnLoanXpadPledgeVerifyParams());
        verifyParams.setToAcctNum(pledgeLoanInfoFillViewModel.getToActNum());
        verifyParams.setToAccountId(pledgeLoanInfoFillViewModel.getToAccount());
        verifyParams.setLoanRepayAccountId(pledgeLoanInfoFillViewModel.getPayAccount());
        mLoanService.psnLoanXpadPledgeVerify(verifyParams)
                    .compose(this.<PsnLoanXpadPledgeVerifyResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnLoanXpadPledgeVerifyResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnLoanXpadPledgeVerifyResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(
                                PsnLoanXpadPledgeVerifyResult psnLoanXpadPledgeVerifyResult) {
                            VerifyBean verifyBean =
                                    BeanConvertor.toBean(psnLoanXpadPledgeVerifyResult,
                                            new VerifyBean());
                            mPledgeLoanInfoFillView.onVerifySuccess(verifyBean);
                        }
                    });
    }
}
