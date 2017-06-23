package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferPre.PsnMobileTransferPreParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferPre.PsnMobileTransferPreResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferSubmit.PsnMobileTransferSubmitParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferSubmit.PsnMobileTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui.PhoneConfirmContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/7/27.
 */
public class PhoneConfirmPresenter implements PhoneConfirmContact.Presenter {

    private PhoneConfirmContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private TransferService psnPhoneService;
    private PhoneConfirmModel uiModel;
    private GlobalService globalService;
    private AccountService accountService;

    public PhoneConfirmPresenter(PhoneConfirmContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        psnPhoneService = new TransferService();
        uiModel = view.getModel();
        globalService = new GlobalService();
        accountService = new AccountService();
    }

    @Override
    public void psnMobileTransferPre() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(uiModel.getPhoneEditModel().getConversationId());
        ((BussFragment) view).showLoadingDialog();
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnMobileTransferPreResult>>() {
                    @Override
                    public Observable<PsnMobileTransferPreResult> call(String s) {
                        PsnMobileTransferPreParam param = new PsnMobileTransferPreParam();
                        param.setConversationId(uiModel.getPhoneEditModel().getConversationId());
                        param.set_combinId(uiModel.getPhoneEditModel().getSelectedFactorId());
                        param.setPayeeName(uiModel.getPhoneEditModel().getPayeeName());
                        param.setAmount(uiModel.getPhoneEditModel().getTrfAmount());
                        param.setCurrency("001");
                        param.setExecuteType("0");
                        param.setFromAccountId(uiModel.getPhoneEditModel().getAccountId());
                        param.setPayeeMobile(uiModel.getPhoneEditModel().getPayeeMobile().replaceAll(" ", ""));
                        param.setRemark(uiModel.getPhoneEditModel().getTips());
                        param.setToken(s);
                        uiModel.getPhoneEditModel().setTokenId(s);
                        return psnPhoneService.psnMobileTransferPre(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnMobileTransferPreResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileTransferPreResult result) {
                        uiModel.getPhoneEditModel().setPayeeAccountNum(result.getPayeeActno());
                        uiModel.getPhoneEditModel().setIsHaveAccount(result.getIsHaveAcct());
                        uiModel.getPhoneEditModel().setNeedPassword(result.getNeedPassword());
                        uiModel.setPrefactorList(result.getFactorList());
                        uiModel.setmPlainData(result.get_plainData());
                        uiModel.getPhoneEditModel().setPayeeBankNum(result.getPayeeBankNum());
                        uiModel.getPhoneEditModel().setToAccountType(result.getToAccountType());
                        SecurityVerity.getInstance().setConversationId(uiModel.getPhoneEditModel().getConversationId());
                        EShieldVerify.getInstance(((BussFragment) view).getActivity()).setmPlainData(uiModel.getmPlainData());

                        if (uiModel.getPhoneEditModel().getIsHaveAccount().equals("0")) {
                            ((BussFragment) view).closeProgressDialog();
                        }

                        view.psnMobileTransferPreReturned();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });

    }

