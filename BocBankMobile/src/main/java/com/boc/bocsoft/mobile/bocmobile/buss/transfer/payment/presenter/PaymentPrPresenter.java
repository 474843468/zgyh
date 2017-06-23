package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit.PsnTransActPaymentSubmitParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit.PsnTransActPaymentSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify.PsnTransActPaymentVerifyParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify.PsnTransActPaymentVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentPreModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui.PaymentPreContact;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import org.threeten.bp.LocalDateTime;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/6/30.
 */
public class PaymentPrPresenter implements PaymentPreContact.Presenter {

    private PaymentPreContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private TransferService psnPaymentService;
    private PaymentPreModel uiModel;
    private GlobalService globalService;
    private String conversationId;
    private LocalDateTime currentTime;
    private AccountService accountService;

    public PaymentPrPresenter(PaymentPreContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        psnPaymentService = new TransferService();
        accountService = new AccountService();
        uiModel = view.getModel();
        globalService = new GlobalService();
    }

    @Override
    public void psnTransActPaymentVerify() {
        PsnCommonQuerySystemDateTimeParams params = new PsnCommonQuerySystemDateTimeParams();
        ((BussFragment) view).showLoadingDialog();
        GlobalService.psnCommonQuerySystemDateTime(params)
                .compose(mRxLifecycleManager.<PsnCommonQuerySystemDateTimeResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnCommonQuerySystemDateTimeResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnCommonQuerySystemDateTimeResult result) {
                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(conversationId);
                        currentTime = result.getDateTme();
                        SecurityVerity.getInstance().setConversationId(conversationId);
                        return globalService.psnGetRandom(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnTransActPaymentVerifyResult>>() {
                    @Override
                    public Observable<PsnTransActPaymentVerifyResult> call(String s) {
                        uiModel.setRandomNum(s);
                        PsnTransActPaymentVerifyParam params = new PsnTransActPaymentVerifyParam();
                        params.setConversationId(conversationId);
                        params.set_combinId(uiModel.getSelectedFactorId());
                        params.setNotifyId(uiModel.getNotifyId());
                        params.setFromAccountId(uiModel.getPayerAccountId());
                        params.setNotifyCreateChannel("2");
                        params.setNotifyCreateDate(uiModel.getCreateDate());
                        params.setNotifyCurrentDate(currentTime.toLocalDate().format(DateFormatters.dateFormatter1));
                        params.setNotifyRequestAmount(uiModel.getRequestAmount() + "");
                        params.setNotifyTrfAmount(uiModel.getTrfAmount());
                        params.setNotifyTrfCur(uiModel.getTrfCur());
                        params.setPayeeActno(uiModel.getPayeeAccountNumber());
                        params.setPayeeMobile(uiModel.getPayeeMobile());
                        params.setPayeeName(uiModel.getPayeeName());
                        params.setPayerCustId(uiModel.getPayerCustId());
                        params.setPayerMobile(uiModel.getPayerMobile());
                        params.setPayerName(uiModel.getPayerName());
                        return psnPaymentService.psnTransActPaymentVerify(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnTransActPaymentVerifyResult, Observable<PsnTransGetBocTransferCommissionChargeResult>>() {
                    @Override
                    public Observable<PsnTransGetBocTransferCommissionChargeResult> call(PsnTransActPaymentVerifyResult result) {
                        uiModel.setPrefactorList(result.getFactorList());
                        uiModel.setNeedPassword(result.getNeedPassword());
                        PsnTransGetBocTransferCommissionChargeParams params = new PsnTransGetBocTransferCommissionChargeParams();
                        params.setServiceId("PB037C");
                        params.setFromAccountId(uiModel.getPayerAccountId());
                        params.setCurrency(uiModel.getTrfCur());
                        params.setAmount(uiModel.getTrfAmount());
                        params.setCashRemit("02");
                        params.setNotifyId(uiModel.getNotifyId());
                        params.setRemark(uiModel.getFurInfo());
                        return psnPaymentService.psnTransGetBocTransferCommissionCharge(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnTransGetBocTransferCommissionChargeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showLoadingDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransGetBocTransferCommissionChargeResult result) {
                        if (result.getGetChargeFlag().equals("1")) {
                            uiModel.setDiscountAmount(result.getPreCommissionCharge().toString());
                        } else {
                            uiModel.setDiscountAmount("");
                        }
                        view.psnTransActPaymentVerifyReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnTransActPaymentSubmit() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(conversationId);
        ((BussFragment) view).showLoadingDialog();
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnTransActPaymentSubmitResult>>() {
                    @Override
                    public Observable<PsnTransActPaymentSubmitResult> call(String s) {
                        PsnTransActPaymentSubmitParam param = new PsnTransActPaymentSubmitParam();
                        param.setConversationId(conversationId);
                        param.setNotifyId(uiModel.getNotifyId());
                        param.setFromAccountId(uiModel.getPayerAccountId());
                        param.setNotifyCreateChannel("2");
                        param.setNotifyCreateDate(uiModel.getCreateDate());
                        param.setNotifyCurrentDate(currentTime.toLocalDate().format(DateFormatters.dateFormatter1));
                        param.setNotifyRequestAmount(uiModel.getRequestAmount() + "");
                        param.setNotifyTrfAmount(uiModel.getTrfAmount());
                        param.setNotifyTrfCur(uiModel.getTrfCur());
                        param.setPayeeActno(uiModel.getPayeeAccountNumber());
                        param.setPayeeMobile(uiModel.getPayeeMobile());
                        param.setPayeeName(uiModel.getPayeeName());
                        param.setPayerCustId(uiModel.getPayerCustId());
                        param.setPayerMobile(uiModel.getPayerMobile());
                        param.setPayerName(uiModel.getPayerName());
                        param.setToken(s);
                        if (uiModel.getSelectedFactorId().equals("8")) {
                            param.setOtp(uiModel.getEncryptPasswords()[0]);
                            param.setOtp_RC(uiModel.getEncryptRandomNums()[0]);
                        } else if (uiModel.getSelectedFactorId().equals("32")) {
                            param.setSmc(uiModel.getEncryptPasswords()[0]);
                            param.setSmc_RC(uiModel.getEncryptRandomNums()[0]);
                        } else if (uiModel.getSelectedFactorId().equals("40")) {
                            param.setOtp(uiModel.getEncryptPasswords()[0]);
                            param.setOtp_RC(uiModel.getEncryptRandomNums()[0]);
                            param.setSmc(uiModel.getEncryptPasswords()[1]);
                            param.setSmc_RC(uiModel.getEncryptRandomNums()[1]);
                        } else if (uiModel.getSelectedFactorId().equals("96")) {
                            param.setSmc(uiModel.getEncryptPasswords()[0]);
                            param.setSmc_RC(uiModel.getEncryptRandomNums()[0]);
                            DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(((BussFragment) view).getActivity(),
                                    uiModel.getRandomNum());
                            param.setDeviceInfo(info.getDeviceInfo());
                            param.setDeviceInfo_RC(info.getDeviceInfo_RC());
                            param.setDevicePrint(DeviceInfoUtils.getDevicePrint(((BussFragment) view).getActivity()));
                        }

                        if (uiModel.getNeedPassword().equals("0")) {

                        } else if (uiModel.getNeedPassword().equals("1")) {
                            param.setAtmPassword(uiModel.getEncryptPasswordsPass()[0]);
                            param.setAtmPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                        } else if (uiModel.getNeedPassword().equals("2")) {
                            param.setPhoneBankPassword(uiModel.getEncryptPasswordsPass()[0]);
                            param.setPhoneBankPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                        } else if (uiModel.getNeedPassword().equals("3")) {
                            param.setPassbookPassword(uiModel.getEncryptPasswordsPass()[0]);
                            param.setPassbookPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                        }

                        param.setActiv(SecurityVerity.getInstance().getCfcaVersion());
                        param.setState(SecurityVerity.SECURITY_VERIFY_STATE);
                        return psnPaymentService.psnTransActPaymentSubmit(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnTransActPaymentSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransActPaymentSubmitResult psnTransOrderListResult) {
                        uiModel.setTrfAmount(psnTransOrderListResult.getTrfAmount() + "");
                        uiModel.setDiscountAmount(psnTransOrderListResult.getCommissionCharge() + "");
                        uiModel.setRequestAmount(psnTransOrderListResult.getRequestAmount() + "");
                        uiModel.setPayChanel(psnTransOrderListResult.getTrfChannel());
                        uiModel.setTransferNum(psnTransOrderListResult.getTransactionId());
                        uiModel.setPayeeIbk(psnTransOrderListResult.getPayeeIbk());
                        uiModel.setPayDate(psnTransOrderListResult.getPaymentDate());
                        view.psnTransActPaymentSubmitReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnAccountQueryAccountDetail() {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(uiModel.getPayerAccountId());
        ((BussFragment) view).showLoadingDialog();
        accountService.psnAccountQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        uiModel.setQueryStates(false);
                        view.psnAccountQueryAccountDetailReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        // 是否有人民币余额
                        boolean isHaveRMB = false;
                        PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean = null;
                        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean item : result.getAccountDetaiList()) {
                            if ("001".equals(item.getCurrencyCode())) {
                                isHaveRMB = true;
                                bean = item;
                                break;
                            } else {
                                isHaveRMB = false;
                            }
                        }
                        if (isHaveRMB) {
                            uiModel.setQueryStates(true);
                            uiModel.setRemainAmount(bean.getAvailableBalance());
                            uiModel.setTrfCur(bean.getCurrencyCode());
                        } else {
                            uiModel.setQueryStates(false);
                        }
                        view.psnAccountQueryAccountDetailReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void queryCreditAccountDetail(String accountId) {
        ((BussFragment) view).showLoadingDialog();
        PsnCrcdQueryAccountDetailParams params = new PsnCrcdQueryAccountDetailParams();
        params.setAccountId(accountId);
        params.setCurrency("001");
        accountService.psnCrcdQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnCrcdQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        uiModel.setQueryStates(false);
                        view.queryCreditAccountDetailReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryAccountDetailResult result) {
                        // 是否有人民币余额
                        boolean isHaveRMB = false;
                        PsnCrcdQueryAccountDetailResult.CrcdAccountDetailListBean bean = null;
                        for (PsnCrcdQueryAccountDetailResult.CrcdAccountDetailListBean item : result.getCrcdAccountDetailList()) {
                            if ("001".equals(item.getCurrency())) {
                                isHaveRMB = true;
                                bean = item;
                                break;
                            } else {
                                isHaveRMB = false;
                            }
                        }
                        if (isHaveRMB) {
                            uiModel.setQueryStates(true);
                            uiModel.setRemainAmount(bean.getLoanBalanceLimit());
                            uiModel.setTrfCur(bean.getCurrency());
                        } else {
                            uiModel.setQueryStates(false);
                        }
                        ((BussFragment) view).closeProgressDialog();
                        view.queryCreditAccountDetailReturned();
                    }
                });

    }

    @Override
    public void psnGetSecurityFactor() {
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        ((BussFragment) view).showLoadingDialog();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String s) {
                        PsnGetSecurityFactorParams mSecurityFactorParams = new PsnGetSecurityFactorParams();
                        conversationId = s;
                        mSecurityFactorParams.setConversationId(s);
                        mSecurityFactorParams.setServiceId("PB037C");
                        return globalService.psnGetSecurityFactor(mSecurityFactorParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult result) {
                        SecurityFactorModel factor = new SecurityFactorModel(result);
                        uiModel.setFactorModel(factor);
                        view.securityFactorReturned();
                        ((BussFragment) view).closeProgressDialog();
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
