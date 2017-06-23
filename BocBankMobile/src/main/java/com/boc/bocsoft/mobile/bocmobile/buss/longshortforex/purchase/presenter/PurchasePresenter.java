package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetRegCurrency.PsnVFGGetRegCurrencyParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycle;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querysingelquotation.WFSSQuerySingelQuotationResult;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.service.WFSSForexAndNobleMetalService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.exceptions.CompositeException;
import rx.functions.Func1;

/**
 * @author wangyang
 *         2016/12/16 09:51
 *         双向宝交易
 */
public class PurchasePresenter extends BaseAccountPresenter implements PurchaseContract.Presenter {

    private PurchaseContract.PurchaseView purchaseView;

    private LongShortForexService service;

    private WFSSForexAndNobleMetalService wfssService;

    public PurchasePresenter(PurchaseContract.PurchaseView purchaseView) {
        this.purchaseView = purchaseView;
        wfssService = new WFSSForexAndNobleMetalService();
        service = new LongShortForexService();
    }

    @Override
    public void queryCurrencyAndSingleRate(PurchaseModel model) {
        Observable<Object> observable;
        if (model.isShowMaxTrade())
            observable = Observable.mergeDelayError(queryAccountInfo(), queryRate(model));
        else
            observable = Observable.mergeDelayError(queryCurrency(model.getCardType()), queryRate(model));

        observable.compose(this.bindToLifecycle()).subscribe(new PurchaseSubscriber(model));
    }

    @Override
    public void querySingleRate(final PurchaseModel model) {
        queryRate(model).compose(this.<WFSSQuerySingelQuotationResult>bindToLifecycle())
                .subscribe(new BaseAccountSubscriber<WFSSQuerySingelQuotationResult>() {
                    @Override
                    public void onNext(WFSSQuerySingelQuotationResult result) {
                        purchaseView.querySingleRate(ModelUtil.generatePurchaseModel(model, result), true);
                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        purchaseView.querySingleRate(model, true);
                    }
                });
    }

    @Override
    public void queryBailAccount() {
        queryAccountInfo().compose(this.<PsnVFGBailAccountInfoListQueryResult>bindToLifecycle())
                .subscribe(new BaseAccountSubscriber<PsnVFGBailAccountInfoListQueryResult>() {

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        purchaseView.queryBailAccount(ModelUtil.generateBailAccount(null));
                    }

                    @Override
                    public void onNext(PsnVFGBailAccountInfoListQueryResult result) {
                        purchaseView.queryBailAccount(ModelUtil.generateBailAccount(result));
                    }
                });
    }

    @Override
    public void intervalQuerySingleRate(final PurchaseModel model) {
        Observable.interval(PurchaseModel.QUERY_RATE_PERIOD, PurchaseModel.QUERY_RATE_PERIOD, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(RxLifecycle.Event.BGTASK_DESTROY))
                .flatMap(new Func1<Long, Observable<WFSSQuerySingelQuotationResult>>() {
                    @Override
                    public Observable<WFSSQuerySingelQuotationResult> call(Long aLong) {
                        return queryRate(model);
                    }
                }).subscribe(new BaseAccountSubscriber<WFSSQuerySingelQuotationResult>() {
            @Override
            public void onNext(WFSSQuerySingelQuotationResult result) {
                purchaseView.querySingleRate(ModelUtil.generatePurchaseModel(model, result), false);
            }

            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            }
        });
    }

    @Override
    public void queryTransaction(PurchaseModel model) {
        service.psnXpadTradeInfoQuery(ModelUtil.generatePsnVFGTradeInfoQueryParams(model.getCurrency()))
                .compose(this.<PsnVFGTradeInfoQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnVFGTradeInfoQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnVFGTradeInfoQueryResult>() {
                    @Override
                    public void onNext(PsnVFGTradeInfoQueryResult result) {
                        System.out.println("queryTransaction==========="+result);
                    }
                });
    }

    private Observable<List<String>> queryCurrency(String cardType) {
        return service.psnXpadGetRegCurrency(new PsnVFGGetRegCurrencyParams(cardType))
                .compose(SchedulersCompat.<List<String>>applyIoSchedulers());
    }

    private Observable<PsnVFGBailAccountInfoListQueryResult> queryAccountInfo() {
        return service.psnVFGBailAccountInfoListQuery(new PsnVFGBailAccountInfoListQueryParams())
                .compose(SchedulersCompat.<PsnVFGBailAccountInfoListQueryResult>applyIoSchedulers());
    }

    private Observable<WFSSQuerySingelQuotationResult> queryRate(final PurchaseModel model) {
        return wfssService.querySingelQuotation(ModelUtil.generateWFSSQuerySingelQuotationParams(model))
                .compose(SchedulersCompat.<WFSSQuerySingelQuotationResult>applyIoSchedulers());
    }

    private class PurchaseSubscriber extends BaseAccountSubscriber<Object> {

        private PurchaseModel model;

        PurchaseSubscriber(PurchaseModel model) {
            this.model = model;
        }

        private WFSSQuerySingelQuotationResult rateResult;

        private PsnVFGBailAccountInfoListQueryResult accountResult;

        private List<String> list;

        @Override
        public void onNext(Object o) {
            if (o instanceof WFSSQuerySingelQuotationResult) {
                rateResult = (WFSSQuerySingelQuotationResult) o;
                purchaseView.querySingleRate(ModelUtil.generatePurchaseModel(model, rateResult), false);
            }

            if (o instanceof PsnVFGBailAccountInfoListQueryResult) {
                accountResult = (PsnVFGBailAccountInfoListQueryResult) o;
                purchaseView.queryBailAccount(ModelUtil.generateBailAccount(accountResult));
            }

            if (o instanceof List) {
                list = (List<String>) o;
                purchaseView.queryCurrencyList(ModelUtil.generateCurrencyList(model, list));
            }
        }

        @Override
        public void handleException(BiiResultErrorException error) {
            if (rateResult == null)
                purchaseView.querySingleRate(model, false);

            if (accountResult == null)
                purchaseView.queryBailAccount(ModelUtil.generateBailAccount(null));
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
            rateResult = null;
            accountResult = null;
            list = null;
            ((BussFragment) purchaseView).closeProgressDialog();
            intervalQuerySingleRate(model);
        }
    }
}
