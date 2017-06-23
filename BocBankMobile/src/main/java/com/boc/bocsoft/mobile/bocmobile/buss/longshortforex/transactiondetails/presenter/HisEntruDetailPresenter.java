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
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.HisEntruDetailContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 双向宝交易查询--历史委托查询详情界面
 * Created by zc on 2016/11/17
 */
public class HisEntruDetailPresenter extends RxPresenter implements HisEntruDetailContract.Presenter {

    private HisEntruDetailContract.View mHisEntruDetailView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService mLongShortForexService;
    private String conversationId;//会话ID

    public HisEntruDetailPresenter(HisEntruDetailContract.View view) {
        this.mHisEntruDetailView = view;
        mHisEntruDetailView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        mLongShortForexService = new LongShortForexService();
    }

    /**
     * 双向宝历史委托详情查询
     * @param model
     */
    @Override
    public void psnXpadHisEntruDetailQuery(final XpadVFGTradeDetailQueryModel model) {
        ((BussFragment) mHisEntruDetailView).showLoadingDialog();
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
                        ((BussFragment) mHisEntruDetailView).closeProgressDialog();
                        mHisEntruDetailView.psnXpadHisEntruDetailQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult) {
                        ((BussFragment) mHisEntruDetailView).closeProgressDialog();
                        mHisEntruDetailView.psnXpadHisEntruDetailQuerySuccess(psnVFGTradeDetailQueryResult);
                    }
                });
    }

    /**
     * 双向宝历史有效委托“撤单”
     * @param viewModel
     */
    @Override
    public void psnXpadCancelOrder(final XpadPsnVFGCancelOrderModel viewModel) {
        ((BussFragment) mHisEntruDetailView).showLoadingDialog();
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        HisEntruDetailPresenter.this.conversationId = conversation;
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
                        ((BussFragment) mHisEntruDetailView).closeProgressDialog();
                        mHisEntruDetailView.psnXpadCancelOrderFail(null);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnVFGCancelOrderResult result) {
                        ((BussFragment) mHisEntruDetailView).closeProgressDialog();
                        BeanConvertor.toBean(result,viewModel);
                        mHisEntruDetailView.psnXpadCancelOrderSuccess(viewModel);
                    }
                });
    }
}
