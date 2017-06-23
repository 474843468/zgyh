package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFADisengageBind.PsnOFADisengageBindParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFADisengageBind.PsnOFADisengageBindResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountCancel.PsnXpadAccountCancelParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountCancel.PsnXpadAccountCancelResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReset.PsnXpadResetParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReset.PsnXpadResetResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadResult.PsnXpadResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadResult.PsnXpadResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.AccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui.AccountContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui.AccountMainFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 理财——账户管理l逻辑
 * Created by Wan mengxin on 2016/9/20.
 */
public class AccountMainPersenter extends RxPresenter implements AccountContract.Presenter {

    private BussFragment bussFragment;
    private WealthManagementService service;
    private GlobalService globalService;
    private AccountService accountService;
    private RxLifecycleManager mRxLifecycleManager;
    private AccountModel uiModel;

    private static final String XPADACCOUNTSATUS = "1";
    private static final String QUERYTYPE = "0";

    public AccountContract.MainView mainView;
    public AccountContract.DetailView detailView;
    public AccountContract.RegistView registView;

    public AccountMainPersenter(AccountContract.MainView mainView) {
        this.mainView = mainView;
        bussFragment = (BussFragment) mainView;
        service = new WealthManagementService();
        globalService = new GlobalService();
        accountService = new AccountService();
        mRxLifecycleManager = new RxLifecycleManager();
        uiModel = mainView.getModel();
    }

    public AccountMainPersenter(AccountContract.DetailView detailView) {
        this.detailView = detailView;
        bussFragment = (BussFragment) detailView;
        service = new WealthManagementService();
        globalService = new GlobalService();
        accountService = new AccountService();
        mRxLifecycleManager = new RxLifecycleManager();
        uiModel = detailView.getModel();
    }

    public AccountMainPersenter(AccountContract.RegistView registView) {
        this.registView = registView;
        bussFragment = (BussFragment) registView;
        service = new WealthManagementService();
        globalService = new GlobalService();
        accountService = new AccountService();
        mRxLifecycleManager = new RxLifecycleManager();
        uiModel = registView.getModel();
    }

