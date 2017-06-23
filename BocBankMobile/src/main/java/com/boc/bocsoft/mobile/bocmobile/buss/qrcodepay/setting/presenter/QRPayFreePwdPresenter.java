package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayClosePassFreeService.QRPayClosePassFreeServiceParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayClosePassFreeService.QRPayClosePassFreeServiceResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo.QRPayGetPassFreeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo.QRPayGetPassFreeInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeService.QRPayOpenPassFreeServiceParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeService.QRPayOpenPassFreeServiceResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeServicePre.QRPayOpenPassFreeServicePreParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeServicePre.QRPayOpenPassFreeServicePreResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.service.QrCodePayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayFreePwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayFreePwdContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.common.utils.RxUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 小额免密 BII通信逻辑处理
 * Created by wangf on 2016/9/18.
 */
public class QRPayFreePwdPresenter extends RxPresenter implements QRPayFreePwdContract.QrCodeFreePwdPresenter{

    private RxLifecycleManager mRxLifecycleManager;

    /** 公共service */
    private GlobalService globalService;

    /** 二维码支付的service */
    private QrCodePayService qrCodePayService;

    private QRPayFreePwdContract.QrPaySecurityFactorView mSecurityFactorView;
    private QRPayFreePwdContract.QRPayQueryPassFreeInfoView mPassFreeInfoView;
    private QRPayFreePwdContract.QRPayFreePwdView mQrPayFreePwdView;

    // 会话ID
    public static String conversationID = "";
    // 随机数ID
    public static String randomID = "";
    // 防重令牌
    public static String tokenID = "";


    public QRPayFreePwdPresenter(){
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        qrCodePayService = new QrCodePayService();
    }

    public QRPayFreePwdPresenter(QRPayFreePwdContract.QrPaySecurityFactorView securityFactorView){
        this();
        mSecurityFactorView = securityFactorView;
    }

    public QRPayFreePwdPresenter(QRPayFreePwdContract.QRPayFreePwdView qrPayFreePwdView){
        this();
        mQrPayFreePwdView = qrPayFreePwdView;
    }

    public QRPayFreePwdPresenter(QRPayFreePwdContract.QRPayQueryPassFreeInfoView qrPayFreePwdView) {
        this();
        mPassFreeInfoView = qrPayFreePwdView;
    }