    @Override
    public void psnTransGetBocTransferCommissionCharge() {
        PsnTransGetBocTransferCommissionChargeParams params = new PsnTransGetBocTransferCommissionChargeParams();
        params.setServiceId("PB035");
        params.setFromAccountId(uiModel.getPhoneEditModel().getAccountId());
        params.setCurrency(uiModel.getPhoneEditModel().getRemainCurrency());
        params.setAmount(uiModel.getPhoneEditModel().getTrfAmount());
        params.setRemark(uiModel.getPhoneEditModel().getTips());
        params.setPayeeActno(uiModel.getPhoneEditModel().getPayeeAccountNum());
        params.setPayeeName(uiModel.getPhoneEditModel().getPayeeName());
        params.setConversationId(uiModel.getPhoneEditModel().getConversationId());
        psnPhoneService.psnTransGetBocTransferCommissionCharge(params)
                .compose(mRxLifecycleManager.<PsnTransGetBocTransferCommissionChargeResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnTransGetBocTransferCommissionChargeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransGetBocTransferCommissionChargeResult result) {
                        if (!"0".equals(result.getGetChargeFlag())) {
                            uiModel.setCommisionCharge(result.getPreCommissionCharge().toString());
                            view.psnTransGetBocTransferCommissionChargeReturned();
                        } else {
                            uiModel.setCommisionCharge("手续费查询失败，请以实际扣收为准");
                            view.psnTransGetBocTransferCommissionChargeReturned();
                        }
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnMobileTransferSubmit() {
        ((BussFragment) view).showLoadingDialog();

        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(uiModel.getPhoneEditModel().getConversationId());
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnMobileTransferSubmitResult>>() {
                    @Override
                    public Observable<PsnMobileTransferSubmitResult> call(String s) {

                        PsnMobileTransferSubmitParam param = new PsnMobileTransferSubmitParam();
                        param.setConversationId(uiModel.getPhoneEditModel().getConversationId());
                        param.set_combinId(uiModel.getPhoneEditModel().getSelectedFactorId());
                        param.setPayeeName(uiModel.getPhoneEditModel().getPayeeName());
                        param.setAmount(uiModel.getPhoneEditModel().getTrfAmount());
                        param.setCurrency("001");
                        param.setExecuteType("0");
                        param.setToAccountType(uiModel.getPhoneEditModel().getToAccountType());
                        param.setPayeeBankNum(uiModel.getPhoneEditModel().getPayeeBankNum());
                        param.setFromAccountId(uiModel.getPhoneEditModel().getAccountId());
                        param.setPayeeMobile(uiModel.getPhoneEditModel().getPayeeMobile());
                        param.setRemark(uiModel.getPhoneEditModel().getTips());
                        param.setIsHaveAcct(uiModel.getPhoneEditModel().getIsHaveAccount());
                        param.setToken(s);
                        if (uiModel.getPhoneEditModel().getSelectedFactorId().equals("8")) {
                            param.setOtp(uiModel.getEncryptPasswords()[0]);
                            param.setOtp_RC(uiModel.getEncryptRandomNums()[0]);
                        } else if (uiModel.getPhoneEditModel().getSelectedFactorId().equals("32")) {
                            param.setSmc(uiModel.getEncryptPasswords()[0]);
                            param.setSmc_RC(uiModel.getEncryptRandomNums()[0]);
                        } else if (uiModel.getPhoneEditModel().getSelectedFactorId().equals("40")) {
                            param.setOtp(uiModel.getEncryptPasswords()[0]);
                            param.setOtp_RC(uiModel.getEncryptRandomNums()[0]);
                            param.setSmc(uiModel.getEncryptPasswords()[1]);
                            param.setSmc_RC(uiModel.getEncryptRandomNums()[1]);
                        } else if (uiModel.getPhoneEditModel().getSelectedFactorId().equals("96")) {
                            param.setSmc(uiModel.getEncryptPasswords()[0]);
                            param.setSmc_RC(uiModel.getEncryptRandomNums()[0]);
                            DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(((BussFragment) view).getActivity(),
                                    uiModel.getPhoneEditModel().getRandomNum());
                            param.setDeviceInfo(info.getDeviceInfo());
                            param.setDeviceInfo_RC(info.getDeviceInfo_RC());
                            param.setDevicePrint(DeviceInfoUtils.getDevicePrint(((BussFragment) view).getActivity()));
                        } else if (uiModel.getPhoneEditModel().getSelectedFactorId().equals("4")) {
                            param.set_signedData(uiModel.getmSignData());
                        }

                        if (uiModel.getPhoneEditModel().getNeedPassword().equals("0")) {
                        } else if (uiModel.getPhoneEditModel().getNeedPassword().equals("1")) {
                            param.setAtmPassword(uiModel.getEncryptPasswordsPass()[0]);
                            param.setAtmPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                        } else if (uiModel.getPhoneEditModel().getNeedPassword().equals("2")) {
                            param.setPhoneBankPassword(uiModel.getEncryptPasswordsPass()[0]);
                            param.setPhoneBankPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                        } else if (uiModel.getPhoneEditModel().getNeedPassword().equals("3")) {
                            param.setPassbookPassword(uiModel.getEncryptPasswordsPass()[0]);
                            param.setPassbookPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                        }
                        param.setActiv(SecurityVerity.getInstance().getCfcaVersion());
                        param.setState(SecurityVerity.SECURITY_VERIFY_STATE);
                        return psnPhoneService.psnMobileTransferSubmit(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnMobileTransferSubmitResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileTransferSubmitResult result) {
                        ((BussFragment) view).closeProgressDialog();
                        uiModel.setTransNum(result.getTransactionId());
                        uiModel.setFromIbkNum(result.getFromIbkNum());
                        uiModel.setCommisionCharge(result.getCommissionCharge() + "");
                        view.psnMobileTransferSubmitReturned();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }


                });
    }

    @Override
    public void queryCreditAccountDetail(String accountId) {
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
                        ((BussFragment) view).closeProgressDialog();
                        view.queryCreditAccountDetailReturned();
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
                            uiModel.setRemainCurrency(bean.getCurrency());
                        } else {
                            uiModel.setQueryStates(false);
                            uiModel.setWarning("该账户无人民币余额，请修改");
                            uiModel.setWarnType("1");
                        }
                        ((BussFragment) view).closeProgressDialog();
                        view.queryCreditAccountDetailReturned();
                    }
                });
    }

    @Override
    public void psnAccountQueryAccountDetail() {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(uiModel.getPhoneEditModel().getAccountId());
        ((BussFragment) view).showLoadingDialog();
        accountService.psnAccountQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        List<PsnAccountQueryAccountDetailResult.AccountDetaiListBean> detaiList = result.getAccountDetaiList();
                        // 是否有人民币余额
                        boolean isHaveRMB = false;
                        PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean = null;
                        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean item : detaiList) {
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
                            uiModel.setRemainCurrency(bean.getCurrencyCode());
                        } else {
                            uiModel.setQueryStates(false);
                            uiModel.setWarning("该账户无人民币余额，请修改");
                            uiModel.setWarnType("1");
                        }
                        view.psnAccountQueryAccountDetailReturned();
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