    /**
     * 建立会话 获取tokenid
     * 020风险评估查询（判断是否做过风险评估） PsnInvtEvaluationInit
     * 037查询客户理财账户信息 PsnXpadAccountQuery
     */
    @Override
    public void psnInvtEvaluationInit() {
        PSNCreatConversationParams Params = new PSNCreatConversationParams();
        globalService.psnCreatConversation(Params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {

                        PSNGetTokenIdParams mParams = new PSNGetTokenIdParams();
                        uiModel.setConversationId(s);
                        mParams.setConversationId(uiModel.getConversationId());
                        return globalService.psnGetTokenId(mParams);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnInvtEvaluationInitResult>>() {
                    @Override
                    public Observable<PsnInvtEvaluationInitResult> call(String token) {
                        uiModel.setTokenId(token);

                        return service.psnInvtEvaluationInit(new PsnInvtEvaluationInitParams());
                    }
                })
                .flatMap(new Func1<PsnInvtEvaluationInitResult, Observable<PsnXpadAccountQueryResult>>() {
                    @Override
                    public Observable<PsnXpadAccountQueryResult> call(PsnInvtEvaluationInitResult result) {
                        uiModel.setRiskLevel(result.getRiskLevel());

                        PsnXpadAccountQueryParams mParams = new PsnXpadAccountQueryParams();
                        mParams.setXpadAccountSatus(XPADACCOUNTSATUS);
                        mParams.setQueryType(QUERYTYPE);
                        return service.psnXpadAccountQuery(mParams);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mainView.psnXpadAccountQueryFailed();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult result) {
                        List<PsnXpadAccountQueryResult.XPadAccountEntity> resultList = result.getList();
                        mainView.psnXpadAccountQuerySuccess(resultList);
                    }
                });
    }

    /**
     * 046网上专属理财账户状态查询 PsnOFAAccountStateQuery
     */
    @Override
    public void psnOFAAccountStateQuery() {
        PsnOFAAccountStateQueryParams params = new PsnOFAAccountStateQueryParams();
        service.psnOFAAccountStateQuery(params)
                .compose(mRxLifecycleManager.<PsnOFAAccountStateQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnOFAAccountStateQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnOFAAccountStateQueryResult>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   mainView.psnOFAAccountStateQueryFailed();
                               }

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onNext(PsnOFAAccountStateQueryResult result) {
                                   uiModel.setOpenStatus(result.getOpenStatus());
                                   if (result.financialAccount != null) {
//                                       BeanConvertor.toBean(result.financialAccount, uiModel.financialAccount);
                                       uiModel.financialAccount.setAccountNumber(result.financialAccount.getAccountNumber());
                                       uiModel.financialAccount.setNickName(result.financialAccount.getNickName());
                                       uiModel.financialAccount.setAccountType(result.financialAccount.getAccountType());
                                       uiModel.financialAccount.setAccountId(result.financialAccount.getAccountId());
                                       uiModel.financialAccount.setCurrencyCode(result.financialAccount.getCurrencyCode());
                                       uiModel.financialAccount.setCustomerId(result.financialAccount.getCustomerId());
                                       uiModel.financialAccount.setAccountKey(result.financialAccount.getAccountKey());
                                   }

                                   if (result.mainAccount != null) {
//                                       BeanConvertor.toBean(result.mainAccount, uiModel.mainAccount);
                                       uiModel.mainAccount.setAccountNumber(result.mainAccount.getAccountNumber());
                                       uiModel.mainAccount.setAccountType(result.mainAccount.getAccountType());
                                       uiModel.mainAccount.setAccountKey(result.mainAccount.getAccountKey());
                                       uiModel.mainAccount.setNickName(result.mainAccount.getNickName());
                                   }

                                   if (result.bankAccount != null) {
//                                       BeanConvertor.toBean(result.bankAccount, uiModel.bankAccount);
                                       uiModel.bankAccount.setNickName(result.bankAccount.getNickName());
                                       uiModel.bankAccount.setAccountNumber(result.bankAccount.getAccountNumber());
                                       uiModel.bankAccount.setAccountId(result.bankAccount.getAccountId());
                                       uiModel.bankAccount.setAccountName(result.bankAccount.getAccountName());
                                       uiModel.bankAccount.setAccountKey(result.bankAccount.getAccountKey());
                                   }
                                   mainView.psnOFAAccountStateQuerySuccess();
                               }
                           }
                );
    }

    /**
     * 002理财账号重新登记(资金账户列表) PsnXpadReset
     */
    @Override
    public void psnXpadReset() {
        PSNCreatConversationParams Params = new PSNCreatConversationParams();
        globalService.psnCreatConversation(Params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<List<PsnXpadResetResult>>>() {
                    @Override
                    public Observable<List<PsnXpadResetResult>> call(String s) {
                        uiModel.setConversationId(s);

                        PsnXpadResetParams params = new PsnXpadResetParams();
                        return service.psnXpadReset(params);
                    }
                })
                .flatMap(new Func1<List<PsnXpadResetResult>, Observable<PsnXpadAccountQueryResult>>() {
                    @Override
                    public Observable<PsnXpadAccountQueryResult> call(List<PsnXpadResetResult> result) {
                        uiModel.setAllAccountList(result);

                        PsnXpadAccountQueryParams mParams = new PsnXpadAccountQueryParams();
                        mParams.setXpadAccountSatus(XPADACCOUNTSATUS);
                        mParams.setQueryType(QUERYTYPE);
                        return service.psnXpadAccountQuery(mParams);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        registView.psnXpadResetFailed();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult result) {
                        List<PsnXpadAccountQueryResult.XPadAccountEntity> resultList = result.getList();
                        registView.psnXpadResetSuccess(resultList);
                    }
                });
    }

    /**
     * 077理财账户解除登记 PsnXpadAccountCancel
     */
    @Override
    public void psnXpadAccountCancel(final String id) {
        PSNGetTokenIdParams mParams = new PSNGetTokenIdParams();
        mParams.setConversationId(uiModel.getConversationId());
        globalService.psnGetTokenId(mParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadAccountCancelResult>>() {
                    @Override
                    public Observable<PsnXpadAccountCancelResult> call(String s) {

                        PsnXpadAccountCancelParams params = new PsnXpadAccountCancelParams();
                        params.setAccountId(id);
                        params.setToken(s);
                        params.setConversationId(uiModel.getConversationId());
                        params.setAccountType(uiModel.getChoiceType());
                        params.setBancAccountNo(uiModel.getChoiceNum());
                        return service.PsnXpadAccountCancel(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadAccountCancelResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountCancelResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        detailView.psnXpadAccountCancelFailed();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadAccountCancelResult result) {
                        detailView.psnXpadAccountCancelSuccess();
                    }
                });
    }

    /**
     * 047解绑专属理财账户 PsnOFADisengageBind
     */
    @Override
    public void psnOFADisengageBind(final String key) {
        PSNGetTokenIdParams mParams = new PSNGetTokenIdParams();
        mParams.setConversationId(uiModel.getConversationId());
        globalService.psnGetTokenId(mParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnOFADisengageBindResult>>() {
                    @Override
                    public Observable<PsnOFADisengageBindResult> call(String s) {

                        PsnOFADisengageBindParams params = new PsnOFADisengageBindParams();
                        params.setFinancialAccountId(uiModel.financialAccount.getAccountId());
                        params.setAccountKey(key);
                        params.setToken(s);
                        params.setConversationId(uiModel.getConversationId());
                        return service.PsnOFADisengageBind(params);
                    }
                })
                .compose(SchedulersCompat.<PsnOFADisengageBindResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnOFADisengageBindResult>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   detailView.psnOFADisengageBindFailed();
                               }

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onNext(PsnOFADisengageBindResult result) {
                                   detailView.psnOFADisengageBindSuccess();
                               }
                           }
                );
    }

    /**
     * I05 003查询账户详情 PsnAccountQueryAccountDetail
     */
    @Override
    public void psnAccountQueryAccountDetail(String id) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(id);
        accountService.psnAccountQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   detailView.psnAccountQueryAccountDetailFailed();
                               }

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onNext(PsnAccountQueryAccountDetailResult result) {
                                   uiModel.setAccountDetail(result);
                                   uiModel.setAvailableBalance(result.getAccountDetaiList().get(0).getAvailableBalance());
                                   uiModel.setCurrencyCode(result.getAccountDetaiList().get(0).getCurrencyCode());
                                   uiModel.setAccOpenBank(result.getAccOpenBank());
                                   uiModel.setAccountType(result.getAccountType());
                                   uiModel.setAccountStatus(result.getAccountStatus());
                                   uiModel.setAccOpenDate(result.getAccOpenDate());
                                   detailView.psnAccountQueryAccountDetailSuccess();
                               }
                           }
                );
    }


    /**
     * 003理财账号登记结果 PsnXpadResult
     */
    @Override
    public void psnXpadResult(final String id) {
        PSNGetTokenIdParams mParams = new PSNGetTokenIdParams();
        mParams.setConversationId(uiModel.getConversationId());
        globalService.psnGetTokenId(mParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadResultResult>>() {
                    @Override
                    public Observable<PsnXpadResultResult> call(String s) {

                        PsnXpadResultParams params = new PsnXpadResultParams();
                        params.setConversationId(uiModel.getConversationId());
                        params.setToken(s);
                        params.setAccountId(id);
                        return service.psnXpadResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadResultResult>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   registView.psnXpadResultFailed();
                               }

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onNext(PsnXpadResultResult result) {
                                   registView.psnXpadResultSuccess();
                               }
                           }
                );
    }
}
