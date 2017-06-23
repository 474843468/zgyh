package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeConfirmContract;
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
public class QrcodeConfirmPresenter implements QrcodeConfirmContract.Presenter {

    private QrcodeConfirmContract.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private TransferService transferService;
    private QrcodeConfirmModel uiModel;
    private GlobalService globalService;
    private AccountService accountService;

    public QrcodeConfirmPresenter(QrcodeConfirmContract.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        transferService = new TransferService();
        uiModel = view.getModel();
        globalService = new GlobalService();
        accountService = new AccountService();
    }

    @Override
    public void psnTransBocTransferVerify() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(uiModel.getQrcodeTransModel().getConversationId());
        ((BussFragment) view).showLoadingDialog();
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnTransBocTransferVerifyResult>>() {
                    @Override
                    public Observable<PsnTransBocTransferVerifyResult> call(String s) {
                        PsnTransBocTransferVerifyParams params = new PsnTransBocTransferVerifyParams();
                        params.setConversationId(uiModel.getQrcodeTransModel().getConversationId());
                        params.setServiceId("PB031");
                        params.setCurrency(ApplicationConst.CURRENCY_CNY);
                        params.set_combinId(uiModel.getQrcodeTransModel().getSelectedFactorId());
                        params.setAmount(uiModel.getQrcodeTransModel().getTrfAmount());
                        params.setExecuteType(ApplicationConst.NOWEXE);
                        params.setFromAccountId(uiModel.getQrcodeTransModel().getAccountId());
                        params.setPayeeActno(uiModel.getQrcodeTransModel().getPayeeAccount());
                        params.setPayeeMobile(uiModel.getQrcodeTransModel().getPayeeMobile());
                        params.setPayeeName(uiModel.getQrcodeTransModel().getPayeeName());
                        params.setRemark(uiModel.getQrcodeTransModel().getTips());
                        params.setToken(s);
                        return transferService.psnTransBocTransferVerify(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnTransBocTransferVerifyResult, Observable<PsnTransGetBocTransferCommissionChargeResult>>() {
                    @Override
                    public Observable<PsnTransGetBocTransferCommissionChargeResult> call(
                            PsnTransBocTransferVerifyResult result) {
                        uiModel.setPayeeAccountType(result.getToAccountType());
                        uiModel.setPayeeBankNum(result.getPayeeBankNum());
                        uiModel.setmPlainData(result.get_plainData());
                        uiModel.getQrcodeTransModel().setNeedPassword(result.getNeedPassword());
                        uiModel.setPrefactorList(result.getFactorList());

                        PsnTransGetBocTransferCommissionChargeParams chargeParams =
                                new PsnTransGetBocTransferCommissionChargeParams();
                        chargeParams.setServiceId(ApplicationConst.PB031);
                        chargeParams.setToAccountId(uiModel.getQrcodeTransModel().getPayeeAccount());
                        chargeParams.setFromAccountId(uiModel.getQrcodeTransModel().getAccountId());
                        chargeParams.setRemark(uiModel.getQrcodeTransModel().getTips());
                        chargeParams.setAmount(uiModel.getQrcodeTransModel().getTrfAmount());
                        chargeParams.setCurrency(ApplicationConst.CURRENCY_CNY);
                        chargeParams.setPayeeActno(uiModel.getQrcodeTransModel().getPayeeAccount());
                        chargeParams.setPayeeName(uiModel.getQrcodeTransModel().getPayeeName());
                        return transferService.psnTransGetBocTransferCommissionCharge(chargeParams);
                    }
                })
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
                            view.psnTransBocTransferVerifyReturned();
                        } else {
                            uiModel.setCommisionCharge("手续费查询失败，请以实际扣收为准");
                            view.psnTransBocTransferVerifyReturned();
                        }
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnTransBocTransferSubmit() {
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        ((BussFragment) view).showLoadingDialog();
        mTokenIdParams.setConversationId(uiModel.getQrcodeTransModel().getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnTransBocTransferSubmitResult>>() {
                             @Override
                             public Observable<PsnTransBocTransferSubmitResult> call(String s) {
                                 // TODO: 2016/7/9  是否需要上送atm密码，和电话银行密码要看实际情况
                                 PsnTransBocTransferSubmitParams param = new PsnTransBocTransferSubmitParams();
                                 param.setAmount((MoneyUtils.transMoneyFormat(uiModel.getQrcodeTransModel().getTrfAmount(), uiModel.getQrcodeTransModel().getTrfCurrency())).replace(",", ""));
                                 param.setCurrency("001");
                                 param.setExecuteType("0");
                                 param.setFromAccountId(uiModel.getQrcodeTransModel().getAccountId());
                                 param.setPayeeActno(uiModel.getQrcodeTransModel().getPayeeAccount());
                                 param.setPayeeName(uiModel.getQrcodeTransModel().getPayeeName());
                                 param.setPayeeMobile(uiModel.getQrcodeTransModel().getPayeeMobile());
                                 param.setPayeeBankNum(uiModel.getPayeeBankNum());
                                 param.setRemark(uiModel.getQrcodeTransModel().getTips());
                                 param.setToAccountType(uiModel.getQrcodeTransModel().getPayeeAccountType());
                                 param.setPayeeBankNum(uiModel.getQrcodeTransModel().getPayeeBankNum());
                                 param.setConversationId(uiModel.getQrcodeTransModel().getConversationId());
                                 param.setToken(s);
//                        param.setDevicePrint(DeviceInfoUtils.getDevicePrint(((BussFragment) view).getActivity()));

                                 if (uiModel.getQrcodeTransModel().getSelectedFactorId().equals("8")) {
                                     param.setOtp(uiModel.getEncryptPasswords()[0]);
                                     param.setOtp_RC(uiModel.getEncryptRandomNums()[0]);
                                 } else if (uiModel.getQrcodeTransModel().getSelectedFactorId().equals("32")) {
                                     param.setSmc(uiModel.getEncryptPasswords()[0]);
                                     param.setSmc_RC(uiModel.getEncryptRandomNums()[0]);
                                 } else if (uiModel.getQrcodeTransModel().getSelectedFactorId().equals("40")) {
                                     param.setOtp(uiModel.getEncryptPasswords()[0]);
                                     param.setOtp_RC(uiModel.getEncryptRandomNums()[0]);
                                     param.setSmc(uiModel.getEncryptPasswords()[1]);
                                     param.setSmc_RC(uiModel.getEncryptRandomNums()[1]);
                                 } else if (uiModel.getQrcodeTransModel().getSelectedFactorId().equals("96")) {
                                     param.setSmc(uiModel.getEncryptPasswords()[0]);
                                     param.setSmc_RC(uiModel.getEncryptRandomNums()[0]);
                                     DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(((BussFragment) view).getActivity(),
                                             uiModel.getQrcodeTransModel().getRandomNum());
                                     param.setDeviceInfo(info.getDeviceInfo());
                                     param.setDeviceInfo_RC(info.getDeviceInfo_RC());
                                     param.setDevicePrint(DeviceInfoUtils.getDevicePrint(((BussFragment) view).getActivity()));
                                 } else if (uiModel.getQrcodeTransModel().getSelectedFactorId().equals("4")) {
                                     param.set_signedData(uiModel.getSignedData());
                                 }

                                 if (uiModel.getQrcodeTransModel().getNeedPassword().equals("0")) {
//                                     param.setAtmPassword("");
//                                     param.setAtmPassword_RC("");
//                                     param.setPhoneBankPassword("");
//                                     param.setPhoneBankPassword_RC("");
//                                     param.setPassbookPassword("");
//                                     param.setPassbookPassword_RC("");
                                 } else if (uiModel.getQrcodeTransModel().getNeedPassword().equals("1")) {
                                     param.setAtmPassword(uiModel.getEncryptPasswordsPass()[0]);
                                     param.setAtmPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                                 } else if (uiModel.getQrcodeTransModel().getNeedPassword().equals("2")) {
                                     param.setPhoneBankPassword(uiModel.getEncryptPasswordsPass()[0]);
                                     param.setPhoneBankPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                                 } else if (uiModel.getQrcodeTransModel().getNeedPassword().equals("3")) {
                                     param.setPassbookPassword(uiModel.getEncryptPasswordsPass()[0]);
                                     param.setPassbookPassword_RC(uiModel.getEncryptRandomNumsPass()[0]);
                                 }
                                 param.setActiv(SecurityVerity.getInstance().getCfcaVersion());
                                 param.setState(SecurityVerity.SECURITY_VERIFY_STATE);
                                 return transferService.psnTransBocTransferSubmit(param);
                             }
                         }

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnTransBocTransferSubmitResult>() {
                               @Override
                               public void handleException(BiiResultErrorException
                                                                   biiResultErrorException) {
                                   ((BussFragment) view).closeProgressDialog();
                                   ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                               }

                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onNext(PsnTransBocTransferSubmitResult result) {
                                   uiModel.setFinalCommissionCharge(result.getFinalCommissionCharge());
                                   uiModel.setTransactionId(result.getTransactionId().longValue());
                                   view.psnTransBocTransferSubmitReturned();
                                   ((BussFragment) view).closeProgressDialog();
                               }
                           }

                );
    }

    /***
     * 查询信用卡账户详情
     */
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
                        uiModel.setCheckStates(false);
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
                            uiModel.setCheckStates(true);
                            uiModel.setCredit(true);
                            uiModel.setRemainAmount(bean.getLoanBalanceLimit());
                            uiModel.setCurrency(bean.getCurrency());
                        } else {
                            uiModel.setCheckStates(false);
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
        params.setAccountId(uiModel.getQrcodeTransModel().getAccountId());
        ((BussFragment) view).showLoadingDialog();
        accountService.psnAccountQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
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
                            uiModel.setCheckStates(true);
                            uiModel.setRemainAmount(bean.getAvailableBalance());
                            uiModel.setCurrency(bean.getCurrencyCode());
                        } else {
                            uiModel.setCheckStates(false);
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
