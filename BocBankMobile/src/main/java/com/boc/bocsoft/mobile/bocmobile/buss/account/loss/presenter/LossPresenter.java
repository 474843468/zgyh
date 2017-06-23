package com.boc.bocsoft.mobile.bocmobile.buss.account.loss.presenter;


import android.content.Context;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportConfirm.PsnAccountLossReportConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportConfirm.PsnAccountLossReportConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportResult.PsnAccountLossReportResultParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportResult.PsnAccountLossReportResultResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryCardholderAddress.PsnCrcdQueryCardholderAddressParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryCardholderAddress.PsnCrcdQueryCardholderAddressResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossConfirm.PsnCrcdReportLossConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossConfirm.PsnCrcdReportLossConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossFee.PsnCrcdReportLossFeeParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossFee.PsnCrcdReportLossFeeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResult.PsnCrcdReportLossResultParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResult.PsnCrcdReportLossResultResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResultReinforce.PsnCrcdReportLossResultReinforceParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResultReinforce.PsnCrcdReportLossResultReinforceResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportConfirm.PsnDebitcardLossReportConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportConfirm.PsnDebitcardLossReportConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportResult.PsnDebitcardLossReportResultParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportResult.PsnDebitcardLossReportResultResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.CombinBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.model.LossViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui.LossContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui.LossFragment;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 挂失冻结逻辑处理
 * <p>
 * Created by liuweidong on 2016/6/12.
 */
public class LossPresenter implements LossContract.Presenter {
    private LossContract.LossBeforeView mBeforeView;
    private LossContract.LossView mView;
    private RxLifecycleManager mRxLifecycleManager;
    /**
     * 公共Service
     */
    private GlobalService globalService;
    /**
     * 账户管理Service
     */
    private AccountService accountService;
    /**
     * 会话ID
     */
    public static String conversationID;
    /**
     * 服务码
     */
    private String serviceID = "";
    public static String randomID = "";
    private String tokenID;

