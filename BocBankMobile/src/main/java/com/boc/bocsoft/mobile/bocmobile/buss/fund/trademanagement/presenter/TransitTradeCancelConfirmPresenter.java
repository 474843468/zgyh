package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter;


import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAppointCancel.PsnFundAppointCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAppointCancel.PsnFundAppointCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConsignAbort.PsnFundConsignAbortParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConsignAbort.PsnFundConsignAbortResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundAppointCancelModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundConsignAbortModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui.TransitTradeCancelConfirmContract;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wy7105 on 2016/12/1.
 * 交易记录-撤单确认presenter
 */
public class TransitTradeCancelConfirmPresenter extends RxPresenter implements TransitTradeCancelConfirmContract.Presenter {

    private TransitTradeCancelConfirmContract.View mTransitTradeCancelView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private FundService fundService;
    private String conversitionId;

    public TransitTradeCancelConfirmPresenter(TransitTradeCancelConfirmContract.View view) {
        super();
        this.mTransitTradeCancelView = view;
        mTransitTradeCancelView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        fundService = new FundService();
    }

    /**
     * 请求I41-021基金撤单接口
     */
    @Override
    public void psnFundConsignAbort(final PsnFundConsignAbortModel model) {
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        conversitionId = s;
                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        params.setConversationId(s);
                        return globalService.psnGetTokenId(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnFundConsignAbortResult>>() {
                    @Override
                    public Observable<PsnFundConsignAbortResult> call(String s) {
                        PsnFundConsignAbortParams params = new PsnFundConsignAbortParams();
                        params.setConversationId(conversitionId);
                        params.setToken(s);
                        params.setFundSeq(model.getFundSeq());
                        params.setFundAmount(model.getFundAmount());
                        params.setOriginalTransCode(model.getOriginalTransCode());
                        params.setDate(model.getDate());
                        params.setFundCode(model.getFundCode());
                        params.setNightFlag(model.getNightFlag());
                        return fundService.psnFundConsignAbort(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnFundConsignAbortResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransitTradeCancelView.psnFundConsignAbortFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnFundConsignAbortResult result) {
                        mTransitTradeCancelView.psnFundConsignAbortSuccess(generatePsnFundConsignAbortModel(model, result));
                    }


                });
    }

    //生成一般撤单交易的返回数据
    private PsnFundConsignAbortModel generatePsnFundConsignAbortModel(PsnFundConsignAbortModel viewModel, PsnFundConsignAbortResult result) {
        String fundSeq = result.getFundSeq();
        String transactionId = result.getTransactionId();
        viewModel.setTransactionId(fundSeq);
        viewModel.setFundSeq(transactionId);
        return viewModel;
    }


    /**
     * 请求I41-036基金指定日期交易撤单接口
     */
    @Override
    public void psnFundAppointCancel(final PsnFundAppointCancelModel model) {
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        conversitionId = s;
                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        params.setConversationId(s);
                        return globalService.psnGetTokenId(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnFundAppointCancelResult>>() {
                    @Override
                    public Observable<PsnFundAppointCancelResult> call(String s) {
                        PsnFundAppointCancelParams params = new PsnFundAppointCancelParams();
                        params.setConversationId(conversitionId);
                        params.setToken(s);
                        params.setFundSeq(model.getFundSeq());
                        params.setOriginalTransCode(model.getOriginalTransCode());
                        params.setAssignedDate(model.getAssignedDate());
                        params.setFundCode(model.getFundCode());
                        return fundService.psnFundAppointCancel(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnFundAppointCancelResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransitTradeCancelView.psnFundAppointCancelFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnFundAppointCancelResult result) {
                        mTransitTradeCancelView.psnFundAppointCancelSuccess(generatePsnFundAppointCancelModel(model, result));
                    }

                });
    }

    //生成指定日期撤单交易的返回数据
    private PsnFundAppointCancelModel generatePsnFundAppointCancelModel(PsnFundAppointCancelModel viewModel, PsnFundAppointCancelResult result) {
        String fundSeq = result.getFundSeq();
        String transactionId = result.getTransactionId();
        viewModel.setTransactionId(fundSeq);
        viewModel.setFundSeq(transactionId);
        return viewModel;
    }

}