package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui.PaymentSignContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.functions.Func1;

/**
 * 作者：XieDu
 * 创建时间：2016/9/6 9:19
 * 描述：
 */
public class PaymentSignPresenter extends RxPresenter implements PaymentSignContract.Presenter {

    PaymentSignContract.View mPaymentSignView;
    private String mConversationId;
    protected PsnLoanService mLoanService;
    protected GlobalService mGlobalService;

    public PaymentSignPresenter(PaymentSignContract.View view) {
        mPaymentSignView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    public void setConversationId(String conversationId) {
        mConversationId = conversationId;
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
                              mPaymentSignView.onQrySecurityFactorSuccess(securityFactorModel);
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
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(VerifyBean verifyBean) {
                        mPaymentSignView.onVerifySuccess(verifyBean);
                    }
                });
    }
}
