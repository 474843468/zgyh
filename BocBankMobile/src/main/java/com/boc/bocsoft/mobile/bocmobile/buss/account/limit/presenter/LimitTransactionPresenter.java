package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.FactorAndCaResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenSubmit.PsnNcpayOpenSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayQuotaModifySubmit.PsnNcpayQuotaModifySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.account.service.LimitService;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.QuotaModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author wangyang
 *         2016/10/17 15:22
 *         交易限额交易Presenter
 */
public class LimitTransactionPresenter extends BaseTransactionPresenter implements LimitContract.LimitTransactionPresenter {

    private AccountService accountService;

    private LimitService limitService;

    private LimitContract.QuotaTransactionView quotaView;

    private LimitContract.LimitCloseView closeView;

    private LimitContract.LimitUpdateView updateView;

    private LimitContract.LimitOpenView openView;

    public LimitTransactionPresenter(LimitContract.QuotaTransactionView quotaView) {
        super(quotaView);
        this.quotaView = quotaView;
        accountService = new AccountService();
    }

    public LimitTransactionPresenter(LimitContract.LimitCloseView closeView) {
        super(closeView);
        this.closeView = closeView;
        limitService = new LimitService();
    }

    public LimitTransactionPresenter(LimitContract.LimitUpdateView updateView) {
        super(updateView);
        this.updateView = updateView;
        limitService = new LimitService();
    }

    public LimitTransactionPresenter(LimitContract.LimitOpenView openView) {
        super(openView);
        this.openView = openView;
        limitService = new LimitService();
    }

    @Override
    public void setQuotaPre(final QuotaModel quotaModel, final String combinId) {
        getConversation().flatMap(new Func1<String, Observable<FactorAndCaResult>>() {
            @Override
            public Observable<FactorAndCaResult> call(String conversationId) {
                return accountService.psnDebitCardSetQuotaPre(ModelUtil.generateDebitCardSetQuotaParams(quotaModel, conversationId, combinId));
            }
        })
                .compose(this.<FactorAndCaResult>bindToLifecycle())
                .compose(SchedulersCompat.<FactorAndCaResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<FactorAndCaResult>() {
                    @Override
                    public void onNext(FactorAndCaResult factorAndCaResult) {
                        preTransactionSuccess(ModelUtil.generateSecurityModel(factorAndCaResult));
                    }
                });
    }

    @Override
    public void setQuota(final QuotaModel quotaModel, final DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords) {
        getToken().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String token) {
                return accountService.psnDebitCardSetQuota(ModelUtil.generateRelationParams(quotaModel, getConversationId(), token, deviceInfoModel, factorId, randomNums, encryptPasswords));
            }
        }).compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String string) {
                        clearConversation();
                        quotaView.setQuota(true);
                    }
                });
    }

    @Override
    public void closeServicePre(final LimitModel limitModel) {
        getConversation().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String conversationId) {
                setConversationId(conversationId);
                return limitService.psnNcpayClosePre(ModelUtil.generateNcpayClosePreParams(limitModel, conversationId));
            }
        }).compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String result) {
                        closeService(limitModel);
                    }
                });
    }

    @Override
    public void closeService(final LimitModel limitModel) {
        getToken().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String token) {
                return limitService.psnNcpayCloseSubmit(ModelUtil.generateNcpayCloseParams(limitModel, getConversationId(), token));
            }
        }).compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String result) {
                        clearConversation();
                        limitModel.setAccountStatus("N");
                        closeView.closeService(limitModel);
                    }
                });
    }

    @Override
    public void updateLimitPre(final LimitModel limitModel, final String factorId) {
        getConversation().flatMap(new Func1<String, Observable<FactorAndCaResult>>() {
            @Override
            public Observable<FactorAndCaResult> call(String conversationId) {
                return limitService.psnNcpayQuotaModifyPre(ModelUtil.generateQuotaModifyPreParams(limitModel, conversationId, factorId));
            }
        }).compose(this.<FactorAndCaResult>bindToLifecycle())
                .compose(SchedulersCompat.<FactorAndCaResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<FactorAndCaResult>() {
                    @Override
                    public void onNext(FactorAndCaResult factorAndCaResult) {
                        preTransactionSuccess(ModelUtil.generateSecurityModel(factorAndCaResult));
                    }
                });
    }

    @Override
    public void updateLimit(final LimitModel limitModel, final DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords) {
        getToken().flatMap(new Func1<String, Observable<PsnNcpayQuotaModifySubmitResult>>() {
            @Override
            public Observable<PsnNcpayQuotaModifySubmitResult> call(String token) {
                return limitService.psnNcpayQuotaModifySubmit(ModelUtil.generateQuotaModifySubmitParams(limitModel, getConversationId(), token, deviceInfoModel, factorId, randomNums, encryptPasswords));
            }
        }).compose(this.<PsnNcpayQuotaModifySubmitResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnNcpayQuotaModifySubmitResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnNcpayQuotaModifySubmitResult>() {
                    @Override
                    public void onNext(PsnNcpayQuotaModifySubmitResult psnNcpayQuotaModifySubmitResult) {
                        clearConversation();
                        updateView.updateLimit(limitModel);
                    }
                });
    }

    @Override
    public void openLimitPre(final LimitModel limitModel, final String factorId) {
        getConversation().flatMap(new Func1<String, Observable<FactorAndCaResult>>() {
            @Override
            public Observable<FactorAndCaResult> call(String conversationId) {
                return limitService.psnNcpayOpenPre(ModelUtil.generateQuotaOpenPreParams(limitModel, conversationId, factorId));
            }
        }).compose(this.<FactorAndCaResult>bindToLifecycle())
                .compose(SchedulersCompat.<FactorAndCaResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<FactorAndCaResult>() {
                    @Override
                    public void onNext(FactorAndCaResult factorAndCaResult) {
                        preTransactionSuccess(ModelUtil.generateSecurityModel(factorAndCaResult));
                    }
                });
    }

    @Override
    public void openLimit(final LimitModel limitModel, final DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords) {
        getToken().flatMap(new Func1<String, Observable<PsnNcpayOpenSubmitResult>>() {
            @Override
            public Observable<PsnNcpayOpenSubmitResult> call(String token) {
                return limitService.psnNcpayOpenSubmit(ModelUtil.generateQuotaOpenSubmitParams(limitModel, getConversationId(), token, deviceInfoModel, factorId, randomNums, encryptPasswords));
            }
        }).compose(this.<PsnNcpayOpenSubmitResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnNcpayOpenSubmitResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnNcpayOpenSubmitResult>() {
                    @Override
                    public void onNext(PsnNcpayOpenSubmitResult result) {
                        clearConversation();
                        limitModel.setCifMobile(result.getCifMobile());
                        limitModel.setAccountStatus("Y");
                        openView.openLimit(limitModel);
                    }
                });
    }
}
