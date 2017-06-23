package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter;

import android.support.annotation.NonNull;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre.PsnVFGSignPreParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre.PsnVFGSignPreResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignSubmit.PsnVFGSignSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignSubmit.PsnVFGSignSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignPreViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.AddNewBailConfirmFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：双向宝-账户管理-新增保证金账户确认
 * Created by zhx on 2016/12/12
 */
public class AddNewBailConfirmPresenter extends BaseConfirmPresenter<VFGSignPreViewModel, VFGSignSubmitViewModel> {
    private LongShortForexService longShortForexService;
    private String conversationId;
    private RxLifecycleManager mRxLifecycleManager;

    public AddNewBailConfirmPresenter(BaseConfirmContract.View<VFGSignSubmitViewModel> view) {
        super(view);

        mRxLifecycleManager = new RxLifecycleManager();
        longShortForexService = new LongShortForexService();
    }

    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(VFGSignPreViewModel fillInfo) {
        PsnVFGSignPreParams params =
                BeanConvertor.toBean(fillInfo, new PsnVFGSignPreParams());
        return longShortForexService.psnVFGSignPre(params)
                .map(new Func1<PsnVFGSignPreResult, VerifyBean>() {
                    @Override
                    public VerifyBean call(
                            PsnVFGSignPreResult psnVFGSignPreResult) {
                        return BeanConvertor.toBean(psnVFGSignPreResult,
                                new VerifyBean());
                    }
                });
    }

    @Override
    public void submit(VFGSignPreViewModel fillInfo, BaseSubmitBean submitBean) {
        PsnVFGSignSubmitParams params =
                BeanConvertor.toBean(fillInfo, new PsnVFGSignSubmitParams());
        params = BeanConvertor.toBean(submitBean, params);
        longShortForexService.psnVFGSignSubmit(params)
                .compose(this.<PsnVFGSignSubmitResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnVFGSignSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGSignSubmitResult>() {
                    @Override
                    public void handleException(
                            BiiResultErrorException biiResultErrorException) {
                        AddNewBailConfirmFragment fragment = (AddNewBailConfirmFragment)mView;
                        fragment.onSubmitFail(biiResultErrorException);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnVFGSignSubmitResult result) {
                        VFGSignSubmitViewModel vfgSignSubmitViewModel = new VFGSignSubmitViewModel();
                        BeanConvertor.toBean(result, vfgSignSubmitViewModel);
                        mView.onSubmitSuccess(vfgSignSubmitViewModel);
                    }
                });
    }

    public void vFGSetTradeAccount(final VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        mGlobalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        AddNewBailConfirmPresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return mGlobalService.psnGetTokenId(psnGetTokenIdParams);
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
                        AddNewBailConfirmFragment fragment = (AddNewBailConfirmFragment) mView;
                        fragment.vFGSetTradeAccountFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGSetTradeAccountResult psnVFGChangeContractResult) {
                        AddNewBailConfirmFragment fragment = (AddNewBailConfirmFragment) mView;
                        fragment.vFGSetTradeAccountSuccess(null);
                    }
                });
    }
}
