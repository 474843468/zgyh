package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeAccountSelectContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 作者：XieDu
 * 创建时间：2016/9/8 15:34
 * 描述：
 */
public class PledgeAccountSelectPresenter extends RxPresenter
        implements PledgeAccountSelectContract.Presenter {
    protected PsnLoanService mLoanService;
    protected GlobalService mGlobalService;
    private PledgeAccountSelectContract.View mPledgeAccountSelectView;
    private String mConversationId;

    public PledgeAccountSelectPresenter(PledgeAccountSelectContract.View view) {
        mPledgeAccountSelectView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void setConversationId(String conversationId) {
        mConversationId = conversationId;
    }

    @Override
    public void checkPayeeAccount(AccountBean accountBean, String currencyCode) {
        PsnLOANPayeeAcountCheckParams params = new PsnLOANPayeeAcountCheckParams();
        params.setConversationId(mConversationId);
        params.setAccountId(accountBean.getAccountId());
        params.setCurrencyCode(currencyCode);
        mLoanService.psnLOANPayeeAcountCheck(params)
                    .compose(this.<PsnLOANPayeeAcountCheckResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnLOANPayeeAcountCheckResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnLOANPayeeAcountCheckResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(
                                PsnLOANPayeeAcountCheckResult psnLOANPayeeAcountCheckResult) {
                            mPledgeAccountSelectView.onCheckPayeeAccountSuccess(
                                    psnLOANPayeeAcountCheckResult.getCheckResult());
                        }
                    });
    }

    @Override
    public void checkPayerAccount(AccountBean accountBean, String currencyCode) {
        PsnLOANPayerAcountCheckParams params = new PsnLOANPayerAcountCheckParams();
        params.setConversationId(mConversationId);
        params.setAccountId(accountBean.getAccountId());
        params.setCurrencyCode(currencyCode);
        mLoanService.psnLOANPayerAcountCheck(params)
                    .compose(this.<PsnLOANPayerAcountCheckResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnLOANPayerAcountCheckResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnLOANPayerAcountCheckResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(
                                PsnLOANPayerAcountCheckResult psnLOANPayerAcountCheckResult) {
                            mPledgeAccountSelectView.onCheckPayerAccountSuccess(
                                    psnLOANPayerAcountCheckResult.getCheckResult());
                        }
                    });
    }
}
