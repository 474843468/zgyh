package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord.QRPayGetTransRecordParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord.QRPayGetTransRecordResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.service.QrCodePayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.model.QRPayPaymentRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.ui.QRPayPaymentRecordContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * 二维码支付 - 交易记录 - BII通信逻辑处理
 * Created by wangf on 2016/9/8.
 */
public class QRPayPaymentRecordPresenter implements QRPayPaymentRecordContract.Presenter {

    private RxLifecycleManager mRxLifecycleManager;
    private QRPayPaymentRecordContract.PaymentRecordView mPaymentRecordView;

    /*** 公共service*/
    private GlobalService globalService;
    /*** 二维码支付service*/
    private QrCodePayService qrCodePayService;


    public QRPayPaymentRecordPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        qrCodePayService = new QrCodePayService();
    }

    public QRPayPaymentRecordPresenter(QRPayPaymentRecordContract.PaymentRecordView view) {
        this();
        mPaymentRecordView = view;
    }


    /**
     * 交易记录查询
     * @param paymentRecordViewModel
     */
    @Override
    public void queryPaymentRecordList(final QRPayPaymentRecordViewModel paymentRecordViewModel) {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayGetTransRecordResult>>() {
                    @Override
                    public Observable<QRPayGetTransRecordResult> call(String conversation) {
                        QRPayGetTransRecordParams transRecordParams = QrCodeModelUtil.buildPaymentRecordBiiParams(paymentRecordViewModel);
                        transRecordParams.setConversationId(conversation);
                        return qrCodePayService.qRPayGetTransRecord(transRecordParams);
                    }
                })
                .compose(SchedulersCompat.<QRPayGetTransRecordResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetTransRecordResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPaymentRecordView.queryPaymentRecordListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetTransRecordResult recordResult) {
                        mPaymentRecordView.queryPaymentRecordListSuccess(QrCodeModelUtil.generatePaymentRecordViewModel(recordResult));
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }
}
