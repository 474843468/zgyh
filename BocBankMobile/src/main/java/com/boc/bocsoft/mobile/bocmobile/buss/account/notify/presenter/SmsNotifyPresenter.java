package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDelete.PsnSsmDeleteParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDelete.PsnSsmDeleteResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageDelete.PsnSsmMessageDeleteParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageDelete.PsnSsmMessageDeleteResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui.AccSmsNotifyHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.SmsNotifyContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/7/12.
 */
public class SmsNotifyPresenter implements SmsNotifyContact.Presenter {

    private SmsNotifyContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private AccountService accountService;
    private SmsNotifyModel uiModel;
    private GlobalService globalService;

    public SmsNotifyPresenter(SmsNotifyContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        accountService = new AccountService();
        uiModel = view.getUiModel();
        globalService = new GlobalService();
    }

    @Override
    public void psnSsmMessageDelete() {
        PsnSsmMessageDeleteParam param = new PsnSsmMessageDeleteParam();
        param.setConversationId(AccSmsNotifyHomeFragment.conversationId);
        param.setPushterm(uiModel.getPhoneNum());
        param.setSsmserviceid("S001");
        ((BussFragment) view).showLoadingDialog(false);
        accountService.psnSsmMessageDelete(param)
                .compose(mRxLifecycleManager.<PsnSsmMessageDeleteResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnSsmMessageDeleteResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnSsmMessageDeleteResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnSsmMessageDeleteResult psnSsmMessageDeleteResult) {
                        ((BussFragment) view).closeProgressDialog();
                        view.ssmMessageDeleteReturned();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });
    }

    @Override
    public void psnSsmDelete() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(AccSmsNotifyHomeFragment.conversationId);
        ((BussFragment) view).showLoadingDialog(false);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnSsmDeleteResult>>() {
                    @Override
                    public Observable<PsnSsmDeleteResult> call(String s) {
                        PsnSsmDeleteParam param = new PsnSsmDeleteParam();
                        param.setConversationId(AccSmsNotifyHomeFragment.conversationId);
                        param.setAccountId(uiModel.getSignAccount().getAccountId());
                        param.setAccountNumber(uiModel.getSignAccount().getAccountNumber());
                        param.setToken(s);
                        PsnSsmDeleteParam.MaplistBean bean = new PsnSsmDeleteParam.MaplistBean();
                        bean.setSsmserviceid("S001");
                        bean.setPushterm(uiModel.getPhoneNum());
                        bean.setPushchannel("1");
                        bean.setLoweramount(uiModel.getMinMoney());
                        bean.setUpperamount(uiModel.getMaxMoney());
                        param.setMaplist(bean);
                        return accountService.psnSsmDelete(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnSsmDeleteResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnSsmDeleteResult result) {
                        ((BussFragment) view).closeProgressDialog();
                        view.ssmMessageDeleteReturned();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
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