    /*** 查询安全因子 */
    @Override
    public void loadFreePwdSecurityFactor() {
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
                        mSecurityFactorView.loadFreePwdSecurityFactorFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        mSecurityFactorView.loadFreePwdSecurityFactorSuccess(QrCodeModelUtil.generateSecurityFactorViewModel(psnGetSecurityFactorResult));
                    }
                });
    }

    /*** 开通小额免密服务预交易 */
    @Override
    public void loadQRPayOpenPassFreeServicePre(QRPayFreePwdViewModel freePwdViewModel) {
        //封装开通小额密码服务预交易的请求参数
        QRPayOpenPassFreeServicePreParams passPreParams = QrCodeModelUtil.buildOpenPassFreeServicePreBiiParams(freePwdViewModel);
        passPreParams.setConversationId(conversationID);
        //开通小额免密服务预交易
        qrCodePayService.qRPayOpenPassFreeServicePre(passPreParams)
                .compose(mRxLifecycleManager.<QRPayOpenPassFreeServicePreResult>bindToLifecycle())
                .compose(SchedulersCompat.<QRPayOpenPassFreeServicePreResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayOpenPassFreeServicePreResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrPayFreePwdView.loadQRPayOpenPassFreeServicePreFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayOpenPassFreeServicePreResult openPassFreeServicePreResult) {
                        mQrPayFreePwdView.loadQRPayOpenPassFreeServicePreSuccess(QrCodeModelUtil.generateOpenPassFreeServicePreViewModel(openPassFreeServicePreResult));
                    }
                });
    }

    /*** 开通小额免密服务提交交易 */
    @Override
    public void loadQRPayOpenPassFreeService(final QRPayFreePwdViewModel freePwdViewModel) {
        //获取Token
        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
        getTokenIdParams.setConversationId(conversationID);
        globalService.psnGetTokenId(getTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayOpenPassFreeServiceResult>>() {
                    @Override
                    public Observable<QRPayOpenPassFreeServiceResult> call(String tokenID) {
                        QRPayOpenPassFreeServiceParams params = QrCodeModelUtil.buildOpenPassFreeServiceBiiParams(freePwdViewModel);
                        params.setToken(tokenID);// tokenID
                        params.setConversationId(conversationID);//会话id
                        //开通小额免密服务提交交易
                        return qrCodePayService.qRPayOpenPassFreeService(params);
                    }
                })
                .compose(SchedulersCompat.<QRPayOpenPassFreeServiceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayOpenPassFreeServiceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrPayFreePwdView.loadQRPayOpenPassFreeServiceFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayOpenPassFreeServiceResult result) {
                        mQrPayFreePwdView.loadQRPayOpenPassFreeServiceSuccess();
                    }
                });
    }

    /*** 关闭小额免密服务 */
    @Override
    public void loadQRPayClosePassFreeService(final String actSeq) {
        //获取Token
        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
        getTokenIdParams.setConversationId(conversationID);
        globalService.psnGetTokenId(getTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayClosePassFreeServiceResult>>() {
                    @Override
                    public Observable<QRPayClosePassFreeServiceResult> call(String tokenID) {
                        QRPayClosePassFreeServiceParams params = new QRPayClosePassFreeServiceParams();
                        params.setActSeq(actSeq);//支付卡流水
                        params.setToken(tokenID);// tokenID
                        params.setConversationId(conversationID);//会话id
                        //关闭小额免密服务
                        return qrCodePayService.qRPayClosePassFreeService(params);
                    }
                })
                .compose(SchedulersCompat.<QRPayClosePassFreeServiceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayClosePassFreeServiceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrPayFreePwdView.loadQRPayClosePassFreeServiceFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayClosePassFreeServiceResult result) {
                        mQrPayFreePwdView.loadQRPayClosePassFreeServiceSuccess();
                    }
                });
    }


    /***
     * 查询小额免密信息
     */
    @Override
    public void loadQRPayGetPassFreeInfo(final AccountBean accountBean) {
        // 获取会话ID
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayGetPassFreeInfoResult>>() {
                    @Override
                    public Observable<QRPayGetPassFreeInfoResult> call(String s) {
                        // 查询小额免密信息
                        QRPayGetPassFreeInfoParams params = new QRPayGetPassFreeInfoParams();
                        params.setConversationId(s);
                        params.setActSeq(accountBean.getAccountId());
                        return qrCodePayService.qRPayGetPassFreeInfo(params);
                    }
                })
                .compose(SchedulersCompat.<QRPayGetPassFreeInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetPassFreeInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPassFreeInfoView.loadQRPayGetPassFreeInfoSuccess(QrCodeModelUtil.generatePassFreeInfoViewModel(accountBean, null));
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetPassFreeInfoResult freeInfoResult) {
                        mPassFreeInfoView.loadQRPayGetPassFreeInfoSuccess(QrCodeModelUtil.generatePassFreeInfoViewModel(accountBean, freeInfoResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }


    /**
     * 并发 查询小额免密信息
     *
     * @param accountBeans
     */
    @Override
    public void loadAllPassFreeInfo(List<AccountBean> accountBeans) {
        Observable.just(accountBeans).compose(this.<List<AccountBean>>bindToLifecycle())
                //并发请求账户详情
                .compose(RxUtils.concurrentInIOListRequestTransformer(new Func1<AccountBean, Observable<AccountBean>>() {
                    @Override
                    public Observable<AccountBean> call(final AccountBean accountBean) {
                        // 查询小额免密信息
                        QRPayGetPassFreeInfoParams params = new QRPayGetPassFreeInfoParams();
                        params.setConversationId(conversationID);
                        params.setActSeq(accountBean.getAccountId());
                        qrCodePayService.qRPayGetPassFreeInfo(params)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new GetPassFreeInfoSubscriber<QRPayGetPassFreeInfoResult>(accountBean) {
                                    @Override
                                    public void onNext(QRPayGetPassFreeInfoResult result) {
                                        //回调界面
                                        mPassFreeInfoView.loadQRPayGetPassFreeInfoSuccess(QrCodeModelUtil.generatePassFreeInfoViewModel(accountBean, result));
                                    }
                                });
                        //这里返回,只是为了让程序执行,并没有具体逻辑
                        return Observable.just(accountBean);
                    }
                })).subscribe();
    }


    /**
     * 小额免密处理Subscriber
     */
    private abstract class GetPassFreeInfoSubscriber<T> extends BaseAccountSubscriber<T> {

        /*** 账户信息 */
        private AccountBean accountBean;

        public GetPassFreeInfoSubscriber(AccountBean accountBean) {
            this.accountBean = accountBean;
        }

        @Override
        public void handleException(BiiResultErrorException error) {
            //请求失败,生成AccountListItemViewModel,回调界面
            mPassFreeInfoView.loadQRPayGetPassFreeInfoSuccess(QrCodeModelUtil.generatePassFreeInfoViewModel(accountBean, null));
        }

        /**
         * 重写父类方法,主要是为了不弹出错误对话框
         *
         * @param biiResultErrorException
         */
        @Override
        public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            if (mPassFreeInfoView != null)
                return;

            super.commonHandleException(biiResultErrorException);
        }
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }

}
