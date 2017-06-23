package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.DeletePayerViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.DeletePayerContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：删除付款人
 * Created by zhx on 2016/7/5
 */
public class DeletePayerPresenter implements DeletePayerContract.Presenter {
    private DeletePayerContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private TransferService transferService;

    public DeletePayerPresenter(DeletePayerContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        transferService = new TransferService();
    }

    /**
     * 删除付款人
     * 接口调用顺序：PSNCreatConversation、PSNGetTokenId、PsnTransActDeletePayer
     *
     * @param deletePayerViewModel
     */
    @Override
    public void modifyPayerMobile(final DeletePayerViewModel deletePayerViewModel) {
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
                .flatMap(new Func1<String, Observable<PsnTransActDeletePayerResult>>() {
                    @Override
                    public Observable<PsnTransActDeletePayerResult> call(String token) {
                        deletePayerViewModel.setToken(token);
                        return transferService.psnTransActDeletePayer(buildDeletePayerParams(deletePayerViewModel));
                    }
                })
                .compose(SchedulersCompat.<PsnTransActDeletePayerResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActDeletePayerResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.deletePayerFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActDeletePayerResult psnMobileWithdrawalQueryResult) {
                        mView.deletePayerSuccess(null);
                    }
                });
    }

    /**
     * 封装请求参数：
     * 删除付款人
     *
     * @param deletePayerViewModel
     * @return PsnTransActDeletePayerParams
     */
    private PsnTransActDeletePayerParams buildDeletePayerParams(DeletePayerViewModel deletePayerViewModel) {
        PsnTransActDeletePayerParams psnTransActDeletePayerParams = new PsnTransActDeletePayerParams();
        psnTransActDeletePayerParams.setToken(deletePayerViewModel.getToken());
        psnTransActDeletePayerParams.setPayerId(deletePayerViewModel.getPayerId());
        return psnTransActDeletePayerParams;
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
