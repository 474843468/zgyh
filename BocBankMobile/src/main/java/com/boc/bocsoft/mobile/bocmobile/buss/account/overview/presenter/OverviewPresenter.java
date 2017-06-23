package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountModifyAccountAlias.PsnAccountModifyAccountAliasParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail.PsnAccountQryRecentTransDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail.PsnAccountQryRecentTransDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryVIRCardInfo.PsnCrcdQueryVIRCardInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryVIRCardInfo.PsnCrcdQueryVIRCardInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.account.service.VirtualService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import com.boc.bocsoft.mobile.common.utils.RxUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.exceptions.CompositeException;
import rx.functions.Func1;

/**
 * @author wangyang
 *         16/8/10 18:44
 *         账户概览Presenter
 */
public class OverviewPresenter extends BaseTransactionPresenter implements OverviewContract.Presenter {

    /**
     * 账户概览回调界面
     */
    private OverviewContract.Overview overview;
    /**
     * 活期详情回调界面
     */
    private OverviewContract.CurrentView currentView;
    /**
     * 定期详情回调界面
     */
    private OverviewContract.RegularView regularView;
    /**
     * 账户更多界面回调
     */
    private OverviewContract.MoreAccountView moreAccountView;
    /**
     * 账户详情回调界面
     */
    private OverviewContract.AccountDetailView detailView;
    /**
     * 虚拟卡详情回调界面
     */
    private OverviewContract.VirtualView virtualView;
    /**
     * 账户接口Service
     */
    private AccountService accountService;
    /**
     * 虚拟卡接口Service
     */
    private VirtualService virtualService;
    /**
     * 交易详情
     */
    private List<PsnAccountQryRecentTransDetailResult.TransDetail> details;

    public OverviewPresenter(OverviewContract.Overview overview) {
        super(overview);
        accountService = new AccountService();
        this.overview = overview;
    }

    public OverviewPresenter(OverviewContract.CurrentView currentView) {
        super(currentView);
        this.currentView = currentView;
        accountService = new AccountService();
    }

    public OverviewPresenter(OverviewContract.RegularView regularView) {
        super(regularView);
        accountService = new AccountService();
        this.regularView = regularView;
    }

    public OverviewPresenter(OverviewContract.AccountDetailView detailView) {
        super(detailView);
        accountService = new AccountService();
        this.detailView = detailView;
    }

    public OverviewPresenter(OverviewContract.MoreAccountView moreAccountView) {
        super(moreAccountView);
        this.moreAccountView = moreAccountView;
        accountService = new AccountService();
    }

    public OverviewPresenter(OverviewContract.VirtualView virtualView) {
        super(virtualView);
        virtualService = new VirtualService();
        this.virtualView = virtualView;
    }

    @Override
    public void queryAccountDetail(final List<AccountBean> accountBeans) {
        Observable.just(accountBeans)
                .compose(RxUtils.concurrentInIOListRequestTransformer(new Func1<AccountBean, Observable<AccountBean>>() {
                    @Override
                    public Observable<AccountBean> call(AccountBean accountBean) {
                        getAccountDetailByType(accountBean);
                        return Observable.just(accountBean);
                    }
                })).compose(this.<AccountBean>bindToLifecycle()).subscribe();
    }

    @Override
    public void queryAccountDetail(AccountBean accountBean) {
        getAccountDetailByType(accountBean);
    }

    @Override
    public void queryAccountDetailAndTransaction(AccountBean accountBean) {
        Observable.mergeDelayError(getNormalAccountDetail1(accountBean), queryAccountTransDetail1(accountBean))
                .compose(bindToLifecycle())
                .subscribe(new CurrentSubscriber(accountBean));
    }

    private void getAccountDetailByType(AccountBean accountBean) {
        switch (accountBean.getAccountType()) {
            //电子现金账户,查询电子现金账户详情接口
            case ApplicationConst.ACC_TYPE_ECASH:
                queryFinanceDetail(accountBean);
                break;
            //信用卡,查询信用卡详情接口
            case ApplicationConst.ACC_TYPE_ZHONGYIN:
            case ApplicationConst.ACC_TYPE_GRE:
            case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
                getCreditAccountDetail(accountBean);
                break;
            //定期账户,不请求余额接口
            case ApplicationConst.ACC_TYPE_CBQX:
            case ApplicationConst.ACC_TYPE_ZOR:
            case ApplicationConst.ACC_TYPE_EDU:
            case ApplicationConst.ACC_TYPE_REG:
                break;
            //其他查询账户详情接口
            case ApplicationConst.ACC_TYPE_XNCRCD1:
            case ApplicationConst.ACC_TYPE_XNCRCD2:
                break;
            default:
                getNormalAccountDetail(accountBean);
                break;
        }
    }

