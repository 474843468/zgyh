package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailTransfer.PsnVFGBailTransferParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailTransfer.PsnVFGBailTransferResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelContract.PsnVFGCancelContractParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelContract.PsnVFGCancelContractResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount.PsnVFGQueryMaxAmountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount.PsnVFGQueryMaxAmountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGCancelContractViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGQueryMaxAmountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.ConfirmCancelContractContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：双向宝-账户管理-保证金账户详情
 * Created by zhx on 2016/11/23
 */
public class ConfirmCancelContractPresenter extends RxPresenter implements ConfirmCancelContractContact.Presenter {
    private ConfirmCancelContractContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private LongShortForexService longShortForexService;
    private String conversationId;

    public ConfirmCancelContractPresenter(ConfirmCancelContractContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        longShortForexService = new LongShortForexService();
    }

    @Override
    public void vFGCancelContract(final VFGCancelContractViewModel vfgCancelContractViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        ConfirmCancelContractPresenter.this.conversationId = conversationId;
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnVFGCancelContractResult>>() {
                    @Override
                    public Observable<PsnVFGCancelContractResult> call(String token) {
                        PsnVFGCancelContractParams params = new PsnVFGCancelContractParams();
                        BeanConvertor.toBean(vfgCancelContractViewModel, params);
                        params.setConversationId(conversationId);
                        params.setToken(token);
                        return longShortForexService.psnVFGCancelContract(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGCancelContractResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGCancelContractResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGCancelContractFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGCancelContractResult psnVFGCancelContractResult) {
                        mView.vFGCancelContractSuccess(vfgCancelContractViewModel);
                    }
                });
    }

    @Override
    public void vFGQueryMaxAmount(final VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel) {
        PsnVFGQueryMaxAmountParams params = new PsnVFGQueryMaxAmountParams();
        params.setCashRemit(vfgQueryMaxAmountViewModel.getCashRemit());
        params.setCurrencyCode(vfgQueryMaxAmountViewModel.getCurrencyCode());
        longShortForexService.psnVFGQueryMaxAmount(params)
                .compose(SchedulersCompat.<PsnVFGQueryMaxAmountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGQueryMaxAmountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGQueryMaxAmountFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGQueryMaxAmountResult psnVFGQueryMaxAmountResult) {
                        BeanConvertor.toBean(psnVFGQueryMaxAmountResult, vfgQueryMaxAmountViewModel);
                        mView.vFGQueryMaxAmountSuccess(vfgQueryMaxAmountViewModel);
                    }
                });
    }

    @Override
    public void vFGBailTransfer(final VFGBailTransferViewModel vfgBailTransferViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        ConfirmCancelContractPresenter.this.conversationId = conversationId;
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnVFGBailTransferResult>>() {
                    @Override
                    public Observable<PsnVFGBailTransferResult> call(String token) {
                        PsnVFGBailTransferParams params = new PsnVFGBailTransferParams();
                        BeanConvertor.toBean(vfgBailTransferViewModel, params);
                        params.setConversationId(conversationId);
                        params.setToken(token);
                        return longShortForexService.psnVFGBailTransfer(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGBailTransferResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGBailTransferResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGBailTransferFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }



                    @Override
                    public void onNext(PsnVFGBailTransferResult psnVFGBailTransferResult) {
                        vfgBailTransferViewModel.setTransactionId(psnVFGBailTransferResult.getTransactionId());
                        vfgBailTransferViewModel.setStockBalance(psnVFGBailTransferResult.getStockBalance());
                        mView.vFGBailTransferSuccess(vfgBailTransferViewModel);
                    }
                });
    }
}
