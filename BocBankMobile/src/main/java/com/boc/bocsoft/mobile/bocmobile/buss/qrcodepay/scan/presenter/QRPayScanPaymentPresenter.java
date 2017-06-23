package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoPayment.QRPayDoPaymentParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoPayment.QRPayDoPaymentResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeInfo.QRPayGetPayeeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeInfo.QRPayGetPayeeInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.service.QrCodePayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model.QRPayScanPaymentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui.QRPayGetPayeeInfoContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui.QRPayScanPaymentContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 正扫支付 BII通信逻辑处理
 * Created by wangf on 2016/9/5.
 */
public class QRPayScanPaymentPresenter implements QRPayScanPaymentContract.ScanPaymentPresenter, QRPayGetPayeeInfoContract.GetPayeeInfoPresenter{

    private QRPayScanPaymentContract.QRPayScanPaymentView mPaymentView;
    private QRPayGetPayeeInfoContract.QRPayGetPayeeInfoView mPayeeInfoView;
    private RxLifecycleManager mRxLifecycleManager;

    /** 公共service */
    private GlobalService globalService;
    /** 二维码支付的service */
    private QrCodePayService qrCodePayService;

    //会话ID
    private String conversationID = "";


    public QRPayScanPaymentPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        qrCodePayService = new QrCodePayService();
    }

    public QRPayScanPaymentPresenter(QRPayScanPaymentContract.QRPayScanPaymentView view) {
        this();
        mPaymentView = view;
    }

    public QRPayScanPaymentPresenter(QRPayGetPayeeInfoContract.QRPayGetPayeeInfoView view) {
        this();
        mPayeeInfoView = view;
    }


    /**
     * 获取随机数
     */
    @Override
    public void loadGetRandom() {
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
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPaymentView.loadGetRandomFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String random) {
                        mPaymentView.loadGetRandomSuccess(random);
                    }
                });
    }

	/**
	 * 正扫支付
	 */
	@Override
	public void loadQRPayDoPayment(final QRPayScanPaymentViewModel paymentViewModel) {
			PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
			psnGetTokenIdParams.setConversationId(conversationID);
			// 查询TokenID
			globalService.psnGetTokenId(psnGetTokenIdParams)
					.compose(mRxLifecycleManager.<String> bindToLifecycle())
					.flatMap(new Func1<String, Observable<QRPayDoPaymentResult>>() {
								@Override
								public Observable<QRPayDoPaymentResult> call(
										String tokenID) {
									QRPayDoPaymentParams doPaymentParams = QrCodeModelUtil
											.buildDoPaymentBiiParams(paymentViewModel);
									doPaymentParams.setConversationId(conversationID);
									doPaymentParams.setToken(tokenID);
									// 正扫支付
									return qrCodePayService.qRPayDoPayment(doPaymentParams);
								}
							})
					.compose(
							SchedulersCompat
									.<QRPayDoPaymentResult> applyIoSchedulers())
					.subscribe(new BIIBaseSubscriber<QRPayDoPaymentResult>() {
						@Override
						public void handleException(
								BiiResultErrorException biiResultErrorException) {
							mPaymentView
									.loadQRPayDoPaymentFail(biiResultErrorException);
						}

						@Override
						public void onCompleted() {

						}

						@Override
						public void onNext(QRPayDoPaymentResult doPaymentResult) {
							mPaymentView.loadQRPayDoPaymentSuccess(QrCodeModelUtil
									.generateDoPaymentViewModel(doPaymentResult));
						}

                        @Override
                        public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                            //此处重写该方法，在错误处理时使底层不在弹窗
                        }
					});
	}


    /**
     * 正扫支付 - 免密
     */
    @Override
    public void loadQRPayDoPaymentFreePass(final QRPayScanPaymentViewModel paymentViewModel) {
        //获取会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        conversationID = conversationId;
                        // 查询TokenID
                        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
                        getTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(getTokenIdParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, QRPayDoPaymentParams>() {
                    @Override
                    public QRPayDoPaymentParams call(String s) {
                        QRPayDoPaymentParams doPaymentParams = QrCodeModelUtil.buildDoPaymentBiiParams(paymentViewModel);
                        doPaymentParams.setConversationId(conversationID);
                        doPaymentParams.setToken(s);
                        return doPaymentParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<QRPayDoPaymentParams, Observable<QRPayDoPaymentResult>>() {
                    @Override
                    public Observable<QRPayDoPaymentResult> call(QRPayDoPaymentParams doPaymentParams) {
                        // 正扫支付
                        return qrCodePayService.qRPayDoPayment(doPaymentParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<QRPayDoPaymentResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPaymentView.loadQRPayDoPaymentFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayDoPaymentResult doPaymentResult) {
                        mPaymentView.loadQRPayDoPaymentSuccess(QrCodeModelUtil.generateDoPaymentViewModel(doPaymentResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }


    /*** 查询收款人信息  */
    @Override
    public void loadGetPayeeInfo(final String qrNo) {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayGetPayeeInfoResult>>() {
                    @Override
                    public Observable<QRPayGetPayeeInfoResult> call(String conversation) {
                        //查询收款人信息
                        QRPayGetPayeeInfoParams params = new QRPayGetPayeeInfoParams();
                        params.setConversationId(conversation);
                        params.setQrNo(qrNo);
                        return qrCodePayService.qRPayGetPayeeInfo(params);
                    }
                })
                .compose(SchedulersCompat.<QRPayGetPayeeInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetPayeeInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPayeeInfoView.loadGetPayeeInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetPayeeInfoResult result) {
                        mPayeeInfoView.loadGetPayeeInfoSuccess(QrCodeModelUtil.generatePayeeInfoViewModel(result));
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
