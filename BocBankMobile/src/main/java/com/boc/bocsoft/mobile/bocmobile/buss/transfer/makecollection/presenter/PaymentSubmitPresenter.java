package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnTransActPaymentOrderDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnTransActPaymentOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit.PsnTransActPaymentSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit.PsnTransActPaymentSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify.PsnTransActPaymentVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify.PsnTransActPaymentVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PaymentSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.PaymentSubmitContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：主动收款付款
 * Created by zhx on 2016/7/7
 */
public class PaymentSubmitPresenter implements PaymentSubmitContract.Presenter {
    PaymentSubmitContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private TransferService transferService;

    // 不知道该说啥
    private String _combinId = null;
    private String conversationId = null;
    private String token = null;

    public PaymentSubmitPresenter(PaymentSubmitContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        transferService = new TransferService();
    }

    /**
     * 主动收款付款
     * 接口调用顺序：PsnTransActPaymentOrderDetail、PSNCreatConversation、PsnGetSecurityFactor、PsnTransActPaymentVerify、PSNGetTokenId、PsnTransActPaymentSubmit
     *
     * @param paymentSubmitViewModel
     */
    @Override
    public void paymentSubmit(final PaymentSubmitViewModel paymentSubmitViewModel) {

        PsnTransActPaymentOrderDetailParams params = new PsnTransActPaymentOrderDetailParams();
        params.setNotifyId(paymentSubmitViewModel.getNotifyId());
        transferService.psnTransActPaymentOrderDetail(params)
                .compose(mRxLifecycleManager.<PsnTransActPaymentOrderDetailResult>bindToLifecycle())
                .flatMap(new Func1<PsnTransActPaymentOrderDetailResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnTransActPaymentOrderDetailResult psnTransActPaymentOrderDetailResult) {
                        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
                        return globalService.psnCreatConversation(psnCreatConversationParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversationId) {
                        // 给conversationId字段赋值，以便后面使用
                        PaymentSubmitPresenter.this.conversationId = conversationId;

                        PsnGetSecurityFactorParams factorParams = new PsnGetSecurityFactorParams();
                        factorParams.setConversationId(conversationId);
                        factorParams.setServiceId(null); // TODO ServiceId哪里获取
                        return globalService.psnGetSecurityFactor(factorParams);
                    }
                })
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnTransActPaymentVerifyResult>>() {
                    @Override
                    public Observable<PsnTransActPaymentVerifyResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        // 给_combinId字段赋值，以便后面使用
                        _combinId = psnGetSecurityFactorResult.get_combinList().get(0).getId(); // TODO 这样获取是否正确？

                        return transferService.psnTransActPaymentVerify(buildPaymentVerifyParams(paymentSubmitViewModel));
                    }
                })
                .flatMap(new Func1<PsnTransActPaymentVerifyResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnTransActPaymentVerifyResult psnGetSecurityFactorResult) {
                        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
                        getTokenIdParams.setConversationId(PaymentSubmitPresenter.this.conversationId);
                        return globalService.psnGetTokenId(getTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransActPaymentSubmitResult>>() {
                    @Override
                    public Observable<PsnTransActPaymentSubmitResult> call(String token) {
                        // 给token字段赋值，以便后面使用
                        PaymentSubmitPresenter.this.token = token;

                        return transferService.psnTransActPaymentSubmit(buildPaymentSubmitParams(paymentSubmitViewModel));
                    }
                })
                .compose(SchedulersCompat.<PsnTransActPaymentSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActPaymentSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActPaymentSubmitResult psnTransActPaymentSubmitResult) {
                    }
                });


    }

    /**
     * 封装请求参数：
     * PaymentVerify交易校验
     *
     * @param paymentSubmitViewModel
     * @return PsnTransActPaymentVerifyParams
     */
    private PsnTransActPaymentVerifyParams buildPaymentVerifyParams(PaymentSubmitViewModel paymentSubmitViewModel) {
        PsnTransActPaymentVerifyParams paymentVerifyParams = new PsnTransActPaymentVerifyParams();
        paymentVerifyParams.setFromAccountId(paymentSubmitViewModel.getFromAccountId());
        paymentVerifyParams.setNotifyTrfCur(paymentSubmitViewModel.getNotifyTrfCur());
        paymentVerifyParams.setNotifyTrfAmount(paymentSubmitViewModel.getNotifyTrfAmount());
        paymentVerifyParams.setPayeeName(paymentSubmitViewModel.getPayeeName());
        paymentVerifyParams.setPayeeActno(paymentSubmitViewModel.getPayeeActno());
        paymentVerifyParams.set_combinId(_combinId); // TODO _combinId的获取方式是否正确？
        paymentVerifyParams.setNotifyId(paymentSubmitViewModel.getNotifyId());
        paymentVerifyParams.setNotifyCreateDate(paymentSubmitViewModel.getNotifyCreateDate()); // TODO 到LocalDateTime怎么转换呢？
        paymentVerifyParams.setNotifyCurrentDate(paymentSubmitViewModel.getNotifyCurrentDate());
        paymentVerifyParams.setNotifyRequestAmount(paymentSubmitViewModel.getNotifyRequestAmount());
        paymentVerifyParams.setPayeeMobile(paymentSubmitViewModel.getPayeeMobile());
        paymentVerifyParams.setPayerName(paymentSubmitViewModel.getPayerName());
        paymentVerifyParams.setPayerMobile(paymentSubmitViewModel.getPayerMobile());
        paymentVerifyParams.setPayerCustId(paymentSubmitViewModel.getPayerCustId());
        paymentVerifyParams.setNotifyCreateChannel(paymentSubmitViewModel.getNotifyCreateChannel());
        return paymentVerifyParams;
    }

    /**
     * 封装请求参数：
     * PaymentVerify交易校验
     *
     * @param paymentSubmitViewModel
     * @return PsnTransActPaymentVerifyParams
     */
    private PsnTransActPaymentSubmitParams buildPaymentSubmitParams(PaymentSubmitViewModel paymentSubmitViewModel) {
        PsnTransActPaymentSubmitParams psnTransActPaymentSubmitParams = new PsnTransActPaymentSubmitParams();

        psnTransActPaymentSubmitParams.setFromAccountId(paymentSubmitViewModel.getFromAccountId());
        psnTransActPaymentSubmitParams.setNotifyId(paymentSubmitViewModel.getNotifyId());
        psnTransActPaymentSubmitParams.setNotifyTrfCur(paymentSubmitViewModel.getNotifyTrfCur());
        psnTransActPaymentSubmitParams.setNotifyTrfAmount(paymentSubmitViewModel.getNotifyTrfAmount());
        psnTransActPaymentSubmitParams.setToken(PaymentSubmitPresenter.this.token); // ----- //
        psnTransActPaymentSubmitParams.set_signedData(paymentSubmitViewModel.get_signedData()); // ----- //
        psnTransActPaymentSubmitParams.setNotifyCreateDate(paymentSubmitViewModel.getNotifyCreateDate());
        psnTransActPaymentSubmitParams.setNotifyCurrentDate(paymentSubmitViewModel.getNotifyCurrentDate());
        psnTransActPaymentSubmitParams.setPayeeActno(paymentSubmitViewModel.getPayeeActno());
        psnTransActPaymentSubmitParams.setNotifyRequestAmount(paymentSubmitViewModel.getNotifyRequestAmount());
        psnTransActPaymentSubmitParams.setPayeeMobile(paymentSubmitViewModel.getPayeeMobile());
        psnTransActPaymentSubmitParams.setPayerName(paymentSubmitViewModel.getPayerName());
        psnTransActPaymentSubmitParams.setPayerMobile(paymentSubmitViewModel.getPayerMobile());
        psnTransActPaymentSubmitParams.setPayerCustId(paymentSubmitViewModel.getPayerCustId());
        psnTransActPaymentSubmitParams.setNotifyCreateChannel(paymentSubmitViewModel.getNotifyCreateChannel());
        psnTransActPaymentSubmitParams.setSmc(paymentSubmitViewModel.getSmc());
        psnTransActPaymentSubmitParams.setOtp(paymentSubmitViewModel.getOtp());

        return psnTransActPaymentSubmitParams;
    }

    @Override
    public void subscribe() {
        //TODO onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        //TODO 防止外界已经销毁，而后台线程的任务还在执行
    }
}
