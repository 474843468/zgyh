package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActSavePayer.PsnTransActSavePayerParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActSavePayer.PsnTransActSavePayerResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.SavePayerViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.SavePayerContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：主动收款保存常用付款人
 * Created by zhx on 2016/7/5
 */
public class SavePayerPresenter implements SavePayerContract.Presenter {
    private SavePayerContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private TransferService transferService;

    public SavePayerPresenter(SavePayerContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        transferService = new TransferService();
    }

    @Override
    public void savePayer(final SavePayerViewModel savePayerViewModel) {
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
                .flatMap(new Func1<String, Observable<PsnTransActSavePayerResult>>() {
                    @Override
                    public Observable<PsnTransActSavePayerResult> call(String token) {
                        savePayerViewModel.setToken(token);
                        return transferService.psnTransActSavePayer(buildSavePayerMobileParams(savePayerViewModel));
                    }
                })
                .compose(SchedulersCompat.<PsnTransActSavePayerResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActSavePayerResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.savePayerFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActSavePayerResult psnTransActSavePayerResult) {
                        mView.savePayerSuccess(null);
                    }
                });
    }

    /**
     * 封装请求参数：
     * 主动收款保存常用付款人
     *
     * @param savePayerViewModel
     * @return PsnTransActSavePayerParams
     */
    private PsnTransActSavePayerParams buildSavePayerMobileParams(SavePayerViewModel savePayerViewModel) {
        PsnTransActSavePayerParams psnTransActSavePayerParams = new PsnTransActSavePayerParams();
        psnTransActSavePayerParams.setToken(savePayerViewModel.getToken());
        psnTransActSavePayerParams.setPayerChannel("2"); // 收款人类型：1：WEB渠道、2：手机渠道
        psnTransActSavePayerParams.setPayerCustId(savePayerViewModel.getPayerCustId()); // TODO 付款人客户号，怎么获取？
        psnTransActSavePayerParams.setPayerMobile(savePayerViewModel.getPayerMobile());
        psnTransActSavePayerParams.setPayerName(savePayerViewModel.getPayerName());
        return psnTransActSavePayerParams;
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
