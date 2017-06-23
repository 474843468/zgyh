package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter;

import android.support.annotation.NonNull;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignSubmit.PsnLoanPaymentSignSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import rx.Observable;
import rx.functions.Func1;

public class PaymentSignConfirmPresenter
        extends BaseConfirmPresenter<PaymentSignViewModel, String> {

    public PaymentSignConfirmPresenter(BaseConfirmContract.View<String> view) {
        super(view);
    }

    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(PaymentSignViewModel fillInfo) {
        PsnLoanPaymentSignVerifyParams params =
                BeanConvertor.toBean(fillInfo, new PsnLoanPaymentSignVerifyParams());
        return mLoanService.psnLoanPaymentSignVerify(params)
                           .map(new Func1<PsnLoanPaymentSignVerifyResult, VerifyBean>() {
                               @Override
                               public VerifyBean call(
                                       PsnLoanPaymentSignVerifyResult psnLoanPaymentSignVerifyResult) {
                                   return BeanConvertor.toBean(psnLoanPaymentSignVerifyResult,
                                           new VerifyBean());
                               }
                           });
    }

    @Override
    public void submit(PaymentSignViewModel fillInfo, BaseSubmitBean submitBean) {
        PsnLoanPaymentSignSubmitParams params = new PsnLoanPaymentSignSubmitParams();
        params = BeanConvertor.toBean(fillInfo, params);
        params = BeanConvertor.toBean(submitBean, params);
        mLoanService.psnLoanPaymentSignSubmit(params)
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