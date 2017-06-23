package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDelegateCancel.PsnXpadDelegateCancelParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDelegateCancel.PsnXpadDelegateCancelResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadDelegateCancelViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.TransDetailContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Contact：中银理财-委托交易详情
 * Created by zhx on 2016/9/22
 */
public class TransDetailPresenter implements TransDetailContact.Presenter {
    private TransDetailContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    private String conversationId;

    public TransDetailPresenter(TransDetailContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    @Override
    public void psnXpadDelegateCancel(final XpadDelegateCancelViewModel viewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        TransDetailPresenter.this.conversationId = conversation;
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnXpadDelegateCancelResult>>() {
                    @Override
                    public Observable<PsnXpadDelegateCancelResult> call(String token) {
                        PsnXpadDelegateCancelParams psnXpadDelegateCancelParams = new PsnXpadDelegateCancelParams();
                        BeanConvertor.toBean(viewModel, psnXpadDelegateCancelParams);
                        psnXpadDelegateCancelParams.setConversationId(TransDetailPresenter.this.conversationId);
                        psnXpadDelegateCancelParams.setToken(token);
                        return wealthManagementService.psnXpadDelegateCancel(psnXpadDelegateCancelParams);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadDelegateCancelResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadDelegateCancelResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadDelegateCancelFail(null);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadDelegateCancelResult result) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadDelegateCancelSuccess(viewModel);
                    }
                });
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
