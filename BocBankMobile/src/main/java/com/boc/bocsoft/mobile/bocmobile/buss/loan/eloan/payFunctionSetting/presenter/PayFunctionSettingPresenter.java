package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedVerify.PsnLOANChangeSignedVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedVerify.PsnLOANChangeSignedVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPaymentInfoQuery.PsnLOANPaymentInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPaymentInfoQuery.PsnLOANPaymentInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignContractQuery.PsnLoanPaymentSignContractQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignContractQuery.PsnLoanPaymentSignContractQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify.PsnLoanPaymentUnsignVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify.PsnLoanPaymentUnsignVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentInfo;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.QryPaymentInfoParams;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui.PayFunctionSettingContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.Observable;
import rx.functions.Func1;

public class PayFunctionSettingPresenter extends RxPresenter
        implements PayFunctionSettingContract.Presenter {

    private PayFunctionSettingContract.View mPayFunctionSettingMainView;
    private GlobalService mGlobalService;
    private PsnLoanService mLoanService;
    private String mConversationId;
    private PaymentInfo mPaymentInfo;
    private String mContract;

    public PayFunctionSettingPresenter(PayFunctionSettingContract.View view) {
        mPayFunctionSettingMainView = view;
        mGlobalService = new GlobalService();
        mLoanService = new PsnLoanService();
    }

    @Override
    public void qryPaymentInfo(final QryPaymentInfoParams qryPaymentInfoParams) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                      .compose(this.<String>bindToLifecycle())
                      .flatMap(new Func1<String, Observable<PsnLOANPaymentInfoQueryResult>>() {
                          @Override
                          public Observable<PsnLOANPaymentInfoQueryResult> call(
                                  String conversationId) {
                              mConversationId = conversationId;
                              PsnLOANPaymentInfoQueryParams queryParams =
                                      new PsnLOANPaymentInfoQueryParams();
                              queryParams.setConversationId(mConversationId);
                              queryParams.setLoanActNum(qryPaymentInfoParams.getQuoteOrActNo());
                              return qryPaymentInfoParams.isLoanTypeFlag() ? Observable.just(
                                      (PsnLOANPaymentInfoQueryResult) null)
                                      : mLoanService.psnLOANPaymentInfoQuery(queryParams);
                          }
                      })
                      .map(new Func1<PsnLOANPaymentInfoQueryResult, PaymentInfo>() {

                          @Override
                          public PaymentInfo call(
                                  PsnLOANPaymentInfoQueryResult psnLOANPaymentInfoQueryResult) {
                              mPaymentInfo = BeanConvertor.toBean(psnLOANPaymentInfoQueryResult,
                                      new PaymentInfo());
                              return mPaymentInfo;
                          }
                      })
                      .compose(SchedulersCompat.<PaymentInfo>applyIoSchedulers())
                      .subscribe(new BIIBaseSubscriber<PaymentInfo>() {

                          @Override
                          public void commonHandleException(
                                  BiiResultErrorException biiResultErrorException) {
                              if (!"BANCS.0188".equals(biiResultErrorException.getErrorCode())) {
                                  super.commonHandleException(biiResultErrorException);
                              }
                          }

                          @Override
                          public void handleException(
                                  BiiResultErrorException biiResultErrorException) {
                              if (!"BANCS.0188".equals(biiResultErrorException.getErrorCode())) {
                                  mPayFunctionSettingMainView.onQryPaymentInfoFailed(
                                          biiResultErrorException.getErrorMessage());
                              } else {
                                  mPayFunctionSettingMainView.onQryPaymentInfoSuccess(
                                          mConversationId, mPaymentInfo);
                              }
                          }

                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onNext(PaymentInfo paymentInfo) {
                              mPayFunctionSettingMainView.onQryPaymentInfoSuccess(mConversationId,
                                      mPaymentInfo);
                          }
                      });
    }

    @Override
    public void qryContract(String eLanguage, String dealType) {
        PsnLoanPaymentSignContractQueryParams params = new PsnLoanPaymentSignContractQueryParams();
        params.setDealType(dealType);
        params.seteLanguage(eLanguage);
        mLoanService.psnLoanPaymentSignContractQuery(params)
                    .compose(this.<PsnLoanPaymentSignContractQueryResult>bindToLifecycle())
                    .compose(
                            SchedulersCompat.<PsnLoanPaymentSignContractQueryResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnLoanPaymentSignContractQueryResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(
                                PsnLoanPaymentSignContractQueryResult psnLoanPaymentSignContractQueryResult) {
                            mPayFunctionSettingMainView.onQryContractSuccess(
                                    psnLoanPaymentSignContractQueryResult.getLoanContract());
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
                              SecurityFactorModel securityFactorModel =
                                      ModelUtil.generateSecurityFactorModel(
                                              psnGetSecurityFactorResult);
                              mPayFunctionSettingMainView.onQrySecurityFactorSuccess(
                                      securityFactorModel);
                          }
                      });
    }

    @Override
    public void verifySign(PaymentSignViewModel paymentSignViewModel) {
        PsnLoanPaymentSignVerifyParams params =
                BeanConvertor.toBean(paymentSignViewModel, new PsnLoanPaymentSignVerifyParams());
        mLoanService.psnLoanPaymentSignVerify(params)
                    .compose(this.<PsnLoanPaymentSignVerifyResult>bindToLifecycle())
                    .map(new Func1<PsnLoanPaymentSignVerifyResult, VerifyBean>() {
                        @Override
                        public VerifyBean call(
                                PsnLoanPaymentSignVerifyResult psnLoanPaymentSignVerifyResult) {
                            return BeanConvertor.toBean(psnLoanPaymentSignVerifyResult,
                                    new VerifyBean());
                        }
                    })
                    .compose(SchedulersCompat.<VerifyBean>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<VerifyBean>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(VerifyBean verifyBean) {
                            mPayFunctionSettingMainView.onVerifySignSuccess(verifyBean);
                        }
                    });
    }

    @Override
    public void verifyUnSign(PaymentSignViewModel paymentSignViewModel) {
        PsnLoanPaymentUnsignVerifyParams params = new PsnLoanPaymentUnsignVerifyParams();
        params.set_combinId(paymentSignViewModel.get_combinId());
        params.setConversationId(paymentSignViewModel.getConversationId());
        params.setQuoteFlag(paymentSignViewModel.getQuoteFlag());
        params.setQuoteOrActNo(paymentSignViewModel.getQuoteOrActNo());
        params.setSignAccountNum(paymentSignViewModel.getSignAccountNum());
        mLoanService.psnLoanPaymentUnsignVerify(params)
                    .compose(this.<PsnLoanPaymentUnsignVerifyResult>bindToLifecycle())
                    .map(new Func1<PsnLoanPaymentUnsignVerifyResult, VerifyBean>() {

                        @Override
                        public VerifyBean call(
                                PsnLoanPaymentUnsignVerifyResult psnLoanPaymentUnsignVerifyResult) {
                            return BeanConvertor.toBean(psnLoanPaymentUnsignVerifyResult,
                                    new VerifyBean());
                        }
                    })
                    .compose(SchedulersCompat.<VerifyBean>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<VerifyBean>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(VerifyBean verifyBean) {
                            mPayFunctionSettingMainView.onVerifyUnSignSuccess(verifyBean);
                        }
                    });
    }

    @Override
    public void verifyChange(PaymentSignViewModel paymentSignViewModel) {
        PsnLOANChangeSignedVerifyParams params =
                BeanConvertor.toBean(paymentSignViewModel, new PsnLOANChangeSignedVerifyParams());
        mLoanService.psnLOANChangeSignedVerify(params)
                    .compose(this.<PsnLOANChangeSignedVerifyResult>bindToLifecycle())
                    .map(new Func1<PsnLOANChangeSignedVerifyResult, VerifyBean>() {
                        @Override
                        public VerifyBean call(
                                PsnLOANChangeSignedVerifyResult psnLOANChangeSignedVerifyResult) {
                            return BeanConvertor.toBean(psnLOANChangeSignedVerifyResult,
                                    new VerifyBean());
                        }
                    })
                    .compose(SchedulersCompat.<VerifyBean>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<VerifyBean>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(VerifyBean verifyBean) {
                            mPayFunctionSettingMainView.onVerifyChangeSuccess(verifyBean);
                        }
                    });
    }
}