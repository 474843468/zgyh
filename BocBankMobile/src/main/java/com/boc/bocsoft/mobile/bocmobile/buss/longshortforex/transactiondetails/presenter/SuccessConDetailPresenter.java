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
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.SuccessConDetailContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 双向宝交易查询--成交状况查询详情界面
 * Created by zc on 2016/11/17
 */
public class SuccessConDetailPresenter extends RxPresenter implements SuccessConDetailContract.Presenter {

    private SuccessConDetailContract.View mSuccessConDetailView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService mLongShortForexService;
    private String conversationId;

    public SuccessConDetailPresenter(SuccessConDetailContract.View view) {
        this.mSuccessConDetailView = view;
        mSuccessConDetailView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        mLongShortForexService = new LongShortForexService();
    }
    @Override
    public void psnXpadSuccessConDetailQuery(final XpadVFGTradeDetailQueryModel model) {
        ((BussFragment) mSuccessConDetailView).showLoadingDialog();
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
                        ((BussFragment) mSuccessConDetailView).closeProgressDialog();
                        mSuccessConDetailView.psnXpadSuccessConDetailQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult) {
                        ((BussFragment) mSuccessConDetailView).closeProgressDialog();
                        mSuccessConDetailView.psnXpadSuccessConDetailQuerySuccess(psnVFGTradeDetailQueryResult);
                    }
                });
    }

}
