package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer.QRPayDoTransferParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer.QRPayDoTransferResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult.QRPayGetPayeeResultParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult.QRPayGetPayeeResultResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode.QRPayGetQRCodeParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode.QRPayGetQRCodeResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.service.QrCodePayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui.QRCollectionCodeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui.QRPayDoPaymentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fanbin on 16/9/29.
 */
public class QRCollectionCodePresenter implements QRCollectionCodeContract.Presenter {
    private QRCollectionCodeContract.QrCollectionCodeView mQrCodeView;
    private QRCollectionCodeContract.GetQrCollectionCodeView mGetQrCollectionCodeView;
    private QRCollectionCodeContract.QrTransCodeView mQrTransCodeView;
    private RxLifecycleManager mRxLifecycleManager;
    /***
     * 公共service
     */
    private GlobalService globalService;
    /***
     * 二维码收款service
     */
    private QrCodePayService qrCodePayService;
    private boolean noFirst;
    public QRCollectionCodePresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        qrCodePayService = new QrCodePayService();
    }

    public QRCollectionCodePresenter(QRCollectionCodeContract.QrCollectionCodeView qrCodeView) {
        this();
        mQrCodeView = qrCodeView;
    }

    public QRCollectionCodePresenter(QRCollectionCodeContract.GetQrCollectionCodeView getQrCollectionCodeView) {
        this();
        mGetQrCollectionCodeView = getQrCollectionCodeView;
    }

    public QRCollectionCodePresenter(QRCollectionCodeContract.QrTransCodeView qrTransCodeView) {
        this();
        mQrTransCodeView = qrTransCodeView;
    }


    @Override
    public void loadQRPayGetQRCode(final String accSeq,final String amount,final String payeeComments) {
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
                        //接口规定 01：支付   02：转账
                        qrPayGetQRCodeParams.setType("02");
                        qrPayGetQRCodeParams.setConversationId(QRPayMainFragment.qRCodeConversationID);
                        qrPayGetQRCodeParams.setToken(s);
                        qrPayGetQRCodeParams.setActSeq(accSeq);
                        qrPayGetQRCodeParams.setAmount(amount);
                        qrPayGetQRCodeParams.setPayeeComments(payeeComments);
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
                        mGetQrCollectionCodeView.loadQRCollectionGetQRCodeFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetQRCodeResult qrPayGetQRCodeResult) {
                        mGetQrCollectionCodeView.loadQRCollectionGetQRCodeSuccess(QrCodeModelUtil.generateGetQrCodeViewModel(qrPayGetQRCodeResult));
                    }
                });

    }

    //收款结果查询
    @Override
    public void loadQRPayGetTransRecord(final String qrNo) {
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
                }).compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, QRPayGetPayeeResultParams>() {
                    @Override
                    public QRPayGetPayeeResultParams call(String s) {
                        QRPayGetPayeeResultParams qrPayGetPayeeResultParams = new QRPayGetPayeeResultParams();
                        qrPayGetPayeeResultParams.setConversationId(QRPayMainFragment.qRCodeConversationID);
                        qrPayGetPayeeResultParams.setQrNo(qrNo);
                        return qrPayGetPayeeResultParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<QRPayGetPayeeResultParams, Observable<QRPayGetPayeeResultResult>>() {
                    @Override
                    public Observable<QRPayGetPayeeResultResult> call(QRPayGetPayeeResultParams qrPayGetPayeeResultParams) {
                        // 收款结果查询
                        return qrCodePayService.qRPayGetPayeeResult(qrPayGetPayeeResultParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<QRPayGetPayeeResultResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrCodeView.loadQRPayGetTransRecordFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetPayeeResultResult qrPayGetPayeeResultResult) {
                        mQrCodeView.loadQRPayGetTransRecordSuccess(qrPayGetPayeeResultResult);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //重写父类方法,主要是为了不弹出错误对话框
                        if (!noFirst){
                            BaseMobileActivity curActivity =
                                    (BaseMobileActivity) ActivityManager.getAppManager().currentActivity();
                            curActivity.showErrorDialog(biiResultErrorException.getErrorMessage());
                            noFirst=true;
                        }

                    }

                });

    }

    //转账交易
    @Override
    public void loadQRPayDoTransfer(final String actSeq, final String password, final String password_RC, final String tranAmount, final String qrNo, final String payerComments, final String payeeAccNo, final String payeeName) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(QRPayDoPaymentFragment.conversationId);
        // 查询TokenID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayDoTransferResult>>() {
                    @Override
                    public Observable<QRPayDoTransferResult> call(String tokenID) {
                        QRPayDoTransferParams params = new QRPayDoTransferParams();
                        params.setConversationId(QRPayDoPaymentFragment.conversationId);
                        params.setToken(tokenID);
                        params.setPassword(password);
                        params.setPassword_RC(password_RC);
                        params.setActSeq(actSeq);
                        params.setTranAmount(tranAmount);
                        params.setQrNo(qrNo);
                        params.setPayerComments(payerComments);
                        params.setPayeeAccNo(payeeAccNo);
                        params.setPayeeName(payeeName);
                        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
                        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
                        params.setPassType("01");
                        return qrCodePayService.qRPayDoTransfer(params);
                    }
                }).compose(SchedulersCompat.<QRPayDoTransferResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayDoTransferResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrTransCodeView.loadQRPayDoTransferFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayDoTransferResult qrPayDoTransferResult) {
                        mQrTransCodeView.loadQRPayDoTransferSuccess(qrPayDoTransferResult);
                    }


                });
    }

    @Override
    public void loadGetRandom() {

        PSNGetRandomParams params = new PSNGetRandomParams();
        params.setConversationId(QRPayDoPaymentFragment.conversationId);
        //获取随机数
        globalService.psnGetRandom(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrTransCodeView.loadGetRandomFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String random) {
                        mQrTransCodeView.loadGetRandomSuccess(random);
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
