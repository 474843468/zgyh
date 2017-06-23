package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeTransContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/7/28.
 */
public class QrcodeTransPresenter extends RxPresenter implements QrcodeTransContact.Presenter {
    private QrcodeTransContact.View view;
    private AccountService accountService;
    private QrcodeTransModel uiModel;
    private String conversationId;
    private GlobalService globalService;
    private TransferService transferService;

    public void setUiModel(QrcodeTransModel uiModel) {
        this.uiModel = uiModel;
    }

    public QrcodeTransPresenter(QrcodeTransContact.View view) {
        this.view = view;
        accountService = new AccountService();
        globalService = new GlobalService();
        transferService = new TransferService();
        uiModel = view.getModel();
    }

    @Override
    public void psnAccountQueryAccountDetail(String accId) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accId);
        accountService.psnAccountQueryAccountDetail(params)
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        uiModel.setCredit(false);
                        uiModel.setCheckStates(false);
                        view.psnAccountQueryAccountDetailReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        List<PsnAccountQueryAccountDetailResult.AccountDetaiListBean> list = result.getAccountDetaiList();
                        // 是否有人民币余额
                        boolean isHaveRMB = false;
                        PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean = null;
                        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean item : list) {
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
                            uiModel.setRemainCurrency(bean.getCurrencyCode());
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
    public void psnGetSecurityFactor() {
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String s) {
                        PsnGetSecurityFactorParams mSecurityFactorParams = new PsnGetSecurityFactorParams();
                        conversationId = s;
                        uiModel.setConversationId(conversationId);
                        SecurityVerity.getInstance(((BussFragment) view).getActivity()).setConversationId(conversationId);
                        mSecurityFactorParams.setConversationId(s);
                        mSecurityFactorParams.setServiceId(ApplicationConst.PB031);
                        return globalService.psnGetSecurityFactor(mSecurityFactorParams);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnGetSecurityFactorResult result) {
                        SecurityFactorModel factor = new SecurityFactorModel(result);
                        uiModel.setFactorModel(factor);

                        PhoneEditModel.factorModel = factor;
                        CombinListBean bean = SecurityVerity.getInstance(((BussFragment) view).getActivity())
                                .getDefaultSecurityFactorId(PhoneEditModel.factorModel);
                        uiModel.setDefaultFactorName(bean.getName());
                        uiModel.setSelectedFactorId(bean.getId());

                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(conversationId);
                        return globalService.psnGetRandom(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnTransBocTransferVerifyResult>>() {
                    @Override
                    public Observable<PsnTransBocTransferVerifyResult> call(String s) {
                        uiModel.setRandomNum(s);
                        PsnTransBocTransferVerifyParams params = new PsnTransBocTransferVerifyParams();
                        params.setConversationId(conversationId);
                        params.setServiceId("PB031");
                        params.setCurrency(ApplicationConst.CURRENCY_CNY);
                        params.set_combinId(uiModel.getSelectedFactorId());
                        params.setAmount(uiModel.getTrfAmount());
                        params.setExecuteType(ApplicationConst.NOWEXE);
                        params.setFromAccountId(uiModel.getAccountId());
                        params.setPayeeActno(uiModel.getPayeeAccount());
                        params.setPayeeMobile(uiModel.getPayeeMobile());
                        params.setPayeeName(uiModel.getPayeeName());
                        params.setRemark(uiModel.getTips());
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
                        uiModel.setNeedPassword(result.getNeedPassword());
                        uiModel.setPrefactorList(result.getFactorList());
                        PhoneEditModel.prefactorList = result.getFactorList();

                        PsnTransGetBocTransferCommissionChargeParams chargeParams =
                                new PsnTransGetBocTransferCommissionChargeParams();
                        chargeParams.setServiceId(ApplicationConst.PB031);
                        chargeParams.setToAccountId(uiModel.getPayeeAccount());
                        chargeParams.setFromAccountId(uiModel.getAccountId());
                        chargeParams.setRemark(uiModel.getTips());
                        chargeParams.setAmount(uiModel.getTrfAmount());
                        chargeParams.setCurrency(ApplicationConst.CURRENCY_CNY);
                        chargeParams.setPayeeActno(uiModel.getPayeeAccount());
                        chargeParams.setPayeeName(uiModel.getPayeeName());
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
                        if ("1".equals(result.getGetChargeFlag())) {
                            uiModel.setCommisionCharge(result.getPreCommissionCharge().toString());
                            view.securityFactorReturned();
                        } else {
                            uiModel.setCommisionCharge("手续费查询失败，请以实际扣收为准");
                            view.securityFactorReturned();
                        }
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
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
                .compose(this.<PsnCrcdQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        uiModel.setCheckStates(false);
                        uiModel.setCredit(true);
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
                            uiModel.setLoanBalanceLimitFlag(bean.getLoanBalanceLimitFlag());
                            uiModel.setRemainAmount(bean.getLoanBalanceLimit());
                            uiModel.setRemainCurrency(bean.getCurrency());
                        } else {
                            uiModel.setCheckStates(false);
                            uiModel.setWarning("该账户无人民币余额，请修改");
                            uiModel.setWarnType("1");
                        }
                        view.queryCreditAccountDetailReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnTransQuotaquery() {
        PsnTransQuotaQueryParams params = new PsnTransQuotaQueryParams();
        transferService.psnTransQuotaQuery(params)
                .compose(this.<PsnTransQuotaQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransQuotaQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransQuotaQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        view.queryQuotaForTransFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransQuotaQueryResult result) {
                        view.queryQuotaForTransSuccess(result);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                });
    }

    @Override
    public void psnTransBocTransferVerify() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(conversationId);
        ((BussFragment) view).showLoadingDialog();
        globalService.psnGetTokenId(params)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnTransBocTransferVerifyResult>>() {
                    @Override
                    public Observable<PsnTransBocTransferVerifyResult> call(String s) {
                        PsnTransBocTransferVerifyParams params = new PsnTransBocTransferVerifyParams();
                        params.setConversationId(conversationId);
                        params.setServiceId("PB031");
                        params.setCurrency(ApplicationConst.CURRENCY_CNY);
                        params.set_combinId(uiModel.getSelectedFactorId());
                        params.setAmount(uiModel.getTrfAmount());
                        params.setExecuteType(ApplicationConst.NOWEXE);
                        params.setFromAccountId(uiModel.getAccountId());
                        params.setPayeeActno(uiModel.getPayeeAccount());
                        params.setPayeeMobile(uiModel.getPayeeMobile());
                        params.setPayeeName(uiModel.getPayeeName());
                        params.setRemark(uiModel.getTips());
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
                        uiModel.setNeedPassword(result.getNeedPassword());

                        PsnTransGetBocTransferCommissionChargeParams chargeParams =
                                new PsnTransGetBocTransferCommissionChargeParams();
                        chargeParams.setServiceId(ApplicationConst.PB031);
                        chargeParams.setToAccountId(uiModel.getPayeeAccount());
                        chargeParams.setFromAccountId(uiModel.getAccountId());
                        chargeParams.setRemark(uiModel.getTips());
                        chargeParams.setAmount(uiModel.getTrfAmount());
                        chargeParams.setCurrency(ApplicationConst.CURRENCY_CNY);
                        chargeParams.setPayeeActno(uiModel.getPayeeAccount());
                        chargeParams.setPayeeName(uiModel.getPayeeName());
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
                        uiModel.setCommisionCharge(result.getPreCommissionCharge().toString());
                        view.psnTransBocTransferVerifyReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }
//
//    @Override
//    public void subscribe() {
//
//    }
//
//    @Override
//    public void unsubscribe() {
//        mRxLifecycleManager.onDestroy();
//    }
}
