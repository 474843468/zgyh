package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChange.PsnSsmAccountChangeParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChange.PsnSsmAccountChangeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChangePre.PsnSsmAccountChangePreParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChangePre.PsnSsmAccountChangePreResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
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
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.EditFeeAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.EditFeeAccountContact;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/8/11.
 */
public class EditFeeAccountPresenter implements EditFeeAccountContact.Presenter {
    private EditFeeAccountContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private EditFeeAccountModel uiModel;
    private AccountService accountService;
    private String conversationId;

    public EditFeeAccountPresenter(EditFeeAccountContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        uiModel = view.getUiModel();
        accountService = new AccountService();
    }

    @Override
    public void psnSsmAccountChangePre() {
        PsnSsmAccountChangePreParam param = new PsnSsmAccountChangePreParam();
        param.set_combinId(uiModel.getSelectedFactorId());
        param.setAccountId(uiModel.getSignedAccount().getAccountId());
        param.setAccountNumber(uiModel.getSignedAccount().getAccountNumber());
        param.setConversationId(conversationId);
        ((BussFragment) view).showLoadingDialog(false);
        accountService.psnSsmAccountChangePre(param)
                .compose(mRxLifecycleManager.<PsnSsmAccountChangePreResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnSsmAccountChangePreResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnSsmAccountChangePreResult result) {
                        uiModel.setmPlainData(result.get_plainData());
                        uiModel.setPrefactorList(result.getFactorList());

                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(conversationId);
                        SecurityVerity.getInstance().setConversationId(conversationId);
                        return globalService.psnGetRandom(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String result) {
                        uiModel.setRandomNum(result);
                        ((BussFragment) view).closeProgressDialog();
                        view.psnSsmAccountChangePreReturned();
                    }
                });
    }

    @Override
    public void psnSsmAccountChange() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(conversationId);
        ((BussFragment) view).showLoadingDialog(false);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnSsmAccountChangeResult>>() {
                    @Override
                    public Observable<PsnSsmAccountChangeResult> call(String s) {
                        PsnSsmAccountChangeParam param = new PsnSsmAccountChangeParam();
                        param.setConversationId(conversationId);
                        param.setAccountId(uiModel.getSignedAccount().getAccountId());
                        param.setAccountNumber(uiModel.getSignedAccount().getAccountNumber());
                        param.setFeeAccountNum(uiModel.getAccountOld());
                        param.setSsmfeeAccountId(uiModel.getAccountNew().getAccountId());
                        param.setState(SecurityVerity.SECURITY_VERIFY_STATE);
                        param.setActiv(SecurityVerity.getInstance().getCfcaVersion());
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
                        } else if (uiModel.getSelectedFactorId().equals("4")) {
                            param.set_signedData(uiModel.getmSignedData());
                        }
                        return accountService.psnSsmAccountChange(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnSsmAccountChangeResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnSsmAccountChangeResult psnSsmAccountChangeResult) {
                        ((BussFragment) view).closeProgressDialog();
                        view.psnSsmAccountChangeReturned();
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
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        ((BussFragment) view).showLoadingDialog(false);
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
                        mSecurityFactorParams.setServiceId("PB174C");
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
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }
}
