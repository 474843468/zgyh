package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoScannedPayment.QRPayDoScannedPaymentParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoScannedPayment.QRPayDoScannedPaymentResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo.QRPayGetConfirmInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo.QRPayGetConfirmInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode.QRPayGetQRCodeParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode.QRPayGetQRCodeResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransInfo.QRPayGetTransInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransInfo.QRPayGetTransInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.service.QrCodePayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.ui.QRPayCodeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 反扫支付 BII通信逻辑处理
 * Created by wangf on 2016/8/29.
 */
public class QRPayCodePresenter implements QRPayCodeContract.Presenter {

    private QRPayCodeContract.QrCodeView mQrCodeView;
    private QRPayCodeContract.GetQrCodeView mGetQrCodeView;
    private RxLifecycleManager mRxLifecycleManager;

    /** 公共service */
    private GlobalService globalService;
    /** 二维码支付的service */
    private QrCodePayService qrCodePayService;


    // 获取二维码的会话ID
//    private String qRCodeConversationID = "";

    public QRPayCodePresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        qrCodePayService = new QrCodePayService();
    }

    public QRPayCodePresenter(QRPayCodeContract.QrCodeView view) {
        this();
        mQrCodeView = view;
    }

    public QRPayCodePresenter(QRPayCodeContract.GetQrCodeView view) {
        this();
        mGetQrCodeView = view;
    }


    /**
     * 获取二维码
     *
     * @param accSeq
     */
    @Override
    public void loadQRPayGetQRCode(final String accSeq) {
        //获取会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        QRPayMainFragment.qRCodeConversationID = conversationId;
                        // 查询TokenID
                        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
                        getTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(getTokenIdParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, QRPayGetQRCodeParams>() {
                    @Override
                    public QRPayGetQRCodeParams call(String s) {
                        QRPayGetQRCodeParams qrPayGetQRCodeParams = new QRPayGetQRCodeParams();
                        qrPayGetQRCodeParams.setConversationId(QRPayMainFragment.qRCodeConversationID);
                        qrPayGetQRCodeParams.setToken(s);
                        qrPayGetQRCodeParams.setType("01");//支付
                        qrPayGetQRCodeParams.setActSeq(accSeq);
                        return qrPayGetQRCodeParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<QRPayGetQRCodeParams, Observable<QRPayGetQRCodeResult>>() {
                    @Override
                    public Observable<QRPayGetQRCodeResult> call(QRPayGetQRCodeParams qrPayGetQRCodeParams) {
                        // 获取二维码
                        return qrCodePayService.qRPayGetQRCode(qrPayGetQRCodeParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<QRPayGetQRCodeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mGetQrCodeView.loadQRPayGetQRCodeFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetQRCodeResult qrPayGetQRCodeResult) {
                        mGetQrCodeView.loadQRPayGetQRCodeSuccess(QrCodeModelUtil.generateGetQrCodeViewModel(qrPayGetQRCodeResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }

    /**
     * 查询反扫后的交易确认通知
     */
    @Override
    public void loadQRPayGetConfirmInfo() {
        final QRPayGetConfirmInfoParams params = new QRPayGetConfirmInfoParams();
        params.setConversationId(QRPayMainFragment.qRCodeConversationID);
        qrCodePayService.qRPayGetConfirmInfo(params)
                .compose(mRxLifecycleManager.<QRPayGetConfirmInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<QRPayGetConfirmInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetConfirmInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrCodeView.loadQRPayGetConfirmInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetConfirmInfoResult infoResult) {

                        mQrCodeView.loadQRPayGetConfirmInfoSuccess(QrCodeModelUtil.generateGetConfirmInfoViewModel(infoResult, params));
                    }
                    
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }


    /***
     * 反扫支付
     */
    @Override
    public void loadQRPayDoScannedPayment(final String password, final String password_RC, final String confirmInfoConversationID) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(confirmInfoConversationID);
//        psnGetTokenIdParams.setConversationId(QRPayMainFragment.qRCodeConversationID);
        // 查询TokenID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayDoScannedPaymentResult>>() {
                    @Override
                    public Observable<QRPayDoScannedPaymentResult> call(String tokenID) {
                        QRPayDoScannedPaymentParams params = new QRPayDoScannedPaymentParams();
                        params.setConversationId(confirmInfoConversationID);
//                        params.setConversationId(QRPayMainFragment.qRCodeConversationID);
                        params.setToken(tokenID);
                        params.setPassword(password);
                        params.setPassword_RC(password_RC);
                        params.setPassType("01");
                        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
                        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
                        return qrCodePayService.qRPayDoScannedPayment(params);
                    }
                }).compose(SchedulersCompat.<QRPayDoScannedPaymentResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayDoScannedPaymentResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrCodeView.loadQRPayDoScannedPaymentFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayDoScannedPaymentResult result) {
                        mQrCodeView.loadQRPayDoScannedPaymentSuccess();
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }

    /*** 获取随机数 */
    @Override
    public void loadGetRandom() {
        PSNGetRandomParams params = new PSNGetRandomParams();
        params.setConversationId(QRPayMainFragment.qRCodeConversationID);
        //获取随机数
        globalService.psnGetRandom(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrCodeView.loadGetRandomFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String random) {
                        mQrCodeView.loadGetRandomSuccess(random);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });

    }

    /*** 查询反扫支付交易信息 */
    @Override
    public void loadQRPayGetTransInfo(String settleKey) {
        QRPayGetTransInfoParams params = new QRPayGetTransInfoParams();
        params.setConversationId(QRPayMainFragment.qRCodeConversationID);
        params.setSettleKey(settleKey);
        qrCodePayService.qRPayGetTransInfo(params)
                .compose(mRxLifecycleManager.<QRPayGetTransInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<QRPayGetTransInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetTransInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrCodeView.loadQRPayGetTransInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetTransInfoResult infoResult) {
                        mQrCodeView.loadQRPayGetTransInfoSuccess(QrCodeModelUtil.generateGetTransInfoViewModel(infoResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
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
