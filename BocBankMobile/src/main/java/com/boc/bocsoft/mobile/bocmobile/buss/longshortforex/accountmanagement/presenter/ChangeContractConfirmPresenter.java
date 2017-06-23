package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGChangeContract.PsnVFGChangeContractParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGChangeContract.PsnVFGChangeContractResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGChangeContractViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.ChangeContractConfirmContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：双向宝-账户管理-变更账户确认
 * Created by zhx on 2016/12/14
 */
public class ChangeContractConfirmPresenter extends RxPresenter implements ChangeContractConfirmContact.Presenter {
    private ChangeContractConfirmContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService longShortForexService;
    private String conversationId;

    public ChangeContractConfirmPresenter(ChangeContractConfirmContact.View view) {
        this.mView = view;

        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        longShortForexService = new LongShortForexService();
        globalService = new GlobalService();
    }

    @Override
    public void vFGChangeContract(final VFGChangeContractViewModel vfgChangeContractViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        ChangeContractConfirmPresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnVFGChangeContractResult>>() {
                    @Override
                    public Observable<PsnVFGChangeContractResult> call(String token) {
                        PsnVFGChangeContractParams params = new PsnVFGChangeContractParams();
                        params.setConversationId(conversationId);
                        params.setToken(token);

                        BeanConvertor.toBean(vfgChangeContractViewModel, params);
                        return longShortForexService.psnVFGChangeContract(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGChangeContractResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGChangeContractResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGChangeContractFail(biiResultErrorException);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGChangeContractResult psnVFGChangeContractResult) {
                        mView.vFGChangeContractSuccess(null);
                    }
                });
    }

    // 双向宝-账户管理-首次/重新设定双向宝账户
    @Override
    public void vFGSetTradeAccount(final VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        ChangeContractConfirmPresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnVFGSetTradeAccountResult>>() {
                    @Override
                    public Observable<PsnVFGSetTradeAccountResult> call(String token) {
                        PsnVFGSetTradeAccountParams params = new PsnVFGSetTradeAccountParams();
                        params.setConversationId(conversationId);
                        params.setToken(token);

                        BeanConvertor.toBean(vfgSetTradeAccountViewModel, params);
                        return longShortForexService.psnVFGSetTradeAccount(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGSetTradeAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGSetTradeAccountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGSetTradeAccountFail(biiResultErrorException);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGSetTradeAccountResult psnVFGChangeContractResult) {
                        mView.vFGSetTradeAccountSuccess(null);
                    }
                });
    }
}
