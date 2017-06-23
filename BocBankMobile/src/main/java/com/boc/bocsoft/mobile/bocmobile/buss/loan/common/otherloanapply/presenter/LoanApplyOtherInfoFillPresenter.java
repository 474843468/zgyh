package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanFieldQry.PsnOnLineLoanFieldQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanFieldQry.PsnOnLineLoanFieldQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanFieldBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui.LoanApplyOtherInfoFillContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.Observable;
import rx.functions.Func1;

public class LoanApplyOtherInfoFillPresenter extends RxPresenter
        implements LoanApplyOtherInfoFillContract.Presenter {

    private LoanApplyOtherInfoFillContract.View mLoanApplyOtherInfoFillView;
    private PsnLoanService mLoanService;
    private GlobalService mGlobalService;

    public LoanApplyOtherInfoFillPresenter(LoanApplyOtherInfoFillContract.View view) {
        mLoanApplyOtherInfoFillView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void getOnLineLoanFieldQry(final String productCode) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                      .compose(this.<String>bindToLifecycle())
                      .flatMap(new Func1<String, Observable<PsnOnLineLoanFieldQryResult>>() {
                          @Override
                          public Observable<PsnOnLineLoanFieldQryResult> call(
                                  String conversationId) {
                              PsnOnLineLoanFieldQryParams params =
                                      new PsnOnLineLoanFieldQryParams();
                              params.setProductCode(productCode);
                              return mLoanService.psnOnLineLoanFieldQry(params);
                          }
                      })
                      .compose(SchedulersCompat.<PsnOnLineLoanFieldQryResult>applyIoSchedulers())
                      .subscribe(new BIIBaseSubscriber<PsnOnLineLoanFieldQryResult>() {
                          @Override
                          public void handleException(
                                  BiiResultErrorException biiResultErrorException) {

                          }

                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onNext(
                                  PsnOnLineLoanFieldQryResult psnOnLineLoanFieldQryResult) {
                              if (psnOnLineLoanFieldQryResult != null) {
                                  OnLineLoanFieldBean bean =
                                          BeanConvertor.toBean(psnOnLineLoanFieldQryResult,
                                                  new OnLineLoanFieldBean());
                                  mLoanApplyOtherInfoFillView.onLoadOnLineLoanFieldSuccess(bean);
                              }
                          }
                      });
    }

}