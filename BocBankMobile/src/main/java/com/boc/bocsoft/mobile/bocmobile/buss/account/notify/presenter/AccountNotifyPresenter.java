package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDiscountmodel.PsnSsmDiscountmodelParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDiscountmodel.PsnSsmDiscountmodelResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmFeeQuery.PsnSsmFeeQueryParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmFeeQuery.PsnSsmFeeQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.AccountNotifyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.AccountNotifyContact;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui.AccSmsNotifyHomeFragment;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/6/25.
 */
public class AccountNotifyPresenter implements AccountNotifyContact.Presenter {

    private AccountNotifyContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private AccountService psnNotifyService;
    private AccountNotifyModel uiModel;
    private GlobalService globalService;

    public AccountNotifyPresenter(AccountNotifyContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        psnNotifyService = new AccountService();
        uiModel = view.getModel();
        globalService = new GlobalService();
    }

    /**
     * 查询短信通知开通情况
     */
    @Override
    public void psnSmsQuery() {
        ((BussFragment) view).showLoadingDialog();
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnSsmQueryResult>>() {
                    @Override
                    public Observable<PsnSsmQueryResult> call(String result) {
                        AccSmsNotifyHomeFragment.conversationId = result;
                        PsnSsmQueryParam params = new PsnSsmQueryParam();
                        params.setAccountId(uiModel.getAccount().getAccountId());
                        params.setConversationId(result);
                        return psnNotifyService.psnSsmQuery(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnSsmQueryResult, Observable<PsnSsmFeeQueryResult>>() {
                    @Override
                    public Observable<PsnSsmFeeQueryResult> call(PsnSsmQueryResult result) {
                        if (result.getStatus().equals("01")) {
                            uiModel.setStatus(true);
                        } else {
                            uiModel.setStatus(false);
                        }
                        uiModel.setMapList(result.getMaplist());
                        uiModel.setFeeAccount(result.getFeeAccountNum());

                        PsnSsmFeeQueryParam params = new PsnSsmFeeQueryParam();
                        params.setConversationId(AccSmsNotifyHomeFragment.conversationId);
                        params.setAccountId(uiModel.getAccount().getAccountId());
                        return psnNotifyService.psnSsmFeeQuery(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnSsmFeeQueryResult, Observable<PsnSsmDiscountmodelResult>>() {
                    @Override
                    public Observable<PsnSsmDiscountmodelResult> call(PsnSsmFeeQueryResult result) {
                        PsnSsmDiscountmodelParam params = new PsnSsmDiscountmodelParam();
                        params.setConversationId(AccSmsNotifyHomeFragment.conversationId);
                        params.setAccountId(uiModel.getAccount().getAccountId());
                        return psnNotifyService.psnSsmDiscountmodel(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnSsmDiscountmodelResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnSsmDiscountmodelResult psnSsmQueryResult) {
                        ((BussFragment) view).closeProgressDialog();
                        uiModel.setDiscountmodel(psnSsmQueryResult.getDiscountmodel());
                        view.psnSmsQueryReturned();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                        view.requestFailed(biiResultErrorException);
                    }
                });
    }

    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }
}