    public LossPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        accountService = new AccountService();
    }

    public LossPresenter(LossContract.LossBeforeView view) {
        this();
        mBeforeView = view;
    }

    public LossPresenter(LossContract.LossView view) {
        this();
        mView = view;
    }

    /**
     * 信用卡挂失费用
     *
     * @param accountId
     */
    @Override
    public void queryCreditLossFee(int accountId) {
        PsnCrcdReportLossFeeParams params = new PsnCrcdReportLossFeeParams();
        params.setAccountId(accountId);
        accountService.psnCrcdReportLossFee(params)
                .compose(mRxLifecycleManager.<PsnCrcdReportLossFeeResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdReportLossFeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdReportLossFeeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBeforeView.queryCreditLossFeeFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnCrcdReportLossFeeResult psnCrcdReportLossFeeResult) {
                        resultModelToViewModel(psnCrcdReportLossFeeResult);
                        mBeforeView.queryCreditLossFeeSuccess();
                    }
                });
    }

    /**
     * 信用卡补卡地址
     *
     * @param accountId
     */
    @Override
    public void queryCreditLossAddress(int accountId) {
        PsnCrcdQueryCardholderAddressParams params = new PsnCrcdQueryCardholderAddressParams();
        params.setAccountId(accountId);
        accountService.psnCrcdQueryCardholderAddress(params)
                .compose(mRxLifecycleManager.<PsnCrcdQueryCardholderAddressResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryCardholderAddressResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryCardholderAddressResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBeforeView.queryCreditLossAddressFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnCrcdQueryCardholderAddressResult psnCrcdQueryCardholderAddressResult) {
                        resultModelToViewModel(psnCrcdQueryCardholderAddressResult);
                        mBeforeView.queryCreditLossAddressSuccess();
                    }
                });
    }

    /**
     * 查询安全因子
     */
    @Override
    public void querySecurityFactor(String accountType) {
        switch (accountType) {
            case ApplicationConst.ACC_TYPE_ZHONGYIN:// 信用卡
            case ApplicationConst.ACC_TYPE_GRE:
            case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
                serviceID = ServiceIdCodeConst.SERVICE_ID_LOSS_CREDIT_CARD;
                break;
            default:
                serviceID = ServiceIdCodeConst.SERVICE_ID_LOSS_DEBIT_CARD;
                break;
        }
        // 创建会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        conversationID = s;
                        // 查询随机数
                        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
                        psnGetRandomParams.setConversationId(conversationID);
                        return globalService.psnGetRandom(psnGetRandomParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, PsnGetSecurityFactorParams>() {
                    @Override
                    public PsnGetSecurityFactorParams call(String random) {
                        randomID = random;
                        // 安全因子
                        PsnGetSecurityFactorParams psnGetSecurityFactorParams = new PsnGetSecurityFactorParams();
                        psnGetSecurityFactorParams.setConversationId(conversationID);
                        psnGetSecurityFactorParams.setServiceId(serviceID);
                        return psnGetSecurityFactorParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnGetSecurityFactorParams, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(PsnGetSecurityFactorParams params) {
                        return globalService.psnGetSecurityFactor(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBeforeView.querySecurityFactorFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        mBeforeView.querySecurityFactorSuccess(resultModelToViewModel(psnGetSecurityFactorResult));
                    }
                });
    }

    /**
     * 借记卡预交易
     */
    @Override
    public void debitCardLossConfirm(final String accountNum, final LossViewModel lossViewModel, final String combinID) {
        PsnDebitcardLossReportConfirmParams params = buildPsnDebitcardLossReportConfirmParams(lossViewModel);
        params.setConversationId(conversationID);
        params.setAccountNumber(accountNum);
        params.set_combinId(combinID);
        accountService.psnDebitcardLossReportConfirm(params)
                .compose(mRxLifecycleManager.<PsnDebitcardLossReportConfirmResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDebitcardLossReportConfirmResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDebitcardLossReportConfirmResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.debitCardLossConfirmFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnDebitcardLossReportConfirmResult result) {
                        resultModelToViewModel(result);
                        mView.debitCardLossConfirmSuccess();
                    }
                });
    }

    /**
     * 借记卡提交交易
     */
    @Override
    public void debitCardLossResult(final AccountBean accountBean, final String[] randomNums, final String[] encryptPasswords, final int curCombinID, final Context context) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationID);

        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnDebitcardLossReportResultResult>>() {
                    @Override
                    public Observable<PsnDebitcardLossReportResultResult> call(String result) {
                        PsnDebitcardLossReportResultParams params = buildPsnDebitcardLossReportResultParams(accountBean, randomNums, encryptPasswords, curCombinID, context);
                        params.setToken(result);
                        return accountService.psnDebitcardLossReportResult(params);
                    }
                }).compose(SchedulersCompat.<PsnDebitcardLossReportResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDebitcardLossReportResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.debitCardLossResultFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnDebitcardLossReportResultResult psnDebitcardLossReportResultResult) {
                        mView.debitCardLossResultSuccess(resultModelToViewModel(psnDebitcardLossReportResultResult));
                    }
                });
    }

    /**
     * 活期一本通预交易
     */
    @Override
    public void accountLossConfirm(final String accountNum, final String combinID) {
        PsnAccountLossReportConfirmParams params = new PsnAccountLossReportConfirmParams();
        params.setConversationId(conversationID);
        params.setAccountNumber(accountNum);
        params.set_combinId(combinID);
        accountService.psnAccountLossReportConfirm(params)
                .compose(mRxLifecycleManager.<PsnAccountLossReportConfirmResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountLossReportConfirmResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountLossReportConfirmResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.accountLossConfirmFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountLossReportConfirmResult psnAccountLossReportConfirmResult) {
                        resultModelToViewModel(psnAccountLossReportConfirmResult);
                        mView.accountLossConfirmSuccess();
                    }
                });
    }

    /**
     * 活期一本通提交交易
     */
    @Override
    public void accountLossResult(final AccountBean accountBean, final String[] randomNums, final String[] encryptPasswords, final int curCombinID, final Context context) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationID);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnAccountLossReportResultResult>>() {
                    @Override
                    public Observable<PsnAccountLossReportResultResult> call(String tokenID) {
                        PsnAccountLossReportResultParams params = buildPsnAccountLossReportResultParams(accountBean, randomNums, encryptPasswords, curCombinID, context);
                        params.setToken(tokenID);
                        return accountService.psnAccountLossReportResult(params);
                    }
                }).compose(SchedulersCompat.<PsnAccountLossReportResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountLossReportResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.accountLossResultFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountLossReportResultResult psnAccountLossReportResultResult) {
                        mView.accountLossResultSuccess();
                    }
                });
    }

    /**
     * 信用卡预交易
     */
    @Override
    public void creditCardLossConfirm(Integer accountID, String combinID) {
        PsnCrcdReportLossConfirmParams params = new PsnCrcdReportLossConfirmParams();
        params.setConversationId(conversationID);
        params.setAccountId(accountID);
        params.set_combinId(combinID);
        accountService.psnCrcdReportLossConfirm(params)
                .compose(mRxLifecycleManager.<PsnCrcdReportLossConfirmResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdReportLossConfirmResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdReportLossConfirmResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.creditCardLossConfirmFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdReportLossConfirmResult psnCrcdReportLossConfirmResult) {
                        resultModelToViewModel(psnCrcdReportLossConfirmResult);
                        mView.creditCardLossConfirmSuccess();
                    }
                });
    }

    /**
     * 信用卡提交交易
     */
    @Override
    public void creditCardLossResult(final String accountID, final String[] randomNums, final String[] encryptPasswords, final int curCombinID, final Context context) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationID);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdReportLossResultResult>>() {
                    @Override
                    public Observable<PsnCrcdReportLossResultResult> call(String tokenID) {
                        PsnCrcdReportLossResultParams params = buildPsnCrcdReportLossResultParams(accountID, randomNums, encryptPasswords, curCombinID, context);
                        params.setToken(tokenID);
                        return accountService.psnCrcdReportLossResult(params);
                    }
                }).compose(SchedulersCompat.<PsnCrcdReportLossResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdReportLossResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.creditCardLossResultFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdReportLossResultResult psnCrcdReportLossResultResult) {
                        mView.creditCardLossResultSuccess();
                    }
                });
    }

    /**
     * 信用卡提交加强认证交易
     */
    @Override
    public void creditCardResultReinforce() {
        PsnCrcdReportLossResultReinforceParams psnCrcdReportLossResultReinforceParams = new PsnCrcdReportLossResultReinforceParams();
        psnCrcdReportLossResultReinforceParams.setConversationId(conversationID);
        accountService.psnCrcdReportLossResultReinforce(psnCrcdReportLossResultReinforceParams)
                .compose(mRxLifecycleManager.<PsnCrcdReportLossResultReinforceResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdReportLossResultReinforceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdReportLossResultReinforceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdReportLossResultReinforceResult psnCrcdReportLossResultReinforceResult) {

                    }
                });
    }

    /**
     * 封装借记卡预交易请求参数
     *
     * @param lossViewModel
     * @return
     */
    private PsnDebitcardLossReportConfirmParams buildPsnDebitcardLossReportConfirmParams(LossViewModel lossViewModel) {
        PsnDebitcardLossReportConfirmParams params = new PsnDebitcardLossReportConfirmParams();
        params.setLossDays(lossViewModel.getLossDays());// 挂失期限
        params.setAccFlozenFlag(lossViewModel.getAccFlozenFlag());// 是否同时冻结借记卡主账户
        return params;
    }

    /**
     * 封装借记卡提交交易请求参数
     *
     * @param accountBean
     * @param encryptPasswords
     * @param curCombinID
     * @param context
     * @return
     */
    private PsnDebitcardLossReportResultParams buildPsnDebitcardLossReportResultParams(AccountBean accountBean, String[] randomNums, String[] encryptPasswords, int curCombinID, Context context) {
        PsnDebitcardLossReportResultParams params = new PsnDebitcardLossReportResultParams();
        LossViewModel lossViewModel = LossFragment.getViewModel();
        params.setConversationId(conversationID);
        params.setAccountId(Integer.valueOf(accountBean.getAccountId()));// 账号标识
        params.setAccountNumber(accountBean.getAccountNumber());// 账号
        params.setLossDays(lossViewModel.getLossDays());// 挂失期限
        params.setAccFlozenFlag(lossViewModel.getAccFlozenFlag());// 是否同时冻结借记卡主账户
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        switch (curCombinID) {
            case SecurityVerity.SECURITY_VERIFY_TOKEN:// 动态口令
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS:// 短信
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:// 动态口令+短信
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:// 手机交易码+硬件绑定
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(context, LossPresenter.randomID);
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:// 中银e盾
                params.set_signedData(randomNums[0]);
                break;
            default:
                break;
        }
        return params;
    }

    /**
     * 封装活期一本通提交交易请求参数
     *
     * @param accountBean
     * @param encryptPasswords
     * @param curCombinID
     * @param context
     * @return
     */
    private PsnAccountLossReportResultParams buildPsnAccountLossReportResultParams(AccountBean accountBean, String[] randomNums, String[] encryptPasswords, int curCombinID, Context context) {
        PsnAccountLossReportResultParams params = new PsnAccountLossReportResultParams();
        LossViewModel lossViewModel = LossFragment.getViewModel();
        params.setConversationId(conversationID);
        params.setAccountId(Integer.valueOf(accountBean.getAccountId()));// 账号标识
        params.setAccountNumber(accountBean.getAccountNumber());// 账号
        params.setLossDays(lossViewModel.getLossDays());// 挂失期限
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        switch (curCombinID) {
            case SecurityVerity.SECURITY_VERIFY_TOKEN:// 动态口令
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS:// 短信
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:// 动态口令+短信
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:// 手机交易码+硬件绑定
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(context, LossPresenter.randomID);
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:// 中银e盾
                params.set_signedData(randomNums[0]);
                break;
            default:
                break;
        }
        return params;
    }

    /**
     * 信用卡提交交易请求参数
     *
     * @param accountID
     * @param encryptPasswords
     * @param curCombinID
     * @param context
     * @return
     */
    private PsnCrcdReportLossResultParams buildPsnCrcdReportLossResultParams(String accountID, String[] randomNums, String[] encryptPasswords, int curCombinID, Context context) {
        PsnCrcdReportLossResultParams params = new PsnCrcdReportLossResultParams();
        LossViewModel lossViewModel = LossFragment.getViewModel();
        params.setConversationId(conversationID);
        params.setAccountId(accountID);// 账户标识
        params.setSelectLossType(lossViewModel.getSelectLossType());// 挂失类型
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        switch (curCombinID) {
            case SecurityVerity.SECURITY_VERIFY_TOKEN:// 动态口令
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS:// 短信
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:// 动态口令+短信
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:// 手机交易码+硬件绑定
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(context, randomID);
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:// 中银e盾

                break;
            default:
                break;
        }
        return params;
    }

    /**
     * 信用卡加强认证交易请求参数
     *
     * @param lossViewModel
     * @return
     */
    private PsnCrcdReportLossResultReinforceParams buildPsnCrcdReportLossResultReinforceParams(LossViewModel lossViewModel) {
        PsnCrcdReportLossResultReinforceParams params = new PsnCrcdReportLossResultReinforceParams();
        return params;
    }

    /**
     * 信用卡挂失费用响应数据
     *
     * @param result
     */
    private void resultModelToViewModel(PsnCrcdReportLossFeeResult result) {
        LossViewModel viewModel = LossFragment.getViewModel();
        viewModel.setLossFee(result.getLossFee());// 挂失费用
        viewModel.setLossFeeCurrency(result.getLossFeeCurrency());// 挂失币种
        viewModel.setReportFee(result.getReportFee());// 补卡费用
        viewModel.setReportFeeCurrency(result.getReportFeeCurrency());// 补卡币种
        viewModel.setGetChargeFlag(result.getGetChargeFlag());// 试费查询是否成功标识位
    }

    /**
     * 信用卡挂失地址响应数据
     *
     * @param result
     */
    private void resultModelToViewModel(PsnCrcdQueryCardholderAddressResult result) {
        LossViewModel viewModel = LossFragment.getViewModel();
        viewModel.setMailAddress(result.getMailAddress());// 邮寄地址
        viewModel.setMailAddressType(result.getMailAddressType());// 邮寄地址类型
    }

    /**
     * 安全因子响应数据
     *
     * @param result
     * @return
     */
    private SecurityViewModel resultModelToViewModel(PsnGetSecurityFactorResult result) {
        SecurityViewModel viewModel = new SecurityViewModel();
        if (result.get_defaultCombin() != null) {
            CombinBean defaultCombin = new CombinBean();
            defaultCombin.setName(result.get_defaultCombin().getName());
            defaultCombin.setId(result.get_defaultCombin().getId());
            viewModel.set_defaultCombin(defaultCombin);// 设置默认安全因子
        }
        List<CombinBean> list = new ArrayList<CombinBean>();
        for (CombinListBean item : result.get_combinList()) {
            CombinBean bean = new CombinBean();
            bean.setId(item.getId());
            bean.setName(item.getName());
            list.add(bean);
        }
        viewModel.set_combinList(list);
        return viewModel;
    }

    /**
     * 活期一本通预交易响应数据
     *
     * @param result
     * @return
     */
    private void resultModelToViewModel(PsnAccountLossReportConfirmResult result) {
        LossViewModel viewModel = LossFragment.getViewModel();
        viewModel.set_certDN(result.get_certDN());
        viewModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        viewModel.set_plainData(result.get_plainData());

        List<FactorBean> list = new ArrayList<FactorBean>();
        for (PsnAccountLossReportConfirmResult.FactorListBean item : result.getFactorList()) {
            FactorBean bean = new FactorBean();
            FactorBean.FieldBean fieldBean = new FactorBean.FieldBean();
            fieldBean.setName(item.getField().getName());
            fieldBean.setType(item.getField().getType());
            bean.setField(fieldBean);
            list.add(bean);
        }
        viewModel.setFactorList(list);
    }

    /**
     * 借记卡预交易响应数据
     *
     * @param result
     * @return
     */
    private void resultModelToViewModel(PsnDebitcardLossReportConfirmResult result) {
        LossViewModel viewModel = LossFragment.getViewModel();
        viewModel.set_certDN(result.get_certDN());
        viewModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        viewModel.set_plainData(result.get_plainData());
        List<FactorBean> list = new ArrayList<FactorBean>();
        for (PsnDebitcardLossReportConfirmResult.FactorListBean item : result.getFactorList()) {
            FactorBean bean = new FactorBean();
            FactorBean.FieldBean fieldBean = new FactorBean.FieldBean();
            fieldBean.setName(item.getField().getName());
            fieldBean.setType(item.getField().getType());
            bean.setField(fieldBean);
            list.add(bean);
        }
        viewModel.setFactorList(list);
    }

    /**
     * 信用卡预交易响应数据
     *
     * @param result
     * @return
     */
    private void resultModelToViewModel(PsnCrcdReportLossConfirmResult result) {
        LossViewModel viewModel = LossFragment.getViewModel();
        viewModel.set_certDN(result.get_certDN());// CA的DN值
        viewModel.setSmcTrigerInterval(result.getSmcTrigerInterval());// 手机验证码有效时间
        viewModel.set_plainData(result.get_plainData());
        List<FactorBean> list = new ArrayList<FactorBean>();
        for (PsnCrcdReportLossConfirmResult.FactorListBean item : result.getFactorList()) {
            FactorBean bean = new FactorBean();
            FactorBean.FieldBean fieldBean = new FactorBean.FieldBean();
            fieldBean.setName(item.getField().getName());
            fieldBean.setType(item.getField().getType());
            bean.setField(fieldBean);
            list.add(bean);
        }
        viewModel.setFactorList(list);
    }

    /**
     * 借记卡提交交易响应数据
     *
     * @param result
     * @return
     */
    private LossViewModel resultModelToViewModel(PsnDebitcardLossReportResultResult result) {
        LossViewModel lossViewModel = LossFragment.getViewModel();
        lossViewModel.setReportLossStatus(result.isReportLossStatus());
        lossViewModel.setAccFrozenStatus(result.isAccFrozenStatus());
        return lossViewModel;
    }

    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }

}
