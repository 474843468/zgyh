package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailTransfer.PsnVFGBailTransferParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailTransfer.PsnVFGBailTransferResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.model.XpadVFGBailTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui.ConfirmInformationContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author hty7062
 */
public class ConfirmInformationPresenter extends RxPresenter implements ConfirmInformationContract.Presenter {

    private ConfirmInformationContract.View mConfirmInformationView;
    private GlobalService globalService;
    private RxLifecycleManager mRxLifecycleManager;
    private LongShortForexService longShortForexService;
    private String conversationId;

    public ConfirmInformationPresenter(ConfirmInformationContract.View view) {
        mConfirmInformationView = view;
        globalService = new GlobalService();
        mRxLifecycleManager = new RxLifecycleManager();
        longShortForexService = new LongShortForexService();
    }

    @Override
    public void psnVFGBailTransfer(final XpadVFGBailTransferViewModel xpadVFGBailTransferViewModel) {
        ((BussFragment) mConfirmInformationView).showLoadingDialog("请稍候...");
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        ConfirmInformationPresenter.this.conversationId = conversation;
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnVFGBailTransferResult>>() {
                    @Override
                    public Observable<PsnVFGBailTransferResult> call(String token) {
                        PsnVFGBailTransferParams psnVFGBailTransferParams = new PsnVFGBailTransferParams();
                        BeanConvertor.toBean(xpadVFGBailTransferViewModel, psnVFGBailTransferParams);
                        psnVFGBailTransferParams.setConversationId(ConfirmInformationPresenter.this.conversationId);
                        psnVFGBailTransferParams.setToken(token);
                        return longShortForexService.psnVFGBailTransfer(psnVFGBailTransferParams);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGBailTransferResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGBailTransferResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mConfirmInformationView.psnVFGBailTransferFail(biiResultErrorException);
                        System.out.print("ConfirmInformation Exception------------>>>>>>>>>---异常");
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGBailTransferResult psnVFGBailTransferResult) {
                        ((BussFragment)mConfirmInformationView).closeProgressDialog();
                        BeanConvertor.toBean(psnVFGBailTransferResult, xpadVFGBailTransferViewModel);
                        mConfirmInformationView.psnVFGBailTransferSuccess(xpadVFGBailTransferViewModel);
                        System.out.print("fadfadfadfasfas");
                    }
                });
    }
}