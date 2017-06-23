package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnTransActPaymentOrderDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnTransActPaymentOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PaymentOrderDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.PaymentOrderDetailContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

/**
 * Presenter：付款指令记录详情
 * Created by zhx on 2016/7/6
 */
public class PaymentOrderDetailPresenter implements PaymentOrderDetailContact.Presenter {
    PaymentOrderDetailContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;

    public PaymentOrderDetailPresenter(PaymentOrderDetailContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        transferService = new TransferService();
    }

    @Override
    public void paymentOrderDetail(PaymentOrderDetailViewModel paymentOrderDetailViewModel) {
        PsnTransActPaymentOrderDetailParams psnTransActPaymentOrderDetailParams = new PsnTransActPaymentOrderDetailParams();
        psnTransActPaymentOrderDetailParams.setNotifyId(paymentOrderDetailViewModel.getNotifyId());

        transferService.psnTransActPaymentOrderDetail(psnTransActPaymentOrderDetailParams)
                .compose(mRxLifecycleManager.<PsnTransActPaymentOrderDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransActPaymentOrderDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActPaymentOrderDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.paymentOrderDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActPaymentOrderDetailResult psnTransActPaymentOrderDetailResult) {
                        mView.paymentOrderDetailSuccess(copyResult2UIModel(psnTransActPaymentOrderDetailResult));
                    }
                });

    }

    private PaymentOrderDetailViewModel copyResult2UIModel(PsnTransActPaymentOrderDetailResult psnTransActPaymentOrderDetailResult) {
        PaymentOrderDetailViewModel paymentOrderDetailViewModel = new PaymentOrderDetailViewModel();

        paymentOrderDetailViewModel.setStatus(psnTransActPaymentOrderDetailResult.getStatus());
        paymentOrderDetailViewModel.setTransactionId(psnTransActPaymentOrderDetailResult.getTransactionId());
        paymentOrderDetailViewModel.setCreateDate(psnTransActPaymentOrderDetailResult.getCreateDate());
        paymentOrderDetailViewModel.setPayeeName(psnTransActPaymentOrderDetailResult.getPayeeName());
        paymentOrderDetailViewModel.setPayerName(psnTransActPaymentOrderDetailResult.getPayerName());
        paymentOrderDetailViewModel.setPayerAccountNumber(psnTransActPaymentOrderDetailResult.getPayerAccountNumber());
        paymentOrderDetailViewModel.setPaymentDate(psnTransActPaymentOrderDetailResult.getPaymentDate());
        paymentOrderDetailViewModel.setPayeeAccountNumber(psnTransActPaymentOrderDetailResult.getPayeeAccountNumber());
        paymentOrderDetailViewModel.setPayerCustomerId(psnTransActPaymentOrderDetailResult.getPayerCustomerId());
        paymentOrderDetailViewModel.setFurInfo(psnTransActPaymentOrderDetailResult.getFurInfo());
        paymentOrderDetailViewModel.setPayeeIbk(psnTransActPaymentOrderDetailResult.getPayeeIbk());
        paymentOrderDetailViewModel.setPayeeMobile(psnTransActPaymentOrderDetailResult.getPayeeMobile());
        paymentOrderDetailViewModel.setPayerMobile(psnTransActPaymentOrderDetailResult.getPayerMobile());
        paymentOrderDetailViewModel.setPayerIbknum(psnTransActPaymentOrderDetailResult.getPayerIbknum());
        paymentOrderDetailViewModel.setPayeeAccountType(psnTransActPaymentOrderDetailResult.getPayeeAccountType());
        paymentOrderDetailViewModel.setPayerAccountType(psnTransActPaymentOrderDetailResult.getPayerAccountType());
        paymentOrderDetailViewModel.setTrfAmount(psnTransActPaymentOrderDetailResult.getTrfAmount());
        paymentOrderDetailViewModel.setNotifyId(psnTransActPaymentOrderDetailResult.getNotifyId());
        paymentOrderDetailViewModel.setTrfCur(psnTransActPaymentOrderDetailResult.getTrfCur());
        paymentOrderDetailViewModel.setRequestAmount(psnTransActPaymentOrderDetailResult.getRequestAmount());
        paymentOrderDetailViewModel.setCreateChannel(psnTransActPaymentOrderDetailResult.getCreateChannel());
        paymentOrderDetailViewModel.setTrfChannel(psnTransActPaymentOrderDetailResult.getTrfChannel());

        return paymentOrderDetailViewModel;
    }

    @Override
    public void subscribe() {
        //TODO onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        //TODO 防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}