    @Override
    public void queryFinanceDetail(final AccountBean accountBean) {
        accountService.psnFinanceICAccountDetail(new PsnFinanceICAccountDetailParams(accountBean.getAccountId()))
                .compose(this.<PsnFinanceICAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFinanceICAccountDetailResult>applyIoSchedulers())
                .subscribe(new BaseAccountOverviewSubscriber<PsnFinanceICAccountDetailResult>(accountBean) {
                    @Override
                    public void onNext(PsnFinanceICAccountDetailResult result) {
                        //回调界面
                        if (overview != null)
                            overview.queryAccountDetail(ModelUtil.generateAccountListItemViewModel(accountBean, result));
                    }
                });
    }

    @Override
    public void queryRegularAccountDetail(AccountBean accountBean) {
        getNormalAccountDetail(accountBean);
    }

    /**
     * 返回交易明细Observable
     *
     * @param accountBean
     * @return
     */
    private Observable<PsnAccountQryRecentTransDetailResult> queryAccountTransDetail1(final AccountBean accountBean) {
        return accountService.psnAccountQryRecentTransDetail(new PsnAccountQryRecentTransDetailParams(accountBean.getAccountId()))
                .compose(SchedulersCompat.<PsnAccountQryRecentTransDetailResult>applyIoSchedulers());
    }

    @Override
    public TransDetailViewModel.ListBean getTransDetail(int position) {
        if (details == null || details.isEmpty())
            return null;

        return ModelUtil.generateTransDetailListBean(details.get(position));
    }

    @Override
    public void querySsm(final String accountId) {
        getConversation().flatMap(new Func1<String, Observable<PsnSsmQueryResult>>() {
            @Override
            public Observable<PsnSsmQueryResult> call(String conversationId) {
                return accountService.psnSsmQuery(ModelUtil.generatepsnSsmQueryParams(accountId, conversationId));
            }
        }).compose(this.<PsnSsmQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnSsmQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnSsmQueryResult>() {
                    @Override
                    public void onNext(PsnSsmQueryResult result) {
                        boolean isOpen = false;

                        if (result == null) {
                            moreAccountView.querySsm(isOpen);
                            return;
                        }

                        List<PsnSsmQueryResult.MaplistBean> list = result.getMaplist();
                        if (list == null || list.isEmpty()) {
                            moreAccountView.querySsm(isOpen);
                            return;
                        }

                        for (PsnSsmQueryResult.MaplistBean bean : list) {
                            if ("S001".equals(bean.getServiceid())) {
                                isOpen = true;
                                break;
                            }
                        }
                        moreAccountView.querySsm(isOpen);
                    }
                });
    }

