package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyPre.PsnXpadProductBuyPreResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyResult.PsnXpadProductBuyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseContact;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.List;

import rx.Observable;
import rx.exceptions.CompositeException;
import rx.functions.Func1;

/**
 * Created by wangtong on 2016/9/20.
 */
public class PurchasePresenter extends BaseTransactionPresenter implements PurchaseContact.PurchasePresenter {

    private static String PRE_CONVERSATION_ID;

    /**
     * 理财service
     */
    private WealthManagementService service;
    /**
     * 账户service
     */
    private AccountService accountService;
    /**
     * 购买回调界面
     */
    private PurchaseContact.PurchaseView purchaseView;
    /**
     * 确认交易界面
     */
    public PurchaseContact.PurchaseTransactionView transactionView;

    public PurchasePresenter(PurchaseContact.PurchaseView purchaseView) {
        super(null);
        accountService = new AccountService();
        service = new WealthManagementService();
        this.purchaseView = purchaseView;
    }

    public PurchasePresenter(PurchaseContact.PurchaseTransactionView transactionView) {
        super(transactionView);
        this.transactionView = transactionView;
        service = new WealthManagementService();
        setConversationId(PRE_CONVERSATION_ID);
    }

    @Override
    public void queryAccountDetail(final PurchaseModel purchaseModel) {
        accountService.psnAccountQueryAccountDetail(new PsnAccountQueryAccountDetailParams(purchaseModel.getPayAccountId()))
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        purchaseView.queryAccountDetail(ModelUtil.generatePurchaseModel(purchaseModel, result));
                    }
                });
    }

    @Override
    public void queryRiskMatch(final PurchaseModel purchaseModel) {
        service.psnXpadQueryRiskMatch(new PsnXpadQueryRiskMatchParams(purchaseModel.getAccountKey(), purchaseModel.getProdCode()))
                .compose(this.<PsnXpadQueryRiskMatchResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadQueryRiskMatchResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadQueryRiskMatchResult>() {
                    @Override
                    public void onNext(PsnXpadQueryRiskMatchResult result) {
                        purchaseView.queryRiskMatch(ModelUtil.generatePurchaseModel(purchaseModel, result));
                    }
                });
    }

    @Override
    public void productBuyPre(final PurchaseModel purchaseModel) {
        getToken().flatMap(new Func1<String, Observable<PsnXpadProductBuyPreResult>>() {
            @Override
            public Observable<PsnXpadProductBuyPreResult> call(String token) {
                PRE_CONVERSATION_ID = getConversationId();
                return service.psnXpadProductBuyPre(ModelUtil.generateProductBuyPreParams(getConversationId(), token, purchaseModel));
            }
        }).compose(this.<PsnXpadProductBuyPreResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadProductBuyPreResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadProductBuyPreResult>() {
                    @Override
                    public void onNext(PsnXpadProductBuyPreResult result) {
                        purchaseView.productBuyPre(ModelUtil.generatePurchaseModel(purchaseModel, result));
                    }
                });
    }

    @Override
    public void productBuySubmit(final PurchaseModel purchaseModel) {
        getToken().flatMap(new Func1<String, Observable<PsnXpadProductBuyResult>>() {
            @Override
            public Observable<PsnXpadProductBuyResult> call(String token) {
                return service.psnXpadProductBuyResult(ModelUtil.generateProductBuyParams(getConversationId(), token, purchaseModel));
            }
        }).compose(this.<PsnXpadProductBuyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadProductBuyResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadProductBuyResult>() {
                    @Override
                    public void onNext(PsnXpadProductBuyResult result) {
                        updateRecentAccountAndQueryLikeList(ModelUtil.generatePurchaseModel(purchaseModel,result));
                    }
                });
    }

    private void updateRecentAccountAndQueryLikeList(final PurchaseModel purchaseModel){
        Observable.mergeDelayError(updateRecentAccount(purchaseModel), queryProductList(purchaseModel))
                .compose(bindToLifecycle())
                .subscribe(new BaseAccountSubscriber<Object>() {

                    private List<WealthListBean> likeBeans;

                    @Override
                    public void onNext(Object o) {
                        if(o instanceof PsnXpadProductListQueryResult){
                            likeBeans = ModelUtil.generateWealthListBeans(purchaseModel.getProdCode(), purchaseModel.getProductRisk(), (PsnXpadProductListQueryResult) o);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof CompositeException) {
                            CompositeException compositeException = (CompositeException) e;
                            e = compositeException.getExceptions().get(0);
                        }
                        super.onError(e);
                        onCompleted();
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {
                        clearConversation();
                        PRE_CONVERSATION_ID = null;
                        transactionView.productBuySubmit(purchaseModel,likeBeans);
                    }
                });
    }

    public Observable<PsnXpadRecentAccountUpdateResult> updateRecentAccount(final PurchaseModel purchaseModel) {
        return getToken().flatMap(new Func1<String, Observable<PsnXpadRecentAccountUpdateResult>>() {
            @Override
            public Observable<PsnXpadRecentAccountUpdateResult> call(String token) {
                return service.psnXpadRecentAccountUpdate(ModelUtil.generateUpdateRecentAccountParams(getConversationId(),token,purchaseModel));
            }
        }).compose(SchedulersCompat.<PsnXpadRecentAccountUpdateResult>applyIoSchedulers());
    }

    private Observable<PsnXpadProductListQueryResult> queryProductList(final PurchaseModel purchaseModel) {
        return getConversation().flatMap(new Func1<String, Observable<PsnXpadProductListQueryResult>>() {
            @Override
            public Observable<PsnXpadProductListQueryResult> call(String conversationId) {
                return service.psnXpadProductListQuery(ModelUtil.generateProductListQueryParams(purchaseModel.getPayAccountId(), purchaseModel.getCurCode(), conversationId));
            }
        }).compose(SchedulersCompat.<PsnXpadProductListQueryResult>applyIoSchedulers());
    }
}
