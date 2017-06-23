package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter;

import android.support.annotation.NonNull;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedSubmit.PsnLOANChangeSignedSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedVerify.PsnLOANChangeSignedVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedVerify.PsnLOANChangeSignedVerifyResult;
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

public class PaymentSignChangeConfirmPresenter
        extends BaseConfirmPresenter<PaymentSignViewModel, String> {

    public PaymentSignChangeConfirmPresenter(BaseConfirmContract.View<String> view) {
        super(view);
    }

    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(PaymentSignViewModel fillInfo) {
        PsnLOANChangeSignedVerifyParams params =
                BeanConvertor.toBean(fillInfo, new PsnLOANChangeSignedVerifyParams());
        return mLoanService.psnLOANChangeSignedVerify(params)
                           .map(new Func1<PsnLOANChangeSignedVerifyResult, VerifyBean>() {
                               @Override
                               public VerifyBean call(
                                       PsnLOANChangeSignedVerifyResult psnLOANChangeSignedVerifyResult) {
                                   return BeanConvertor.toBean(psnLOANChangeSignedVerifyResult,
                                           new VerifyBean());
                               }
                           });
    }

    @Override
    public void submit(PaymentSignViewModel fillInfo, BaseSubmitBean submitBean) {
        PsnLOANChangeSignedSubmitParams params =
                BeanConvertor.toBean(fillInfo, new PsnLOANChangeSignedSubmitParams());
        params = BeanConvertor.toBean(submitBean, params);
        mLoanService.psnLOANChangeSignedSubmit(params)
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