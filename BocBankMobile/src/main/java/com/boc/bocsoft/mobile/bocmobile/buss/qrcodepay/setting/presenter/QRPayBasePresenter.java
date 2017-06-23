package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnCommonQueryOprLoginInfo.PsnCommonQueryOprLoginInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnCommonQueryOprLoginInfo.PsnCommonQueryOprLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnWMSQueryWealthRank.PsnWMSQueryWealthRankParams;
import com.boc.bocsoft.mobile.bii.bus.login.service.PsnLoginService;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetDefaultCard.QRPayGetDefaultCardParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetDefaultCard.QRPayGetDefaultCardResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo.QRPayGetPassFreeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo.QRPayGetPassFreeInfoResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayQuota.QRPayGetPayQuotaParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayQuota.QRPayGetPayQuotaResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetRelativedAcctList.QRPayGetRelativedAcctListParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetRelativedAcctList.QRPayGetRelativedAcctListResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayIsPassSet.QRPayIsPassSetParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayIsPassSet.QRPayIsPassSetResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetDefaultCard.QRPaySetDefaultCardParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetDefaultCard.QRPaySetDefaultCardResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceIsOpen.QRServiceIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceIsOpen.QRServiceIsOpenResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceOpen.QRServiceOpenParams;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceOpen.QRServiceOpenResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.service.QrCodePayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui.QRPayDoPaymentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayBaseContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangf on 2016/8/31.
 */
public class QRPayBasePresenter implements QRPayBaseContract.QrCodeBasePresenter {

    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 公共service
     */
    private GlobalService globalService;
    private AccountService accountService;

    /***
     * 登录service
     */
    private PsnLoginService psnLoginService;

    /**
     * 二维码支付的service
     */
    private QrCodePayService qrCodePayService;

    private QRPayBaseContract.QrServiceOpenBaseView mQrServiceOpenBaseView;
    private QRPayBaseContract.QrQueryBaseView mQrQueryBaseView;
    private QRPayBaseContract.QRQueryPassFreeInfoBaseView mPassFreeInfoBaseView;
    private QRPayBaseContract.QrSetCardBaseView mQrSetCardBaseView;
    private QRPayBaseContract.QrAccountBaseView mQrAccountBaseView;

    // 会话ID
    public static String pwdConversationID = "";
    //设置默认卡的会话id
    public static String cardConversationID = "";

    /***
     * 客户等级
     */
    private String custLevel;


