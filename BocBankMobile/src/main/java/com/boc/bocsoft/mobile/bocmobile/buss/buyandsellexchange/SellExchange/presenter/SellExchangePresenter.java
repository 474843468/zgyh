package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.presenter;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuySellExchangePresenter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 结汇
 * Created by gwluo on 2016/12/21.
 */

public class SellExchangePresenter extends BuySellExchangePresenter {
    protected BuyExchangeContract.SellTransView mView;

    public SellExchangePresenter(BuyExchangeContract.BaseTransView view) {
        super(view);
        mView = (BuyExchangeContract.SellTransView) view;
    }

    /**
     * 结售汇额度
     *
     * @param accId
     * @param fessFlag 结汇：01 购汇：11
     */
    public void getPsnFessQueryLimit(String accId, final String fessFlag) {
        showLoadingDialog();
        PsnFessQueryForLimitParams params = new PsnFessQueryForLimitParams();
        params.setConversationId(model.getConversationId());
        params.setAccountId(accId);
        params.setFessFlag(fessFlag);//结汇：01 购汇：11
        fessService.psnFessQueryForLimit(params)
                .compose(this.<PsnFessQueryForLimitResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<PsnFessQueryForLimitResult, Observable<PsnFessQueryExchangeRateResult>>() {
                    @Override
                    public Observable<PsnFessQueryExchangeRateResult> call(PsnFessQueryForLimitResult result) {
                        model.setAnnAmtUSD(result.getAnnAmtUSD());
                        model.setAnnRmeAmtUSD(result.getAnnRmeAmtUSD());
                        model.setTypeStatus(result.getTypeStatus());
                        model.setSignStatus(result.getSignStatus());
                        model.setCustName(result.getCustName());
                        PsnFessQueryExchangeRateParams params = new PsnFessQueryExchangeRateParams();
                        params.setCashRemit(model.getCashRemit());
                        params.setCurrency(model.getCurrency());
                        params.setAccountId(model.getAccountId());
                        params.setFessFlag("S");
                        return fessService.psnFessQueryExchangeRate(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryExchangeRateResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessQueryExchangeRateResult result) {
                        model.setCashRemitRate(result.getReferenceRate().toPlainString());
                        mView.onQueryLimit();
                    }
                });
    }
}
