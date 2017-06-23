package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter;

import android.content.Context;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessBuyExchangeHibs.PsnFessBuyExchangeHibsParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessBuyExchangeHibs.PsnFessBuyExchangeHibsResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSellExchangeHibs.PsnFessSellExchangeHibsParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSellExchangeHibs.PsnFessSellExchangeHibsResult;
import com.boc.bocsoft.mobile.bii.bus.fess.service.FessService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuySellExchangeConfirmInfoFragment;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 确认信息页面
 * Created by gwluo on 2016/12/14.
 */

public class BuyExchangeConfirmInfoPresenter extends RxPresenter implements BuyExchangeContract.Presenter {
    private FessService fessService;
    private GlobalService globalService;
    private BuyExchangeModel model;
    private BuyExchangeContract.ConfirmView mView;
    private Context mContext;

    public BuyExchangeConfirmInfoPresenter(Context context, BuyExchangeContract.ConfirmView view) {
        mContext = context;
        fessService = new FessService();
        globalService = new GlobalService();
        mView = view;
        model = ((BuySellExchangeConfirmInfoFragment) view).getModel();
    }

    /**
     * 结汇
     */
    public void psnFessBuyExchangeHibs() {
        showDialog();
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(model.getConversationId());
        globalService.psnGetTokenId(params)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnFessBuyExchangeHibsResult>>() {
                    @Override
                    public Observable<PsnFessBuyExchangeHibsResult> call(String s) {
                        PsnFessBuyExchangeHibsParams params = new PsnFessBuyExchangeHibsParams();
                        params.setAccountId(model.getAccountId());
                        params.setCurrency(model.getCurrency());
                        params.setAmount(model.getTransAmount());
                        params.setCashRemit(model.getCashRemit());
                        params.setFundUseInfo(PublicCodeUtils.getBankrollUse(mContext, model.getMoneyUse()));
                        params.setConversationId(model.getConversationId());
                        params.setToken(s);
                        return fessService.psnFessBuyExchangeHibs(params);
                    }
                })
                .flatMap(new Func1<PsnFessBuyExchangeHibsResult, Observable<PsnFessQueryForLimitResult>>() {
                    @Override
                    public Observable<PsnFessQueryForLimitResult> call(PsnFessBuyExchangeHibsResult result) {
                        model.setFinalReferenceRate(result.getReferenceRate());
                        model.setFinalExchangeRate(result.getExchangeRate());
                        model.setBankSelfNum(result.getBankSelfNum());
                        model.setCashRemit(result.getCashRemit());
                        model.setCurrency(result.getCurrency());
                        model.setTransactionId(result.getTransactionId());
                        model.setReturnCnyAmt(result.getReturnCnyAmt());
                        model.setReturnCnyAmt(result.getReturnCnyAmt());
                        model.setTransStatus(result.getTransStatus());

                        PsnFessQueryForLimitParams params = new PsnFessQueryForLimitParams();
                        params.setConversationId(model.getConversationId());
                        params.setAccountId(model.getAccList().get(0).getAccountId());
                        params.setFessFlag("11");//结汇：01 购汇：11
                        return fessService.psnFessQueryForLimit(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryForLimitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        //TODO 该接口报错不应该影响成功页面的展示，只是额度信息无法展示
//                        model.setFinalAnnAmtUSD("");
//                        model.setFinalAnnRmeAmtUSD("");
//                        mView.onSubmit();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessQueryForLimitResult result) {
                        ((BussFragment) mView).closeProgressDialog();
                        model.setFinalAnnAmtUSD(result.getAnnAmtUSD());
                        model.setFinalAnnRmeAmtUSD(result.getAnnRmeAmtUSD());
                        mView.onSubmit();
                    }
                });
    }

    /**
     * 购汇
     */
    public void psnFessSellExchangeHibsParams() {
        showDialog();
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(model.getConversationId());
        globalService.psnGetTokenId(params)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnFessSellExchangeHibsResult>>() {
                    @Override
                    public Observable<PsnFessSellExchangeHibsResult> call(String s) {
                        PsnFessSellExchangeHibsParams params = new PsnFessSellExchangeHibsParams();
                        params.setAccountId(model.getAccountId());
                        params.setCurrency(model.getCurrency());
                        params.setAmount(model.getTransAmount());
                        params.setCashRemit(model.getCashRemit());
                        String identityType = model.getIdentityType();
                        //是否境外
                        boolean isOverSeas = !("01".equals(identityType) || "02".equals(identityType));
                        if (isOverSeas) {
                            params.setFundSource(PublicCodeUtils.getSellExchangeBankrollOverseasUse(mContext, model.getMoneyUse()));
                        } else {
                            params.setFundSource(PublicCodeUtils.getSellExchangeBankrollTerritoryUse(mContext, model.getMoneyUse()));
                        }
                        params.setConversationId(model.getConversationId());
                        params.setToken(s);
                        return fessService.psnFessSellExchangeHibs(params);
                    }
                })
                .flatMap(new Func1<PsnFessSellExchangeHibsResult, Observable<PsnFessQueryForLimitResult>>() {
                    @Override
                    public Observable<PsnFessQueryForLimitResult> call(PsnFessSellExchangeHibsResult result) {
                        model.setFinalReferenceRate(result.getReferenceRate());
                        model.setFinalExchangeRate(result.getExchangeRate());
                        model.setBankSelfNum(result.getBankSelfNum());
                        model.setCashRemit(result.getCashRemit());
                        model.setCurrency(result.getCurrency());
                        model.setTransactionId(result.getTransactionId());
                        model.setReturnCnyAmt(result.getReturnCnyAmt());
                        model.setReturnCnyAmt(result.getReturnCnyAmt());
                        model.setTransStatus(result.getTransStatus());

                        PsnFessQueryForLimitParams params = new PsnFessQueryForLimitParams();
                        params.setConversationId(model.getConversationId());
                        params.setAccountId(model.getAccList().get(0).getAccountId());
                        params.setFessFlag("01");//结汇：01 购汇：11
                        return fessService.psnFessQueryForLimit(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryForLimitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        //TODO 该接口报错不应该影响成功页面的展示，只是额度信息无法展示
//                        model.setFinalAnnAmtUSD("");
//                        model.setFinalAnnRmeAmtUSD("");
//                        mView.onSubmit();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessQueryForLimitResult result) {
                        ((BussFragment) mView).closeProgressDialog();
                        model.setFinalAnnAmtUSD(result.getAnnAmtUSD());
                        model.setFinalAnnRmeAmtUSD(result.getAnnRmeAmtUSD());
                        mView.onSubmit();
                    }
                });
    }

    private void showDialog() {
        ((BussFragment) mView).showLoadingDialog();
    }
}
