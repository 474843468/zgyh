package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANSignAccountCheck.PsnLOANSignAccountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANSignAccountCheck.PsnLOANSignAccountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui.SignAccountSelectContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.PledgeLoanConst;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.Observable;
import rx.functions.Func1;

public class SignAccountSelectPresenter extends RxPresenter
        implements SignAccountSelectContract.Presenter {

    private SignAccountSelectContract.View mLoanSignAccountSelectView;
    private PsnLoanService mLoanService;
    private String mConversationId, mLoanCurrencyCode;

    public SignAccountSelectPresenter(SignAccountSelectContract.View view) {
        mLoanSignAccountSelectView = view;
        mLoanService = new PsnLoanService();
    }

    public void setInitData(String conversationId, String loanCurrencyCode) {
        mConversationId = conversationId;
        mLoanCurrencyCode = loanCurrencyCode;
    }

    @Override
    public void checkAssignAccount(final AccountBean accountBean) {
        Observable.just(mConversationId)
                  .compose(this.<String>bindToLifecycle())
                  .flatMap(new Func1<String, Observable<PsnLOANSignAccountCheckResult>>() {
                      @Override
                      public Observable<PsnLOANSignAccountCheckResult> call(String conversationId) {
                          PsnLOANSignAccountCheckParams params =
                                  new PsnLOANSignAccountCheckParams();
                          params.setConversationId(mConversationId);
                          params.setAccountId(accountBean.getAccountId());
                          params.setAccountNumber(accountBean.getAccountNumber());
                          return mLoanService.psnLOANSignAccountCheck(params);
                      }
                  })
                  .flatMap(
                          new Func1<PsnLOANSignAccountCheckResult, Observable<PsnLOANPayeeAcountCheckResult>>() {
                              @Override
                              public Observable<PsnLOANPayeeAcountCheckResult> call(
                                      PsnLOANSignAccountCheckResult psnLOANSignAccountCheckResult) {
                                  if (psnLOANSignAccountCheckResult != null && "Y".equals(
                                          psnLOANSignAccountCheckResult.getIsSign())) {//支持签约
                                      PsnLOANPayeeAcountCheckParams checkParams =
                                              new PsnLOANPayeeAcountCheckParams();
                                      checkParams.setAccountId(accountBean.getAccountId());
                                      checkParams.setCurrencyCode(mLoanCurrencyCode);
                                      checkParams.setConversationId(mConversationId);
                                      return mLoanService.psnLOANPayeeAcountCheck(checkParams);
                                  } else {//不支持签约
                                      return Observable.error(new Throwable(
                                              ApplicationContext.getInstance()
                                                                .getString(
                                                                        R.string.boc_eloan_payment_account_cannot_sign)));
                                  }
                              }
                          })
                  .compose(SchedulersCompat.<PsnLOANPayeeAcountCheckResult>applyIoSchedulers())
                  .subscribe(new BIIBaseSubscriber<PsnLOANPayeeAcountCheckResult>() {
                                 @Override
                                 public void handleException(BiiResultErrorException biiResultErrorException) {
                                 }

                                 @Override
                                 public void onCompleted() {
                                 }

                                 @Override
                                 public void onNext(
                                         PsnLOANPayeeAcountCheckResult psnLOANPayeeAcountCheckResult) {
                                     String checkResult0 =
                                             psnLOANPayeeAcountCheckResult.getCheckResult().get(0);
                                     if ("01".equals(checkResult0)) {
                                         mLoanSignAccountSelectView.onCheckAssignAccountSuccess();
                                     } else {
                                         ((BaseMobileActivity) ActivityManager.getAppManager()
                                                                              .currentActivity()).showErrorDialog(
                                                 PledgeLoanConst.checkPayeeResultMap.get(checkResult0));
                                     }
                                 }
                             }

                  );
    }
}