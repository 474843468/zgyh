package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.CollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.CollectionSubmitContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：主动收款
 * Created by zhx on 2016/7/5
 */
public class CollectionSubmitPresenter implements CollectionSubmitContract.Presenter {
    private CollectionSubmitContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private TransferService transferService;

    private String conversationId;
    private PsnGetSecurityFactorResult psnGetSecurityFactorResult;

    private CollectionSubmitViewModel uiModel;

    public String getConversationId() {
        return conversationId;
    }

    public PsnGetSecurityFactorResult getPsnGetSecurityFactorResult() {
        return psnGetSecurityFactorResult;
    }

    public CollectionSubmitPresenter(CollectionSubmitContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        transferService = new TransferService();

        uiModel = view.getUiModel();
    }

    /**
     * 主动收款
     * 接口调用顺序：PSNCreatConversation、PsnGetSecurityFactor、PsnTransActCollectionVerify、PSNGetTokenId、PsnTransActCollectionSubmit
     *
     * @param collectionSubmitViewModel
     */
    @Override
    public void collectionSubmit(CollectionSubmitViewModel collectionSubmitViewModel) {
    }

    @Override
    public void psnGetSecurityFactor() {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversation) {
                        // 保存conversationId到成员
                        CollectionSubmitPresenter.this.conversationId = conversation;

                        PsnGetSecurityFactorParams psnGetSecurityFactorParams = new PsnGetSecurityFactorParams();
                        psnGetSecurityFactorParams.setConversationId(conversation);
                        psnGetSecurityFactorParams.setServiceId("PB037");
                        return globalService.psnGetSecurityFactor(psnGetSecurityFactorParams);
                    }
                })
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        ((BussFragment) mView).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult result) {
                        SecurityFactorModel factor = new SecurityFactorModel(result);
                        uiModel.setFactorModel(factor);
                        mView.securityFactorReturned();
                        ((BussFragment) mView).closeProgressDialog();
                    }
                });

    }

    public void psnCollectionVerify() {
    }


    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
    }
}
