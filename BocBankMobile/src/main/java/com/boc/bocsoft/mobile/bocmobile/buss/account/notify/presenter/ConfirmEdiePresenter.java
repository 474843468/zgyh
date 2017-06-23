package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMade.PsnSsmMadeParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMadePre.PsnSsmMadePreParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMadePre.PsnSsmMadePreResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSign.PsnSsmSignParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSign.PsnSsmSignResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSignPre.PsnSsmSignPreParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSignPre.PsnSsmSignPreResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChange.PsnSsmMessageChangeParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChange.PsnSsmMessageChangeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChangePre.PsnSsmMessageChangePreParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChangePre.PsnSsmMessageChangePreResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.ConfirmEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.ConfirmEdieContact;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/6/25.
 */
public class ConfirmEdiePresenter implements ConfirmEdieContact.Presenter {

    private ConfirmEdieContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private AccountService psnNotifyService;
    private GlobalService globalService;
    private ConfirmEditModel uiModel;
    private AccountService accountService;
    private String token;

    public ConfirmEdiePresenter(ConfirmEdieContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        psnNotifyService = new AccountService();
        globalService = new GlobalService();
        uiModel = view.getUiModel();
        accountService = new AccountService();
    }

    @Override
    public void psnSsmMessageChangePre() {
        PsnSsmMessageChangePreParam param = new PsnSsmMessageChangePreParam();
        param.set_combinId(uiModel.getSelectedFactorId());
        param.setConversationId(uiModel.getEditModel().getConversitionId());
        param.setAccountNumber(uiModel.getEditModel().getSignAccount().getAccountNumber());
        param.setAccountId(uiModel.getEditModel().getSignAccount().getAccountId());
        param.setMobileConfirmCode(uiModel.getEditModel().getVerifyCode());
        param.setPushterm(uiModel.getOldEditModel().getPhoneNum());
        param.setTelnumnew(uiModel.getEditModel().getPhoneNum());
        ((BussFragment) view).showLoadingDialog(false);
        accountService.psnSsmMessageChangePre(param)
                .compose(mRxLifecycleManager.<PsnSsmMessageChangePreResult>bindToLifecycle())
                .flatMap(new Func1<PsnSsmMessageChangePreResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnSsmMessageChangePreResult psnSsmMadePreResult) {
                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(uiModel.getEditModel().getConversitionId());
                        if (psnSsmMadePreResult.get_plainData() != null) {
                            uiModel.setmPlainData(psnSsmMadePreResult.get_plainData().toString());
                        }
                        uiModel.setPreFactorList(psnSsmMadePreResult.getFactorList());
                        return globalService.psnGetRandom(params);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String s) {
                        uiModel.setRandomNum(s);
                        view.smsMessageChangePreReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });
    }

    @Override
    public void psnSsmMessageChange() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(uiModel.getEditModel().getConversitionId());
        ((BussFragment) view).showLoadingDialog(false);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnSsmMessageChangeResult>>() {
                    @Override
                    public Observable<PsnSsmMessageChangeResult> call(String s) {
                        PsnSsmMessageChangeParam param = new PsnSsmMessageChangeParam();
                        param.setConversationId(uiModel.getEditModel().getConversitionId());
                        param.setAccountId(uiModel.getEditModel().getSignAccount().getAccountId());
                        param.setAccountNumber(uiModel.getEditModel().getSignAccount().getAccountNumber());
                        param.setSsmserviceid("S001");
                        param.setPushchannel("1");
                        param.setPushterm(uiModel.getOldEditModel().getPhoneNum());
                        param.setLoweramount(uiModel.getOldEditModel().getMinMoney());
                        param.setUpperamount(uiModel.getOldEditModel().getMaxMoney());
                        param.setChanlTypenew("1");
                        param.setTelnumnew(uiModel.getEditModel().getPhoneNum());
                        param.setDownLimitnew(uiModel.getEditModel().getMinMoney());
                        param.setUpLimitnew(uiModel.getEditModel().getMaxMoney());
                        param.setToken(s);
                        param.setActiv(SecurityVerity.getInstance().getCfcaVersion());
                        param.setState(SecurityVerity.SECURITY_VERIFY_STATE);
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
                        } else if (uiModel.getSelectedFactorId().equals("4")) {
                            param.set_signedData(uiModel.getmSignData());
                        }
                        return psnNotifyService.psnSsmMessageChange(param);
                    }
                })
                .compose(SchedulersCompat.<PsnSsmMessageChangeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnSsmMessageChangeResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnSsmMessageChangeResult s) {
                        view.smsMessageChangeReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });
    }


    @Override
    public void psnSsmSignPre() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(uiModel.getEditModel().getConversitionId());
        ((BussFragment) view).showLoadingDialog(false);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnSsmSignPreResult>>() {
                    @Override
                    public Observable<PsnSsmSignPreResult> call(String s) {
                        PsnSsmSignPreParam param = new PsnSsmSignPreParam();
                        param.set_combinId(uiModel.getSelectedFactorId());
                        param.setConversationId(uiModel.getEditModel().getConversitionId());
                        param.setAccountId(uiModel.getEditModel().getSignAccount().getAccountId());
                        param.setAccountNumber(uiModel.getEditModel().getSignAccount().getAccountNumber());
                        param.setToken(s);
                        token = s;
                        return accountService.snSsmSignPre(param);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnSsmSignPreResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnSsmSignPreResult psnSsmMadePreResult) {
                        uiModel.setPreFactorList(psnSsmMadePreResult.getFactorList());
                        uiModel.setmPlainData(psnSsmMadePreResult.get_plainData());

                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(uiModel.getEditModel().getConversitionId());
                        return globalService.psnGetRandom(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<String>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String result) {
                        uiModel.setRandomNum(result);
                        view.psnSsmSignPreReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void handleException(BiiResultErrorException exception) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(exception.getErrorMessage());
                    }
                });
    }

    @Override
    public void psnSsmSign() {
        PsnSsmSignParam param = new PsnSsmSignParam();
        ((BussFragment) view).showLoadingDialog(false);
        param.setConversationId(uiModel.getEditModel().getConversitionId());
        param.setAccountId(uiModel.getEditModel().getSignAccount().getAccountId());
        param.setAccountNumber(uiModel.getEditModel().getSignAccount().getAccountNumber());
        param.setFeeAccountNum(uiModel.getEditModel().getFeeAccount().getAccountNumber());
        param.setSsmfeeAccountId(uiModel.getEditModel().getFeeAccount().getAccountId());
        param.setAccAlias(uiModel.getEditModel().getSignAccount().getAccountName());
        param.setAccountInfo(uiModel.getEditModel().getSignAccount().getCardDescription());
        param.setFeestandard(uiModel.getEditModel().getFeeRate());
        param.setFeetype("00");
        param.setHighloweramount(0);
        param.setOpenStatus(false);
        param.setIsFree(false);
        param.set_combinId(uiModel.getSelectedFactorId());
        PsnSsmSignParam.ListBean bean = new PsnSsmSignParam.ListBean();
        bean.setPushterm(uiModel.getEditModel().getPhoneNum());
        bean.setAccountId(uiModel.getEditModel().getSignAccount().getAccountId());
        bean.setFeestandard(uiModel.getEditModel().getFeeRate());
        bean.setFeetype("00");
        bean.setPushchannel("1");
        bean.setMobileConfirmCode(uiModel.getEditModel().getVerifyCode());
        bean.setSsmserviceid("S001");
        bean.setLoweramount(uiModel.getEditModel().getMinMoney());
        bean.setUpperamount(uiModel.getEditModel().getMaxMoney());
        List<PsnSsmSignParam.ListBean> list = new ArrayList<>();
        list.add(bean);
        param.setList(list);
        param.setToken(token);
        param.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        param.setState(SecurityVerity.SECURITY_VERIFY_STATE);
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
        } else if (uiModel.getSelectedFactorId().equals("4")) {
            param.set_signedData(uiModel.getmSignData());
        }
        psnNotifyService.psnSsmSign(param)
                .compose(mRxLifecycleManager.<PsnSsmSignResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnSsmSignResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnSsmSignResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnSsmSignResult result) {
                        view.psnSsmSignReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });
    }

    @Override
    public void psnSsmMadePre() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(uiModel.getEditModel().getConversitionId());
        ((BussFragment) view).showLoadingDialog(false);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnSsmMadePreResult>>() {
                    @Override
                    public Observable<PsnSsmMadePreResult> call(String s) {
                        PsnSsmMadePreParam param = new PsnSsmMadePreParam();
                        param.set_combinId(uiModel.getSelectedFactorId());
                        param.setConversationId(uiModel.getEditModel().getConversitionId());
                        param.setAccountId(uiModel.getEditModel().getSignAccount().getAccountId());
                        param.setAccountNumber(uiModel.getEditModel().getSignAccount().getAccountNumber());
                        token = s;
                        param.setToken(token);
                        return accountService.psnSsmMadePre(param);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnSsmMadePreResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnSsmMadePreResult psnSsmMadePreResult) {
                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(uiModel.getEditModel().getConversitionId());
                        uiModel.setPreFactorList(psnSsmMadePreResult.getFactorList());
                        uiModel.setmPlainData(psnSsmMadePreResult.get_plainData());
                        return globalService.psnGetRandom(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<String>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String result) {
                        uiModel.setRandomNum(result);
                        view.smsMadePreReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void handleException(BiiResultErrorException exception) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(exception.getErrorMessage());
                    }
                });
    }

    /**
     * 短信验证方式提交
     */
    @Override
    public void psnSsmMade() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(uiModel.getEditModel().getConversitionId());
        ((BussFragment) view).showLoadingDialog(false);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        PsnSsmMadeParam param = new PsnSsmMadeParam();
                        param.setConversationId(uiModel.getEditModel().getConversitionId());
                        param.setAccountId(uiModel.getEditModel().getSignAccount().getAccountId());
                        param.setAccountNumber(uiModel.getEditModel().getSignAccount().getAccountNumber());
                        param.setAccAlias(uiModel.getEditModel().getSignAccount().getAccountName());
                        param.setAccountInfo(uiModel.getEditModel().getSignAccount().getCardDescription());
                        param.setFeestandard(uiModel.getEditModel().getFeeRate());
                        param.setFeetype("00");
                        param.setFeeAccountNum(uiModel.getEditModel().getFeeAccount().getAccountNumber());
                        param.setHighloweramount(0);
                        param.setOpenStatus(true);
                        param.setOpIsFree(true);
                        param.setIsFree(false);
                        param.set_combinId(uiModel.getSelectedFactorId());
                        PsnSsmMadeParam.ListBean bean = new PsnSsmMadeParam.ListBean();
                        bean.setPushterm(uiModel.getEditModel().getPhoneNum());
                        bean.setAccountId(uiModel.getEditModel().getSignAccount().getAccountId());
                        bean.setFeestandard(uiModel.getEditModel().getFeeRate());
                        bean.setFeetype("00");
                        bean.setPushchannel("1");
                        bean.setMobileConfirmCode(uiModel.getEditModel().getVerifyCode());
                        bean.setSsmserviceid("S001");
                        bean.setLoweramount(uiModel.getEditModel().getMinMoney());
                        bean.setUpperamount(uiModel.getEditModel().getMaxMoney());
                        List<PsnSsmMadeParam.ListBean> list = new ArrayList<>();
                        list.add(bean);
                        param.setList(list);
                        param.setToken(token);
                        param.setActiv(SecurityVerity.getInstance().getCfcaVersion());
                        param.setState(SecurityVerity.SECURITY_VERIFY_STATE);
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
                        } else if (uiModel.getSelectedFactorId().equals("4")) {
                            param.set_signedData(uiModel.getmSignData());
                        }
                        return psnNotifyService.psnSsmMade(param);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String s) {
                        view.smsMadeReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });
    }

    @Override
    public void psnGetSecurityFactor() {
        PsnGetSecurityFactorParams mSecurityFactorParams = new PsnGetSecurityFactorParams();
        mSecurityFactorParams.setConversationId(uiModel.getEditModel().getConversitionId());
        mSecurityFactorParams.setServiceId(uiModel.getEditModel().getServiceId());
        ((BussFragment) view).showLoadingDialog(false);
        globalService.psnGetSecurityFactor(mSecurityFactorParams)
                .compose(mRxLifecycleManager.<PsnGetSecurityFactorResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
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
                        SecurityFactorModel securityFactorModel = ModelUtil.generateSecurityFactorModel(result);
                        uiModel.setFactorModel(securityFactorModel);
                        view.securityFactorReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
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
