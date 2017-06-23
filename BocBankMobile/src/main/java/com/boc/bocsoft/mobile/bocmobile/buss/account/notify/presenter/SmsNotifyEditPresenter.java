package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageAdd.PsnSsmMessageAddParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageAdd.PsnSsmMessageAddResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSend.PsnSsmSendParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSend.PsnSsmSendResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.SmsNotifyEditContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/6/25.
 */
public class SmsNotifyEditPresenter implements SmsNotifyEditContact.Presenter {

    private SmsNotifyEditContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private AccountService accountService;
    private SmsNotifyEditModel uiModel;
    private GlobalService globalService;

    public SmsNotifyEditPresenter(SmsNotifyEditContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        accountService = new AccountService();
        globalService = new GlobalService();
        uiModel = view.getUiModel();
    }

    /**
     * 短信验证码发送
     */
    @Override
    public void psnSsmSend(boolean isSign) {
        PSNCreatConversationParams params = new PSNCreatConversationParams();
        globalService.psnCreatConversation(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnSsmSendResult>>() {
                    @Override
                    public Observable<PsnSsmSendResult> call(String s) {
                        uiModel.setConversitionId(s);
                        PsnSsmSendParam param = new PsnSsmSendParam();
                        param.setConversationId(s);
                        param.setPushterm(uiModel.getPhoneNum());
                        return accountService.psnSsmSend(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnSsmSendResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnSsmSendResult psnSsmSendResult) {
                        view.smsSendReturned();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });
//        PsnSsmSendParam param = new PsnSsmSendParam();
//        param.setPushterm(uiModel.getPhoneNum());
//        if (isSign) {
//            param.setConversationId(AccSmsNotifyHomeFragment.conversationId);
//            accountService.psnSsmSend(param)
//                    .compose(mRxLifecycleManager.<PsnSsmSendResult>bindToLifecycle())
//                    .compose(SchedulersCompat.<PsnSsmSendResult>applyIoSchedulers())
//                    .subscribe(new BIIBaseSubscriber<PsnSsmSendResult>() {
//
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onNext(PsnSsmSendResult s) {
//                        }
//
//                        @Override
//                        public void handleException(BiiResultErrorException biiResultErrorException) {
//                            view.smsSendFailed(biiResultErrorException);
//                        }
//                    });
//        } else {
//
//        }
    }

    @Override
    public void psnSsmMessageAdd() {
        PsnSsmMessageAddParam param = new PsnSsmMessageAddParam();
        param.setConversationId(uiModel.getConversitionId());
        param.setAccountId(uiModel.getSignAccount().getAccountId());
        param.setSsmserviceid("S001");
        param.setPushchannel("1");
        param.setPushterm(uiModel.getPhoneNum());
        param.setLoweramount(uiModel.getMinMoney());
        param.setUpperamount(uiModel.getMaxMoney());
        param.setMobileConfirmCode(uiModel.getVerifyCode());
        ((BussFragment) view).showLoadingDialog(false);
        accountService.psnSsmMessageAdd(param)
                .compose(mRxLifecycleManager.<PsnSsmMessageAddResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnSsmMessageAddResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnSsmMessageAddResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnSsmMessageAddResult psnSsmMessageAddResult) {
                        view.psnSsmMessageAddReturned();
                        ((BussFragment) view).closeProgressDialog();
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
