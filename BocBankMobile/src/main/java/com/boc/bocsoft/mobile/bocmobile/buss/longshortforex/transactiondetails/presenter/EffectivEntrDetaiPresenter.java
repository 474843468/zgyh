package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelOrder.PsnVFGCancelOrderParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelOrder.PsnVFGCancelOrderResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGCancelOrderModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeDetailQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.EffectivEntrDetailContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 双向宝交易查询--有效委托查询详情界面
 * Created by zc on 2016/11/17
 */
public class EffectivEntrDetaiPresenter extends RxPresenter implements EffectivEntrDetailContract.Presenter {

    private EffectivEntrDetailContract.View mEffectivEntrDetaiView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService mLongShortForexService;
    private String conversationId;//会话ID，



    public EffectivEntrDetaiPresenter(EffectivEntrDetailContract.View view) {
        this.mEffectivEntrDetaiView = view;
        mEffectivEntrDetaiView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        mLongShortForexService = new LongShortForexService();
    }

    /**
     * 双向宝有效委托交易明细查询
     * @param model
     */
    @Override
    public void psnXpadEffectiveDetailQuery(final XpadVFGTradeDetailQueryModel model) {
        ((BussFragment) mEffectivEntrDetaiView).showLoadingDialog("加载中...");
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnVFGTradeDetailQueryResult>>() {
                    @Override
                    public Observable<PsnVFGTradeDetailQueryResult> call(String conversation) {
                        PsnVFGTradeDetailQueryParams psnVFGTradeDetailQueryParams = new PsnVFGTradeDetailQueryParams();

                        psnVFGTradeDetailQueryParams.setConversationId(model.getConversationId());
                        psnVFGTradeDetailQueryParams.setVfgTransactionId(model.getVfgTransactionId());
                        psnVFGTradeDetailQueryParams.setInternalSeq(model.getInternalSeq());
                        return mLongShortForexService.psnXpadVFGTradeDetailQuery(psnVFGTradeDetailQueryParams);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGTradeDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGTradeDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mEffectivEntrDetaiView).closeProgressDialog();
                        mEffectivEntrDetaiView.psnXpadEffectiveDetailQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult) {
                        ((BussFragment) mEffectivEntrDetaiView).closeProgressDialog();
                        mEffectivEntrDetaiView.psnXpadEffectiveDetailQuerySuccess(psnVFGTradeDetailQueryResult);
                    }
                });
    }

    /**
     * 双向宝有效委托撤单
     * @param viewModel
     */
    @Override
    public void psnXpadCancelOrder(final XpadPsnVFGCancelOrderModel viewModel) {
        ((BussFragment) mEffectivEntrDetaiView).showLoadingDialog();
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        EffectivEntrDetaiPresenter.this.conversationId = conversation;
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnVFGCancelOrderResult>>() {
                    @Override
                    public Observable<PsnVFGCancelOrderResult> call(String token) {
                        PsnVFGCancelOrderParams psnVFGCancelOrderParams = new PsnVFGCancelOrderParams();
                        psnVFGCancelOrderParams.setToken(token);
                        psnVFGCancelOrderParams.setConversationId(conversationId);
                        psnVFGCancelOrderParams.setAmount(viewModel.getAmount());
                        psnVFGCancelOrderParams.setConsignNumber(viewModel.getConsignNumber());
                        psnVFGCancelOrderParams.setCurrencyCode(viewModel.getCurrencyCode());
                        psnVFGCancelOrderParams.setCurrencycode1(viewModel.getCurrencycode1());
                        psnVFGCancelOrderParams.setCurrencycode2(viewModel.getCurrencycode2());
                        psnVFGCancelOrderParams.setDirection(viewModel.getDirection());
                        psnVFGCancelOrderParams.setDueDate(viewModel.getDueDate());
                        psnVFGCancelOrderParams.setExchangeTranType(viewModel.getExchangeTranType());
                        psnVFGCancelOrderParams.setOpenPositionFlag(viewModel.getOpenPositionFlag());
                        psnVFGCancelOrderParams.setPaymentDate(viewModel.getPaymentDate());
                        return mLongShortForexService.psnXpadVFGCancelOrder(psnVFGCancelOrderParams);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGCancelOrderResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGCancelOrderResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mEffectivEntrDetaiView).closeProgressDialog();
                        mEffectivEntrDetaiView.psnXpadCancelOrderFail(null);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnVFGCancelOrderResult result) {
                        ((BussFragment) mEffectivEntrDetaiView).closeProgressDialog();
                        BeanConvertor.toBean(result,viewModel);
                        mEffectivEntrDetaiView.psnXpadCancelOrderSuccess(viewModel);
                    }
                });
    }
}
