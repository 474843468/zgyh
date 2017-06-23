package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeDetailQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.OpenTradingDetailContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 双向宝交易查询--未平仓交易查询详情界面
 * Created by zc on 2016/11/17
 */
public class OpenTradingDetailPresenter extends RxPresenter implements OpenTradingDetailContract.Presenter {

    private OpenTradingDetailContract.View mOpenTradingDetailView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService mLongShortForexService;

    public OpenTradingDetailPresenter(OpenTradingDetailContract.View view) {
        mOpenTradingDetailView = view;
        mOpenTradingDetailView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        mLongShortForexService = new LongShortForexService();
    }

    /**
     * 双向宝未平仓交易详情查询
     * @param model
     */
    @Override
    public void psnXpadHisEntruDetailQuery(final XpadVFGTradeDetailQueryModel model) {
        ((BussFragment) mOpenTradingDetailView).showLoadingDialog();
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
                        ((BussFragment) mOpenTradingDetailView).closeProgressDialog();
                        mOpenTradingDetailView.psnXpadHisEntruDetailQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult) {
                        ((BussFragment) mOpenTradingDetailView).closeProgressDialog();
                        mOpenTradingDetailView.psnXpadHisEntruDetailQuerySuccess(psnVFGTradeDetailQueryResult);
                    }
                });
    }

}
