package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayChangePass.QRPayChangePassParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayChangePass.QRPayChangePassResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPass.QRPaySetPassParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPass.QRPaySetPassResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPassPre.QRPaySetPassPreParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPassPre.QRPaySetPassPreResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.service.QrCodePayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayChangePwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayResetPwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPaySetPwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayPwdContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 密码相关 BII通信逻辑处理
 * Created by wangf on 2016/9/3.
 */
public class QRPayPwdPresenter implements QRPayPwdContract.QrPayPwdPresenter{

    private RxLifecycleManager mRxLifecycleManager;

    /** 公共service */
    private GlobalService globalService;

    /** 二维码支付的service */
    private QrCodePayService qrCodePayService;

    private QRPayPwdContract.QrPaySecurityFactorView mSecurityFactorView;
    private QRPayPwdContract.QrPaySetPwdView mPaySetPwdView;
    private QRPayPwdContract.QrPayChangePwdView mPayChangePwdView;
    private QRPayPwdContract.QrPayResetPwdView mPayResetPwdView;

    // 会话ID
    public static String conversationID = "";
    // 随机数ID
    public static String randomID = "";
    // 防重令牌
    public static String tokenID = "";


    public QRPayPwdPresenter(){
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        qrCodePayService = new QrCodePayService();
    }

    public QRPayPwdPresenter(QRPayPwdContract.QrPaySecurityFactorView securityFactorView){
        this();
        mSecurityFactorView = securityFactorView;
    }

    public QRPayPwdPresenter(QRPayPwdContract.QrPaySetPwdView qrPaySetPwdView){
        this();
        mPaySetPwdView = qrPaySetPwdView;
    }

    public QRPayPwdPresenter(QRPayPwdContract.QrPayChangePwdView payChangePwdView){
        this();
        mPayChangePwdView = payChangePwdView;
    }

    public QRPayPwdPresenter(QRPayPwdContract.QrPayResetPwdView qrPayResetPwdView){
        this();
        mPayResetPwdView = qrPayResetPwdView;
    }


    /*** 查询安全因子 */
    @Override
    public void loadPwdSecurityFactor() {
        //重置数据
        conversationID = "";
        randomID = "";
        //创建会话
        PSNCreatConversationParams conversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        conversationID = s;
                        //查询随机数
                        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
                        psnGetRandomParams.setConversationId(s);
                        return globalService.psnGetRandom(psnGetRandomParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, PsnGetSecurityFactorParams>() {
                    @Override
                    public PsnGetSecurityFactorParams call(String random) {
                        randomID = random;
                        //查询安全因子
                        PsnGetSecurityFactorParams securityFactorParams = new PsnGetSecurityFactorParams();
                        securityFactorParams.setConversationId(conversationID);
                        securityFactorParams.setServiceId(ServiceIdCodeConst.SERVICE_ID_QRPAY_SETPASS);
                        return securityFactorParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnGetSecurityFactorParams, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(PsnGetSecurityFactorParams psnGetSecurityFactorParams) {
                        return globalService.psnGetSecurityFactor(psnGetSecurityFactorParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mSecurityFactorView.loadPwdSecurityFactorFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        mSecurityFactorView.loadPwdSecurityFactorSuccess(QrCodeModelUtil.generateSecurityFactorViewModel(psnGetSecurityFactorResult));
                    }
                });
    }

    /*** 设置支付密码预交易 */
    @Override
    public void loadQRPaySetPassPre(QRPaySetPwdViewModel pwdViewModel) {
        //封装设置支付密码预交易的请求参数
        QRPaySetPassPreParams passPreParams = QrCodeModelUtil.buildSetPayPwdPreBiiParams(pwdViewModel);
        passPreParams.setConversationId(conversationID);
        //设置支付密码预交易
        qrCodePayService.qRPaySetPassPre(passPreParams)
                .compose(mRxLifecycleManager.<QRPaySetPassPreResult>bindToLifecycle())
                .compose(SchedulersCompat.<QRPaySetPassPreResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPaySetPassPreResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPaySetPwdView.loadQRPaySetPassPreFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPaySetPassPreResult qrPaySetPassPreResult) {
                        mPaySetPwdView.loadQRPaySetPassPreSuccess(QrCodeModelUtil.generateSetPwdPreViewModel(qrPaySetPassPreResult));
                    }
                });

    }

    /*** 设置支付密码 */
    @Override
    public void loadQRPaySetPass(final QRPaySetPwdViewModel payPwdViewModel) {

        //获取Token
        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
        getTokenIdParams.setConversationId(conversationID);
        globalService.psnGetTokenId(getTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPaySetPassResult>>() {
                    @Override
                    public Observable<QRPaySetPassResult> call(String tokenID) {
                        QRPaySetPassParams params = QrCodeModelUtil.buildSetPayPwdBiiParams(payPwdViewModel);
                        params.setToken(tokenID);// tokenID
                        params.setConversationId(conversationID);//会话id
                        //设置支付密码
                        return qrCodePayService.qRPaySetPass(params);
                    }
                })
                .compose(SchedulersCompat.<QRPaySetPassResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPaySetPassResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPaySetPwdView.loadQRPaySetPassFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPaySetPassResult result) {
                        mPaySetPwdView.loadQRPaySetPassSuccess();
                    }
                });

    }

    /*** 修改支付密码预交易 */
