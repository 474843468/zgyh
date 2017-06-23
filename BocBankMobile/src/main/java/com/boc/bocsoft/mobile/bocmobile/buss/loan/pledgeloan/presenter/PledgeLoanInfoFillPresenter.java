package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeLoanInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.LoanRateQueryParams;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoFillContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.Observable;

/**
 * 作者：XieDu
 * 创建时间：2016/8/19 21:55
 * 描述：
 */
public abstract class PledgeLoanInfoFillPresenter<T extends IPledgeLoanInfoFillViewModel>
        extends RxPresenter implements PledgeLoanInfoFillContract.Presenter<T> {

    protected PledgeLoanInfoFillContract.View mPledgeLoanInfoFillView;
    protected PsnLoanService mLoanService;
    protected GlobalService mGlobalService;
    protected String mConversationId;

    public PledgeLoanInfoFillPresenter(PledgeLoanInfoFillContract.View view) {
        mPledgeLoanInfoFillView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void setConversationId(String conversationId) {
        mConversationId = conversationId;
    }

    protected abstract Observable<String> getRate(LoanRateQueryParams loanRateQueryParams);

    @Override
    public void qryRate(LoanRateQueryParams loanRateQueryParams) {
        getRate(loanRateQueryParams).compose(this.<String>bindToLifecycle())
                                    .compose(SchedulersCompat.<String>applyIoSchedulers())
                                    .subscribe(new BIIBaseSubscriber<String>() {
                                        @Override
                                        public void handleException(
                                                BiiResultErrorException biiResultErrorException) {

                                        }

                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onNext(String loanRate) {
                                            if (loanRate == null) {
                                                mPledgeLoanInfoFillView.onLoadEmpty();
                                            } else {
                                                mPledgeLoanInfoFillView.onQryRateSuccess(MoneyUtils.transRateFormat(loanRate));
                                            }
                                        }
                                    });
    }

    @Override
    public void qrySecurityFactor(String serviceId) {
        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
        params.setConversationId(mConversationId);
        params.setServiceId(serviceId);
        mGlobalService.psnGetSecurityFactor(params)
                      .compose(this.<PsnGetSecurityFactorResult>bindToLifecycle())
                      .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                      .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                          @Override
                          public void handleException(
                                  BiiResultErrorException biiResultErrorException) {

                          }

                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onNext(
                                  PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                              if (psnGetSecurityFactorResult == null) {
                                  mPledgeLoanInfoFillView.onLoadEmpty();
                                  return;
                              }
                              SecurityFactorModel securityFactorModel =
                                      ModelUtil.generateSecurityFactorModel(
                                              psnGetSecurityFactorResult);
                              mPledgeLoanInfoFillView.onQrySecurityFactorSuccess(
                                      securityFactorModel);
                          }
                      });
    }
}