    public QRPayBasePresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        accountService = new AccountService();
        qrCodePayService = new QrCodePayService();
        psnLoginService = new PsnLoginService();
    }

    public QRPayBasePresenter(QRPayBaseContract.QrServiceOpenBaseView qrServiceOpenBaseView) {
        this();
        mQrServiceOpenBaseView = qrServiceOpenBaseView;
    }

    public QRPayBasePresenter(QRPayBaseContract.QrQueryBaseView qrQueryCardBaseView) {
        this();
        mQrQueryBaseView = qrQueryCardBaseView;
    }

    public QRPayBasePresenter(QRPayBaseContract.QrAccountBaseView accountBaseView) {
        this();
        mQrAccountBaseView = accountBaseView;
    }

    public QRPayBasePresenter(QRPayBaseContract.QrSetCardBaseView setCardBaseView) {
        this();
        mQrSetCardBaseView = setCardBaseView;
    }

    public QRPayBasePresenter(QRPayBaseContract.QRQueryPassFreeInfoBaseView freeInfoBaseView) {
        this();
        mPassFreeInfoBaseView = freeInfoBaseView;
    }

    /**
     * 查询关联账户中的银联账户列表
     * add by wangf on 2016-11-2 20:52:48
     */
    @Override
    public void queryQRPayGetRelativedAcctList() {
        QRPayGetRelativedAcctListParams mParams = new QRPayGetRelativedAcctListParams();
        mParams.setAccountType("");
        qrCodePayService.qRPayGetRelativedAcctList(mParams)
                .compose(mRxLifecycleManager.<List<QRPayGetRelativedAcctListResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<QRPayGetRelativedAcctListResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<QRPayGetRelativedAcctListResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrQueryBaseView.queryRelativeAccountListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<QRPayGetRelativedAcctListResult> result) {
                        QRPayMainFragment.setRealtiveBankAccountList(QrCodeModelUtil.convertBIIAccount2ViewModel(result));
                        mQrQueryBaseView.queryRelativeAccountListSuccess();
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }


    /**
     * 查询中行所有帐户列表
     */
    @Override
    public void queryAllChinaBankAccount() {
        PsnCommonQueryAllChinaBankAccountParams mParams = new PsnCommonQueryAllChinaBankAccountParams();
        mParams.setAccountType(null);
        globalService.psnCommonQueryAllChinaBankAccount(mParams)
                .compose(mRxLifecycleManager.<List<PsnCommonQueryAllChinaBankAccountResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCommonQueryAllChinaBankAccountResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCommonQueryAllChinaBankAccountResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnCommonQueryAllChinaBankAccountResult> result) {
//                        Log.i("feib","-----------PsnCommonQueryAllChinaBankAccountResult---------"+result);
//                        ApplicationContext.getInstance().setChinaBankAccountList(AccountUtils.convertBIIAccount2ViewModel(result));
//                        mLoginBaseView.allChinaBankAccountSuccess(AccountUtils.convertBIIAccount2ViewModel(result));
                    }
                });
    }


    /**
     * 获取客户等级及信息
     */
    @Override
    public void queryOprLoginInfo() {
        //查询客户等级 04 中银理财客户 05 特殊理财客户 06 财富管理客户 07 特殊财富客户 08 私人银行客户
        PsnWMSQueryWealthRankParams wealthRankParams = new PsnWMSQueryWealthRankParams();
        psnLoginService.psnWMSQueryWealthRank(wealthRankParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                        //上个接口调用失败，则继续执行下个接口
                .onErrorResumeNext(Observable.<String>just(null))
                        //查询操作员信息
                .flatMap(new Func1<String, Observable<PsnCommonQueryOprLoginInfoResult>>() {
                    @Override
                    public Observable<PsnCommonQueryOprLoginInfoResult> call(String wealthRank) {
                        // 记录客户等级
                        custLevel = wealthRank;
                        PsnCommonQueryOprLoginInfoParams oprLoginInfoParams = new PsnCommonQueryOprLoginInfoParams();
                        return psnLoginService.psnCommonQueryOprLoginInfo(oprLoginInfoParams);
                    }
                })
                .compose(SchedulersCompat.<PsnCommonQueryOprLoginInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCommonQueryOprLoginInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnCommonQueryOprLoginInfoResult oprLoginInfoResult) {
//                        //查询操作员信息成功，设置用户登录信息
//                        User user = new User();
//                        user.setLogin(true);
//                        user.setCustLevel(custLevel);
//                        BeanConvertor.toBean(oprLoginInfoResult, user);
                        //保存到context
//                        ApplicationContext.getInstance().setUser(user);
                    }
                });
    }


    /**
     * 获取服务器时间
     */
    @Override
    public void querySystemDateTime() {
        PsnCommonQuerySystemDateTimeParams mParams = new PsnCommonQuerySystemDateTimeParams();
        globalService.psnCommonQuerySystemDateTime(mParams)
                .compose(mRxLifecycleManager.<PsnCommonQuerySystemDateTimeResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCommonQuerySystemDateTimeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCommonQuerySystemDateTimeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCommonQuerySystemDateTimeResult systemDateTime) {
//                        //设置服务器时间
//                        ApplicationContext.systemDataTime= systemDateTime.getDateTme();
//                        //设置当前设备时间
//                        ApplicationContext.deviceDataTime = LocalDateTime.now();
                    }
                });
    }

    /***
     * 查询客户是否开通二维码服务
     */
    @Override
    public void loadQRServiceIsOpen() {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRServiceIsOpenResult>>() {
                    @Override
                    public Observable<QRServiceIsOpenResult> call(String conversation) {
                        QRServiceIsOpenParams params = new QRServiceIsOpenParams();
                        params.setConversationId(conversation);
                        QRPayDoPaymentFragment.conversationId=conversation;
                        //查询客户是否开通二维码服务
                        return qrCodePayService.qRServiceIsOpen(params);
                    }
                })
                .compose(SchedulersCompat.<QRServiceIsOpenResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRServiceIsOpenResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrQueryBaseView.loadQRServiceIsOpenFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRServiceIsOpenResult result) {
                        mQrQueryBaseView.loadQRServiceIsOpenSuccess(result.getFlag());
                    }
                });
    }

    /***
     * 开通二维码服务
     */
    @Override
    public void loadQRServiceOpen() {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRServiceOpenResult>>() {
                    @Override
                    public Observable<QRServiceOpenResult> call(String conversation) {
                        //开通二维码服务
                        QRServiceOpenParams params = new QRServiceOpenParams();
                        QRPayDoPaymentFragment.conversationId=conversation;
                        params.setConversationId(conversation);
                        return qrCodePayService.qRServiceOpen(params);
                    }
                })
                .compose(SchedulersCompat.<QRServiceOpenResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRServiceOpenResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrServiceOpenBaseView.loadQRServiceOpenFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRServiceOpenResult result) {
                        mQrServiceOpenBaseView.loadQRServiceOpenSuccess();
                    }
                });
    }

    /**
     * 查询默认卡
     */
    @Override
    public void loadQRPayGetDefaultCard() {
        // 获取会话ID
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayGetDefaultCardResult>>() {
                    @Override
                    public Observable<QRPayGetDefaultCardResult> call(String s) {
                        // 查询默认卡
                        QRPayGetDefaultCardParams params = new QRPayGetDefaultCardParams();
                        QRPayDoPaymentFragment.conversationId=s;
                        params.setConversationId(s);
                        return qrCodePayService.qRPayGetDefaultCard(params);
                    }
                })
                .compose(SchedulersCompat.<QRPayGetDefaultCardResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetDefaultCardResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrQueryBaseView.loadQRPayGetDefaultCardFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetDefaultCardResult qrPayGetDefaultCardResult) {
                        mQrQueryBaseView.loadQRPayGetDefaultCardSuccess(qrPayGetDefaultCardResult.getActSeq());
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });

    }


    /***
     * 查询是否设置支付密码
     */
    @Override
    public void loadQRPayIsPassSet() {
        //获取会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        pwdConversationID = conversationId;
                        QRPayDoPaymentFragment.conversationId=conversationId;
                        // 查询TokenID
                        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
                        getTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(getTokenIdParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, QRPayIsPassSetParams>() {
                    @Override
                    public QRPayIsPassSetParams call(String s) {
                        QRPayIsPassSetParams isPassSetParams = new QRPayIsPassSetParams();
                        isPassSetParams.setConversationId(pwdConversationID);
                        isPassSetParams.setToken(s);
                        isPassSetParams.setPassType("01");
                        return isPassSetParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<QRPayIsPassSetParams, Observable<QRPayIsPassSetResult>>() {
                    @Override
                    public Observable<QRPayIsPassSetResult> call(QRPayIsPassSetParams isPassSetParams) {
                        // 查询是否设置支付密码
                        return qrCodePayService.qRPayIsPassSet(isPassSetParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<QRPayIsPassSetResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrQueryBaseView.loadQRPayIsPassSetFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayIsPassSetResult isPassSetResult) {
                        mQrQueryBaseView.loadQRPayIsPassSetSuccess(isPassSetResult.getFlag());
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }

    /**
     * 查询支付限额
     */
    @Override
    public void loadQRPayGetPayQuota(final String actSeq, final String type) {
        // 获取会话ID
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayGetPayQuotaResult>>() {
                    @Override
                    public Observable<QRPayGetPayQuotaResult> call(String s) {
                        QRPayDoPaymentFragment.conversationId=s;
                        // 查询支付限额
                        QRPayGetPayQuotaParams params = new QRPayGetPayQuotaParams();
                        params.setConversationId(s);
                        params.setActSeq(actSeq);
                        params.setType(type);
                        return qrCodePayService.qRPayGetPayQuota(params);
                    }
                })
                .compose(SchedulersCompat.<QRPayGetPayQuotaResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetPayQuotaResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPassFreeInfoBaseView.loadQRPayGetPayQuotaFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetPayQuotaResult quotaResult) {
                        mPassFreeInfoBaseView.loadQRPayGetPayQuotaSuccess(QrCodeModelUtil.generatePayQuotaViewModel(quotaResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }

    /***
     * 查询小额免密信息
     *
     * @param actSeq
     */
    @Override
    public void loadQRPayGetPassFreeInfo(final String actSeq) {
        // 获取会话ID
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<QRPayGetPassFreeInfoResult>>() {
                    @Override
                    public Observable<QRPayGetPassFreeInfoResult> call(String s) {
                        QRPayDoPaymentFragment.conversationId=s;
                        // 查询小额免密信息
                        QRPayGetPassFreeInfoParams params = new QRPayGetPassFreeInfoParams();
                        params.setConversationId(s);
                        params.setActSeq(actSeq);
                        return qrCodePayService.qRPayGetPassFreeInfo(params);
                    }
                })
                .compose(SchedulersCompat.<QRPayGetPassFreeInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<QRPayGetPassFreeInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPassFreeInfoBaseView.loadQRPayGetPassFreeInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPayGetPassFreeInfoResult freeInfoResult) {
                        mPassFreeInfoBaseView.loadQRPayGetPassFreeInfoSuccess(QrCodeModelUtil.generatePassFreeInfoViewModel(freeInfoResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }

    /**
     * 设置默认卡
     */
    @Override
    public void loadQRPaySetDefaultCard(final String actSeq) {
        //获取会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        cardConversationID = conversationId;
                        QRPayDoPaymentFragment.conversationId=conversationId;
                        // 查询TokenID
                        PSNGetTokenIdParams getTokenIdParams = new PSNGetTokenIdParams();
                        getTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(getTokenIdParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, QRPaySetDefaultCardParams>() {
                    @Override
                    public QRPaySetDefaultCardParams call(String s) {
                        QRPaySetDefaultCardParams setDefaultCardParams = new QRPaySetDefaultCardParams();
                        setDefaultCardParams.setConversationId(cardConversationID);
                        setDefaultCardParams.setToken(s);
                        setDefaultCardParams.setActSeq(actSeq);
                        return setDefaultCardParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<QRPaySetDefaultCardParams, Observable<QRPaySetDefaultCardResult>>() {
                    @Override
                    public Observable<QRPaySetDefaultCardResult> call(QRPaySetDefaultCardParams setDefaultCardParams) {
                        // 设置默认卡
                        return qrCodePayService.qRPaySetDefaultCard(setDefaultCardParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<QRPaySetDefaultCardResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrSetCardBaseView.loadQRPaySetDefaultCardFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(QRPaySetDefaultCardResult setDefaultCardResult) {
                        mQrSetCardBaseView.loadQRPaySetDefaultCardSuccess();
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }

    /***
     * 查询普通账户详情
     */
    @Override
    public void queryAccountDetails(String accountID) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountID);

        accountService.psnAccountQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrAccountBaseView.queryAccountDetailsFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        List<PsnAccountQueryAccountDetailResult.AccountDetaiListBean> accountDetaiListBeans = result.getAccountDetaiList();
                        BigDecimal bigDecimal=null;
                        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean accountDetaiListBean:accountDetaiListBeans){
                            if ("001".equals(accountDetaiListBean.getCurrencyCode())){
                                bigDecimal=accountDetaiListBean.getAvailableBalance();
                            }
                        }
                        mQrAccountBaseView.queryAccountDetailsSuccess(bigDecimal);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }

    /***
     * 查询信用卡账户详情
     */
    @Override
    public void queryCreditAccountDetail(String accountId, String currencyId) {
        PsnCrcdQueryAccountDetailParams params = new PsnCrcdQueryAccountDetailParams();
        params.setAccountId(accountId);
        params.setCurrency(currencyId);

        accountService.psnCrcdQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnCrcdQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mQrAccountBaseView.queryCreditAccountDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryAccountDetailResult result) {
                        mQrAccountBaseView.queryCreditAccountDetailSuccess(result.getCrcdAccountDetailList().get(0).getTotalBalance());
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