    @Override
    public void updateAccountNickName(final AccountBean accountBean, final String nickName) {
        getToken().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String token) {
                PsnAccountModifyAccountAliasParams params = ModelUtil.generateModifyAccountAliasParams(getConversationId(), token, accountBean.getAccountId(), nickName);
                return accountService.psnAccountModifyAccountAlias(params);
            }
        })
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String result) {
                        clearConversation();
                        accountBean.setNickName(nickName);
                        detailView.updateAccountNickName(accountBean);
                    }
                });
    }

    @Override
    public void queryVirtualDetail(final AccountBean accountBean) {
        virtualService.psnCrcdQueryVIRCardInfo(new PsnCrcdQueryVIRCardInfoParams(accountBean.getAccountId()))
                .compose(this.<PsnCrcdQueryVIRCardInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryVIRCardInfoResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdQueryVIRCardInfoResult>() {
                    @Override
                    public void onNext(PsnCrcdQueryVIRCardInfoResult result) {
                        virtualView.queryVirtualDetail(ModelUtil.generateVirtualCardModel(accountBean, result));
                    }
                });
    }

    /**
     * 请求普通账户列表详情
     *
     * @param accountBean
     */
    private void getNormalAccountDetail(final AccountBean accountBean) {
        accountService.psnAccountQueryAccountDetail(new PsnAccountQueryAccountDetailParams(accountBean.getAccountId()))
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BaseAccountOverviewSubscriber<PsnAccountQueryAccountDetailResult>(accountBean) {
                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        //回调界面
                        if (overview != null)
                            overview.queryAccountDetail(ModelUtil.generateAccountListItemViewModel(accountBean, result));
                        if (currentView != null)
                            currentView.queryAccountDetail(ModelUtil.generateAccountListItemViewModel(accountBean, result), ModelUtil.generateAccountInfoBean(result));
                        if (regularView != null)
                            regularView.queryAccountDetail(ModelUtil.generateTermlyViewModels(accountBean, result), ModelUtil.generateAccountInfoBean(result));
                    }
                });
    }

    /**
     * 返回普通账户详情Observable
     *
     * @param accountBean
     * @return
     */
    private Observable<PsnAccountQueryAccountDetailResult> getNormalAccountDetail1(final AccountBean accountBean) {
        return accountService.psnAccountQueryAccountDetail(new PsnAccountQueryAccountDetailParams(accountBean.getAccountId()))
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers());
    }

    /**
     * 请求信用卡列表账户详情
     *
     * @param accountBean
     */
    private void getCreditAccountDetail(final AccountBean accountBean) {
        //获取信用卡下所有币种信息
        accountService.psnCrcdCurrencyQuery(new PsnCrcdCurrencyQueryParams(accountBean.getAccountNumber()))
                .flatMap(new Func1<PsnCrcdCurrencyQueryResult, Observable<List<PsnCrcdCurrencyQueryResult.CurrencyBean>>>() {
                    @Override
                    public Observable<List<PsnCrcdCurrencyQueryResult.CurrencyBean>> call(PsnCrcdCurrencyQueryResult result) {
                        return Observable.just(result.getCurrencyList());
                    }
                    //并发请求账户详情
                }).compose(RxUtils.concurrentInIOListRequestTransformer(new Func1<PsnCrcdCurrencyQueryResult.CurrencyBean, Observable<PsnCrcdQueryAccountDetailResult>>() {
            @Override
            public Observable<PsnCrcdQueryAccountDetailResult> call(PsnCrcdCurrencyQueryResult.CurrencyBean currencyBean) {
                return accountService.psnCrcdQueryAccountDetail(ModelUtil.generateCrcdQueryAccountDetailParams(accountBean.getAccountId(), currencyBean.getCode()));
            }
        }))
                .compose(this.<PsnCrcdQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAccountDetailResult>() {

                    private List<PsnCrcdQueryAccountDetailResult> results;

                    @Override
                    public void onCompleted() {
                        overview.queryAccountDetail(ModelUtil.generateAccountListItemViewModel(accountBean, results));
                    }

                    @Override
                    public void onNext(PsnCrcdQueryAccountDetailResult result) {
                        if (results == null)
                            results = new ArrayList<>();
                        results.add(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                });
    }

    /**
     * 账户详情处理Subscriber
     *
     * @param <T>
     */
    private abstract class BaseAccountOverviewSubscriber<T> extends BaseAccountSubscriber<T> {

        /**
         * 账户信息
         */
        protected AccountBean accountBean;

        public BaseAccountOverviewSubscriber(AccountBean accountBean) {
            this.accountBean = accountBean;
        }

        @Override
        public void handleException(BiiResultErrorException error) {
            //请求失败,生成AccountListItemViewModel,回调界面
            if (overview != null)
                overview.queryAccountDetail(ModelUtil.getAccountListItemViewModel(null, accountBean));
            if (regularView != null) {
                if (error.getType().equals(HttpException.ExceptionType.RESULT))
                    regularView.queryAccountDetail(null, null);
                else
                    regularView.queryAccountDetailFail();
            }
            if (currentView != null)
                currentView.queryAccountDetail(null, null);
        }

        /**
         * 重写父类方法,主要是为了不弹出错误对话框
         *
         * @param error
         */
        @Override
        public void commonHandleException(BiiResultErrorException error) {
            if (overview != null || (regularView != null && !error.getType().equals(HttpException.ExceptionType.RESULT)))
                return;

            super.commonHandleException(error);
        }
    }

    /**
     * 处理活期账户账户详情与交易明细并发请求结果的处理
     *
     * @author wangyang
     * @time 2016/10/26 19:53
     */
    private class CurrentSubscriber extends BaseAccountOverviewSubscriber<Object> {

        PsnAccountQueryAccountDetailResult detailResult;

        PsnAccountQryRecentTransDetailResult transDetailResult;

        public CurrentSubscriber(AccountBean accountBean) {
            super(accountBean);
        }

        @Override
        public void onNext(Object o) {
            if (o instanceof PsnAccountQueryAccountDetailResult) {
                detailResult = (PsnAccountQueryAccountDetailResult) o;
                currentView.queryAccountDetail(ModelUtil.generateAccountListItemViewModel(accountBean, detailResult), ModelUtil.generateAccountInfoBean(detailResult));
            }
            if (o instanceof PsnAccountQryRecentTransDetailResult) {
                transDetailResult = (PsnAccountQryRecentTransDetailResult) o;

                if (transDetailResult != null && transDetailResult.getRecordNumber() > 0)
                    details = transDetailResult.getTransDetails();
                currentView.queryAccountTransDetail(ModelUtil.generateTransactionBean(transDetailResult));
            }
        }

        @Override
        public void handleException(BiiResultErrorException error) {
            if (detailResult == null)
                super.handleException(error);

            if (transDetailResult == null)
                currentView.queryAccountTransDetail(null);
        }

        @Override
        public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            if (biiResultErrorException.getErrorMessage().contains("存单"))
                biiResultErrorException.setErrorMessage(biiResultErrorException.getErrorMessage().replace("存单", "存款"));
            super.commonHandleException(biiResultErrorException);
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof CompositeException) {
                CompositeException compositeException = (CompositeException) e;
                e = compositeException.getExceptions().get(0);
            }
            super.onError(e);
        }

        @Override
        public void onCompleted() {
            detailResult = null;
            transDetailResult = null;
            ((BussFragment) currentView).closeProgressDialog();
        }
    }
}
