package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter;

import android.support.annotation.NonNull;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignSubmit.PsnLoanPaymentUnsignSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify.PsnLoanPaymentUnsignVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify.PsnLoanPaymentUnsignVerifyResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import rx.Observable;
import rx.functions.Func1;

public class PaymentUnSignConfirmPresenter
        extends BaseConfirmPresenter<PaymentSignViewModel, String> {

    public PaymentUnSignConfirmPresenter(BaseConfirmContract.View<String> view) {
        super(view);
    }

    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(PaymentSignViewModel fillInfo) {
        PsnLoanPaymentUnsignVerifyParams params =
                BeanConvertor.toBean(fillInfo, new PsnLoanPaymentUnsignVerifyParams());
        return mLoanService.psnLoanPaymentUnsignVerify(params)
                           .map(new Func1<PsnLoanPaymentUnsignVerifyResult, VerifyBean>() {
                               @Override
                               public VerifyBean call(
                                       PsnLoanPaymentUnsignVerifyResult psnLoanPaymentUnsignVerifyResult) {
                                   return BeanConvertor.toBean(psnLoanPaymentUnsignVerifyResult,
                                           new VerifyBean());
                               }
                           });
    }

    @Override
    public void submit(PaymentSignViewModel fillInfo, BaseSubmitBean submitBean) {
        PsnLoanPaymentUnsignSubmitParams params =
                BeanConvertor.toBean(fillInfo, new PsnLoanPaymentUnsignSubmitParams());
        params = BeanConvertor.toBean(submitBean, params);
        mLoanService.psnLoanPaymentUnsignSubmit(params)
                    .compose(this.<String>bindToLifecycle())
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
                        public void onNext(String result) {
                            mView.onSubmitSuccess(result);
                        }
                    });
    }
}