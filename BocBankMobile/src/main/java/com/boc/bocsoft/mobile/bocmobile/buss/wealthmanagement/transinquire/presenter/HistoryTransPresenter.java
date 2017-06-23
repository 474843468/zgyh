package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus.PsnXpadHisTradStatusParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus.PsnXpadHisTradStatusResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadHisTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.HistoryTransContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：中银理财-交易查询-历史交易（历史常规交易状况查询）
 * Created by zhx on 2016/9/8
 */
public class HistoryTransPresenter implements HistoryTransContact.Presenter {
    private HistoryTransContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    public HistoryTransPresenter(HistoryTransContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    // 中银理财-交易查询-历史交易（历史常规交易状况查询）
    @Override
    public void psnXpadHisTradStatus(final XpadHisTradStatusViewModel xpadHisTradStatusViewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadHisTradStatusResult>>() {
                    @Override
                    public Observable<PsnXpadHisTradStatusResult> call(String conversation) {
                        PsnXpadHisTradStatusParams psnXpadHisTradStatusParams = buildPsnXpadHisTradStatusParams(xpadHisTradStatusViewModel);
                        psnXpadHisTradStatusParams.setConversationId(conversation);
                        return wealthManagementService.psnXpadHisTradStatus(psnXpadHisTradStatusParams);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadHisTradStatusResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadHisTradStatusResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadHisTradStatusResult psnXpadHisTradStatusResult) {
                        ((BussFragment) mView).closeProgressDialog();
                    }
                });
    }

    // 查询客户最近操作的理财账号
    @Override
    public void psnXpadRecentAccountQuery(final XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");

        PsnXpadRecentAccountQueryParams psnXpadRecentAccountQueryParams = new PsnXpadRecentAccountQueryParams();
        wealthManagementService.psnXpadRecentAccountQuery(psnXpadRecentAccountQueryParams)
                .compose(SchedulersCompat.<PsnXpadRecentAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadRecentAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadRecentAccountQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadRecentAccountQueryResult psnXpadRecentAccountQueryResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        BeanConvertor.toBean(psnXpadRecentAccountQueryResult, xpadRecentAccountQueryViewModel);
                        mView.psnXpadRecentAccountQuerySuccess(xpadRecentAccountQueryViewModel);
                    }
                });
    }

    // 生成请求参数（中银理财-交易查询-历史交易（历史常规交易状况查询））
    private PsnXpadHisTradStatusParams buildPsnXpadHisTradStatusParams(XpadHisTradStatusViewModel xpadHisTradStatusViewModel) {
        PsnXpadHisTradStatusParams params = new PsnXpadHisTradStatusParams();

        BeanConvertor.toBean(xpadHisTradStatusViewModel, params); // 这个转换已经确保是成功的

        return params;
    }

    @Override
    public void subscribe() {
        //TODO onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        //TODO 防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}