//    @Override
//    public void loadQRPayChangePassPre(final QRPayChangePwdViewModel pwdViewModel) {
//        //获取Token
//        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
//        getTokenIdParams.setConversationId(conversationID);
//        globalService.psnGetTokenId(getTokenIdParams)
//                .compose(mRxLifecycleManager.<String>bindToLifecycle())
//                .flatMap(new Func1<String, Observable<QRPayChangePassPreResult>>() {
//                    @Override
//                    public Observable<QRPayChangePassPreResult> call(String token) {
//                        tokenID = token;
//                        QRPayChangePassPreParams params = QrCodeModelUtil.buildChangePayPwdPreBiiParams(pwdViewModel);
//                        params.setToken(token);// tokenID
//                        params.setConversationId(conversationID);//会话id
//                        //修改支付密码预交易
//                        return qrCodePayService.qRPayChangePassPre(params);
//                    }
//                })
//                .compose(SchedulersCompat.<QRPayChangePassPreResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<QRPayChangePassPreResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mPayChangePwdView.loadQRPayChangePassPreFail(biiResultErrorException);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onNext(QRPayChangePassPreResult result) {
//                        mPayChangePwdView.loadQRPayChangePassPreSuccess();
//                    }
//                });
//    }

    /*** 修改支付密码 */
    @Override
    public void loadQRPayChangePass(final QRPayChangePwdViewModel pwdViewModel) {
        //获取Token
        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
        getTokenIdParams.setConversationId(conversationID);
        globalService.psnGetTokenId(getTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayChangePassResult>>() {
                    @Override
                    public Observable<QRPayChangePassResult> call(String tokenID) {
                        //封装修改支付密码的请求参数
                        QRPayChangePassParams passParams = QrCodeModelUtil.buildChangePayPwdBiiParams(pwdViewModel);
                        passParams.setConversationId(conversationID);
                        passParams.setToken(tokenID);
                        //修改支付密码
                        return qrCodePayService.qRPayChangePass(passParams);
                    }
                })
                .compose(SchedulersCompat.<QRPayChangePassResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayChangePassResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPayChangePwdView.loadQRPayChangePassFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayChangePassResult result) {
                        mPayChangePwdView.loadQRPayChangePassSuccess();
                    }
                });
    }

    /*** 重置支付密码预交易 */
    @Override
    public void loadQRPayResetPassPre(QRPayResetPwdViewModel resetPwdViewModel) {
//        //封装重置支付密码预交易的请求参数
//        QRPayResetPassPreParams passPreParams = QrCodeModelUtil.buildResetPayPwdPreBiiParams(resetPwdViewModel);
//        passPreParams.setConversationId(conversationID);
//        //重置支付密码预交易
//        qrCodePayService.qRPayResetPassPre(passPreParams)
//                .compose(mRxLifecycleManager.<QRPayResetPassPreResult>bindToLifecycle())
//                .compose(SchedulersCompat.<QRPayResetPassPreResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<QRPayResetPassPreResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mPayResetPwdView.loadQRPayResetPassPreFail(biiResultErrorException);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onNext(QRPayResetPassPreResult qrPayResetPassPreResult) {
//                        mPayResetPwdView.loadQRPayResetPassPreSuccess(QrCodeModelUtil.generateResetPwdPreViewModel(qrPayResetPassPreResult));
//                    }
//                });
    }

    /*** 重置支付密码 */
    @Override
    public void loadQRPayResetPass(final QRPayResetPwdViewModel resetPwdViewModel) {
//        //获取Token
//        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
//        getTokenIdParams.setConversationId(conversationID);
//        globalService.psnGetTokenId(getTokenIdParams)
//                .compose(mRxLifecycleManager.<String>bindToLifecycle())
//                .flatMap(new Func1<String, Observable<QRPayResetPassResult>>() {
//                    @Override
//                    public Observable<QRPayResetPassResult> call(String tokenID) {
//                        QRPayResetPassParams params = QrCodeModelUtil.buildResetPayPwdBiiParams(resetPwdViewModel);
//                        params.setToken(tokenID);// tokenID
//                        params.setConversationId(conversationID);//会话id
//                        //重置支付密码
//                        return qrCodePayService.qRPayResetPass(params);
//                    }
//                })
//                .compose(SchedulersCompat.<QRPayResetPassResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<QRPayResetPassResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mPayResetPwdView.loadQRPayResetPassFail(biiResultErrorException);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onNext(QRPayResetPassResult result) {
//                        mPayResetPwdView.loadQRPayResetPassSuccess();
//                    }
//                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }
}
