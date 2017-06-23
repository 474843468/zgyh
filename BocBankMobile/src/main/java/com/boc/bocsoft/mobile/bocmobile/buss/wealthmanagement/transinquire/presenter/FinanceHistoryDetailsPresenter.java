package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery.PsnXpadTransInfoDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery.PsnXpadTransInfoDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.TransInfoDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.FinanceHistoryDetailsContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import com.tencent.mm.sdk.platformtools.Log;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：中银理财-详情列表
 * Created by zc on 2016/9/12
 */
public class FinanceHistoryDetailsPresenter extends RxPresenter implements FinanceHistoryDetailsContract.Presenter {

    private FinanceHistoryDetailsContract.View mFinanceHistoryDetailsView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private WealthManagementService wealthManagementService;
    private String conversationId;



    public FinanceHistoryDetailsPresenter(FinanceHistoryDetailsContract.View view) {
        mFinanceHistoryDetailsView = view;

        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    //查询历史常规交易详情
    @Override
    public void psnPsnXpadTransInfoDetailQuery(final TransInfoDetailViewModel viewModel) {
        ((BussFragment) mFinanceHistoryDetailsView).showLoadingDialog("请稍候...");

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadTransInfoDetailQueryResult>>() {
                    @Override
                    public Observable<PsnXpadTransInfoDetailQueryResult> call(String conversation) {
                        PsnXpadTransInfoDetailQueryParams psnXpadTransInfoDetailQueryParams = new PsnXpadTransInfoDetailQueryParams();

//                        psnXpadTransInfoDetailQueryParams.setConversationId(conversation);
                        Log.i("zc111","----------"+viewModel.getTranSeq());
                        psnXpadTransInfoDetailQueryParams.setAccountKey(viewModel.getAccountKey());
                        psnXpadTransInfoDetailQueryParams.setTranSeq(viewModel.getTranSeq());
                        return wealthManagementService.psnXpadTransInfoDetailQuery(psnXpadTransInfoDetailQueryParams);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadTransInfoDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadTransInfoDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mFinanceHistoryDetailsView).closeProgressDialog();
                        mFinanceHistoryDetailsView.psnPsnXpadTransInfoDetailQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadTransInfoDetailQueryResult psnXpadTransInfoDetailQueryResult) {
                        ((BussFragment) mFinanceHistoryDetailsView).closeProgressDialog();
                        mFinanceHistoryDetailsView.psnPsnXpadTransInfoDetailQuerySuccess(psnXpadTransInfoDetailQueryResult);
                    }
                });
    }
}
