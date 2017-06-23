package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.service.QrCodePayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayGetAccountCatalogContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.common.utils.RxUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by fanbin on 16/10/14.
 */
public class QRPayGetAccountCatalogPresenter extends RxPresenter implements QRPayGetAccountCatalogContract.QRPayGetAccountCatalogBasePresenter{
    private RxLifecycleManager mRxLifecycleManager;

    /** 公共service */
    private GlobalService globalService;
    /** 二维码支付的service */
    private QrCodePayService qrCodePayService;
    private QRPayGetAccountCatalogContract.QRPayGetAccountCatalogBaseView mQRPayGetAccountCatalogBaseView;

    private boolean noFirst;

    private int count;
    public QRPayGetAccountCatalogPresenter(){
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        qrCodePayService = new QrCodePayService();
    }

    public QRPayGetAccountCatalogPresenter(QRPayGetAccountCatalogContract.QRPayGetAccountCatalogBaseView securityFactorView){
        this();
        mQRPayGetAccountCatalogBaseView = securityFactorView;
    }

//    public void test(List<AccountBean> accountBeansList ){
//        Observable.from(accountBeansList)
//                .flatMap(new Func1<AccountBean, Observable<QRPayGetAccountCatalogResult>>() {
//                    @Override
//                    public Observable<QRPayGetAccountCatalogResult> call(AccountBean accountBean) {
//                        count++;
//                        return qrCodePayService.qRPayGetAccountCatalog(null);
//                    }
//                }).takeFirst(new Func1<QRPayGetAccountCatalogResult, Boolean>() {
//            @Override
//            public Boolean call(QRPayGetAccountCatalogResult qrPayGetAccountCatalogResult) {
//                if("1".equals(qrPayGetAccountCatalogResult.getAccountCatalog())){
//                    return true;
//                }
//                return false;
//            }
//        }).subscribe(new BIIBaseSubscriber<AccountBean>() {
//            @Override
//            public void handleException(BiiResultErrorException biiResultErrorException) {
//
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onNext(AccountBean accountBean) {
//
//            }
//
//            @Override
//            public void onNext(QRPayGetAccountCatalogResult qrPayGetAccountCatalogResult) {
//
//            }
//        });
//    }

    /**
     * 查询账户类别
     * @param actSeq
     */
    @Override
    public void qRPayGetAccountCatalog(final String actSeq) {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayGetAccountCatalogResult>>() {
                    @Override
                    public Observable<QRPayGetAccountCatalogResult> call(String conversation) {
                        QRPayGetAccountCatalogParams qRPayGetAccountCatalogParams = new QRPayGetAccountCatalogParams();
                        qRPayGetAccountCatalogParams.setConversationId(conversation);
                        qRPayGetAccountCatalogParams.setActSeq(actSeq);
                        // 查询账户类别
                        return qrCodePayService.qRPayGetAccountCatalog(qRPayGetAccountCatalogParams);
                    }
                })
                .compose(SchedulersCompat.<QRPayGetAccountCatalogResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetAccountCatalogResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQRPayGetAccountCatalogBaseView.loadQRPayGetAccountCatalogFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetAccountCatalogResult result) {
                        mQRPayGetAccountCatalogBaseView.loadQRPayGetAccountCatalogSuccess(result);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //重写父类方法,主要是为了不弹出错误对话框
                    }

                });
    }




    /**
     * 并发 查询账户类别
     *
     * @param accountBeans
     */
    @Override
    public void qRPayGetAccountCatalog(List<AccountBean> accountBeans) {
        Observable.just(accountBeans).compose(this.<List<AccountBean>>bindToLifecycle())
                //并发请求账户详情
                .compose(RxUtils.concurrentInIOListRequestTransformer(new Func1<AccountBean, Observable<AccountBean>>() {
                    @Override
                    public Observable<AccountBean> call(final AccountBean accountBean) {
                        //查询账户类别
                        QRPayGetAccountCatalogParams qRPayGetAccountCatalogParams = new QRPayGetAccountCatalogParams();
                        qRPayGetAccountCatalogParams.setConversationId(QRPayMainFragment.qRCodeConversationID);
                        qRPayGetAccountCatalogParams.setActSeq(accountBean.getAccountId());
                        qrCodePayService.qRPayGetAccountCatalog(qRPayGetAccountCatalogParams)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BIIBaseSubscriber<QRPayGetAccountCatalogResult>() {
                                    @Override
                                    public void handleException(BiiResultErrorException biiResultErrorException) {
                                        mQRPayGetAccountCatalogBaseView.loadQRPayGetAccountCatalogSuccess(null);
                                    }

                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onNext(QRPayGetAccountCatalogResult qrPayGetAccountCatalogResult) {
                                        mQRPayGetAccountCatalogBaseView.loadQRPayGetAccountCatalogSuccess(qrPayGetAccountCatalogResult);

                                    }

                                    @Override
                                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                                        //重写父类方法,主要是为了不弹出错误对话框
                                    }
                                });
                        //这里返回,只是为了让程序执行,并没有具体逻辑
                        return Observable.just(accountBean);
                    }
                })).subscribe();
    }


}
