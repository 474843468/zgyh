package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnTransActReminderOrderDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnTransActReminderOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActUndoReminderOrder.PsnTransActUndoReminderOrderResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.UndoReminderOrderViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.UndoReminderOrderContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：撤消催款指令
 * Created by zhx on 2016/7/6
 */
public class UndoReminderOrderPresenter implements UndoReminderOrderContract.Presenter {
    UndoReminderOrderContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private TransferService transferService;

    public UndoReminderOrderPresenter(UndoReminderOrderContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        transferService = new TransferService();
    }

    /**
     * 撤消催款指令
     * 接口调用顺序：PSNCreatConversation、PSNGetTokenId、PsnTransActUndoReminderOrder
     *
     * @param undoReminderOrderViewModel
     */
    @Override
    public void undoReminderOrder(final UndoReminderOrderViewModel undoReminderOrderViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransActUndoReminderOrderResult>>() {
                    @Override
                    public Observable<PsnTransActUndoReminderOrderResult> call(String token) {
                        undoReminderOrderViewModel.setToken(token);
                        return transferService.psnTransActUndoReminderOrder(buildReminderOrderDetailParams(undoReminderOrderViewModel));
                    }
                })
                .compose(SchedulersCompat.<PsnTransActUndoReminderOrderResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActUndoReminderOrderResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.undoReminderOrderFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActUndoReminderOrderResult psnTransActUndoReminderOrderResult) {
//                        mView.deletePayerSuccess(null);
                    }
                });
    }

    /**
     * 封装请求参数：
     * 撤消催款指令
     *
     * @param undoReminderOrderViewModel
     * @return PsnTransActDeletePayerParams
     */
    private PsnTransActReminderOrderDetailParams buildReminderOrderDetailParams(UndoReminderOrderViewModel undoReminderOrderViewModel) {
        PsnTransActReminderOrderDetailParams psnTransActReminderOrderDetailParams = new PsnTransActReminderOrderDetailParams();
        psnTransActReminderOrderDetailParams.setNotifyId(undoReminderOrderViewModel.getToken());
        psnTransActReminderOrderDetailParams.setNotifyId(undoReminderOrderViewModel.getNotifyId());
        return psnTransActReminderOrderDetailParams;
    }

    private UndoReminderOrderViewModel copyResult2UIModel(PsnTransActReminderOrderDetailResult psnTransActReminderOrderDetailResult) {
        UndoReminderOrderViewModel undoReminderOrderViewModel = new UndoReminderOrderViewModel();

        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());
        undoReminderOrderViewModel.setCreateChannel(psnTransActReminderOrderDetailResult.getCreateChannel());

        return undoReminderOrderViewModel;
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
