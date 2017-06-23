package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanSubmit.PsnOnLineLoanSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanSubmit.PsnOnLineLoanSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitResult;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui.LoanApplyOtherConfirmationContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.Observable;
import rx.functions.Func1;

public class LoanApplyOtherConfirmationPresenter extends RxPresenter
        implements LoanApplyOtherConfirmationContract.Presenter {

    private LoanApplyOtherConfirmationContract.View mLoanApplyOtherConfirmationView;
    private PsnLoanService mLoanService;
    private GlobalService mGlobalService;
    private String mConversationId;

    public LoanApplyOtherConfirmationPresenter(LoanApplyOtherConfirmationContract.View view) {
        mLoanApplyOtherConfirmationView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void submit(final OnLineLoanSubmitModel submitModel) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                      .compose(this.<String>bindToLifecycle())
                      .flatMap(new Func1<String, Observable<String>>() {
                          @Override
                          public Observable<String> call(String s) {
                              mConversationId = s;
                              PSNGetTokenIdParams tokenIdParams = new PSNGetTokenIdParams();
                              tokenIdParams.setConversationId(mConversationId);
                              return mGlobalService.psnGetTokenId(tokenIdParams);
                          }
                      })
                      .flatMap(new Func1<String, Observable<PsnOnLineLoanSubmitResult>>() {
                          @Override
                          public Observable<PsnOnLineLoanSubmitResult> call(String tokenId) {
                              PsnOnLineLoanSubmitParams submitParams =
                                      BeanConvertor.toBean(submitModel,
                                              new PsnOnLineLoanSubmitParams());
                              /*
                              特殊处理：对于微型企业贷款，同时上送企业名称和appname，后台会混乱，
                              故后台同事让前端把appname置空
                               */
                              if(!StringUtils.isEmptyOrNull(submitParams.getEntName())
                                      && !StringUtils.isEmptyOrNull(submitParams.getPrincipalName())){
                                  submitParams.setAppName("");
                              }
                              submitParams.setConversationId(mConversationId);
                              submitParams.setToken(tokenId);
                              return mLoanService.psnOnLineLoanSubmit(submitParams);
                          }
                      })
                      .compose(SchedulersCompat.<PsnOnLineLoanSubmitResult>applyIoSchedulers())
                      .subscribe(new BIIBaseSubscriber<PsnOnLineLoanSubmitResult>() {
                          @Override
                          public void handleException(
                                  BiiResultErrorException biiResultErrorException) {

                          }

                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onNext(PsnOnLineLoanSubmitResult psnOnLineLoanSubmitResult) {
                              OnLineLoanSubmitResult submitResult =
                                      BeanConvertor.toBean(psnOnLineLoanSubmitResult,
                                              new OnLineLoanSubmitResult());
                              mLoanApplyOtherConfirmationView.onSubmitSuccess(submitResult);
                          }
                      });
    }
}