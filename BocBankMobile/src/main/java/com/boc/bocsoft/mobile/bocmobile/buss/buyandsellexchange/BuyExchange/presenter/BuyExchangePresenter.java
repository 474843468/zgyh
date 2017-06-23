package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 购汇
 * Created by gwluo on 2016/12/21.
 */

public class BuyExchangePresenter extends BuySellExchangePresenter {
    protected BuyExchangeContract.BuyTransView mView;

    public BuyExchangePresenter(BuyExchangeContract.BaseTransView view) {
        super(view);
        mView = (BuyExchangeContract.BuyTransView) view;
    }

    /**
     * 获取余额和结售汇额度
     *
     * @param accId
     * @param fessFlag 结汇：01 购汇：11
     */
    public void getPsnFessQueryAccountBalanceLimit(final String accId, final String fessFlag) {
        showLoadingDialog();
        PsnFessQueryAccountBalanceParams params = new PsnFessQueryAccountBalanceParams();
        params.setAccountId(accId);
        fessService.psnFessQueryAccountBalance(params)
                .compose(this.<List<PsnFessQueryAccountBalanceResult>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<PsnFessQueryAccountBalanceResult>, Observable<PsnFessQueryForLimitResult>>() {
                    @Override
                    public Observable<PsnFessQueryForLimitResult> call(List<PsnFessQueryAccountBalanceResult> psnFessQueryAccountBalanceResults) {
                        PsnFessQueryAccountBalanceResult accBalance =
                                electRMB(psnFessQueryAccountBalanceResults);
                        model.setAvailableBalance(MoneyUtils.trimAmountZero(accBalance.getAvailableBalance()));
                        model.setCashRemit(accBalance.getCashRemit());
//                        model.setCurrency(accBalance.getCurrency());
                        PsnFessQueryForLimitParams params = new PsnFessQueryForLimitParams();
                        params.setConversationId(model.getConversationId());
                        params.setAccountId(model.getAccList().get(0).getAccountId());
                        params.setFessFlag(fessFlag);//结汇：01 购汇：11
                        return fessService.psnFessQueryForLimit(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryForLimitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        closeLoadingDialog();
                    }

                    @Override
                    public void onCompleted() {
                        closeLoadingDialog();
                    }

                    @Override
                    public void onNext(PsnFessQueryForLimitResult result) {
                        closeLoadingDialog();
                        model.setAnnAmtUSD(result.getAnnAmtUSD());
                        model.setAnnRmeAmtUSD(result.getAnnRmeAmtUSD());
                        model.setTypeStatus(result.getTypeStatus());
                        model.setSignStatus(result.getSignStatus());
                        model.setCustName(result.getCustName());
                        mView.updateAccBalanceLimit();
                    }

                });

    }


}
