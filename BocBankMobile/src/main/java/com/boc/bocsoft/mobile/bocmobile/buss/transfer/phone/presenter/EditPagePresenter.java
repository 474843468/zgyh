package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.presenter;

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
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferPre.PsnMobileTransferPreParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferPre.PsnMobileTransferPreResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui.PhoneEditPageContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/7/27.
 */
public class EditPagePresenter extends RxPresenter implements PhoneEditPageContact.Presenter {

    private PhoneEditPageContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private AccountService accountService;
    private PhoneEditModel uiModel;
    private TransferService service;
    private GlobalService globalService;

    public void setUiModel(PhoneEditModel uiModel) {
        this.uiModel = uiModel;
    }

    public EditPagePresenter(PhoneEditPageContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        accountService = new AccountService();
        service = new TransferService();
        globalService = new GlobalService();
        uiModel = view.getModel();
    }

    @Override
    public void psnGetSecurityFactor() {
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        ((BussFragment) view).showLoadingDialog();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        uiModel.setConversationId(s);

                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(s);
                        return globalService.psnGetRandom(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        uiModel.setRandomNum(s);

                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        params.setConversationId(uiModel.getConversationId());
                        return globalService.psnGetTokenId(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String s) {
                        uiModel.setTokenId(s);

                        PsnGetSecurityFactorParams mSecurityFactorParams = new PsnGetSecurityFactorParams();
                        mSecurityFactorParams.setConversationId(uiModel.getConversationId());
                        mSecurityFactorParams.setServiceId("PB035");
                        return globalService.psnGetSecurityFactor(mSecurityFactorParams);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnMobileTransferPreResult>>() {
                    @Override
                    public Observable<PsnMobileTransferPreResult> call(PsnGetSecurityFactorResult result) {
                        SecurityFactorModel factor = new SecurityFactorModel(result);
                        PhoneEditModel.factorModel = factor;
                        CombinListBean bean = SecurityVerity.getInstance(((BussFragment) view).getActivity())
                                .getDefaultSecurityFactorId(PhoneEditModel.factorModel);
                        uiModel.setDefaultFactorName(bean.getName());
                        uiModel.setSelectedFactorId(bean.getId());

                        PsnMobileTransferPreParam param = new PsnMobileTransferPreParam();
                        param.setPayeeName(uiModel.getPayeeName());
                        param.setConversationId(uiModel.getConversationId());
                        param.setFromAccountId(uiModel.getAccountId());
                        param.setExecuteType("0");
                        param.setCurrency("001");
                        param.set_combinId(bean.getId());
                        param.setPayeeMobile(uiModel.getPayeeMobile());
                        param.setToken(uiModel.getTokenId());
                        param.setAmount(uiModel.getTrfAmount());
                        param.setRemark(uiModel.getTips());
                        return service.psnMobileTransferPre(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnMobileTransferPreResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnMobileTransferPreResult result) {
                        uiModel.setIsHaveAccount(result.getIsHaveAcct());
                        uiModel.setNeedPassword(result.getNeedPassword());
                        uiModel.setPayeeAccountNum(result.getPayeeActno());
                        uiModel.setToAccountType(result.getToAccountType());
                        uiModel.setPayeeBankNum(result.getPayeeBankNum());
                        boolean ret = SecurityVerity.getInstance().confirmFactor(result.getFactorList());
                        SecurityVerity.getInstance().setConversationId(uiModel.getConversationId());
                        if (ret) {
                            PhoneEditModel.prefactorList = result.getFactorList();
                            view.psnGetSecurityFactorReturned();
                        } else {
                            ((BussFragment) view).showErrorDialog("预交易失败");
                        }
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnAccountQueryAccountDetail(String accId) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
//        params.setAccountId(uiModel.getAccountId());
        params.setAccountId(accId);
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
                            uiModel.setLoanBalanceLimitFlag(bean.getLoanBalanceLimitFlag());
                            uiModel.setRemainAmount(bean.getLoanBalanceLimit());
                            uiModel.setRemainCurrency(bean.getCurrency());
                        } else {
                            uiModel.setQueryStates(false);
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
        service.psnTransQuotaQuery(params)
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
