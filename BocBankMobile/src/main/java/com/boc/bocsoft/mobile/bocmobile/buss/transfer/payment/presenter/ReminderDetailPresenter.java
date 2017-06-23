package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnReminderOrderDetailParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnReminderOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnTransActReminderOrderDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderSms.PsnTransActReminderSmsParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderSms.PsnTransActReminderSmsResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActUndoReminderOrder.PsnTransActUndoReminderOrderResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.ReminderDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui.ReminderDetailContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/6/28.
 */
public class ReminderDetailPresenter extends RxPresenter implements ReminderDetailContact.Presenter {
    private ReminderDetailContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private TransferService psnPaymentService;
    private ReminderDetailModel uiModel;
    private GlobalService globalService;
    private String conversationId;

    public ReminderDetailPresenter(ReminderDetailContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        psnPaymentService = new TransferService();
        globalService = new GlobalService();
        uiModel = view.getModel();
    }

    @Override
    public void psnTransActReminderOrderDetail() {
        PsnReminderOrderDetailParam param = new PsnReminderOrderDetailParam();
        param.setNotifyId(uiModel.getNotifyId());
        ((BussFragment) view).showLoadingDialog();
        psnPaymentService.psnTransActReminderOrderDetail(param)
                .compose(mRxLifecycleManager.<PsnReminderOrderDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnReminderOrderDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnReminderOrderDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnReminderOrderDetailResult psnTransOrderListResult) {
                        uiModel.transFromPresent(psnTransOrderListResult);
                        ((BussFragment) view).closeProgressDialog();
                        view.reminderOrderDetailReturned();
                    }
                });
    }



    @Override
    public void psnTransActReminderSms() {
        PsnTransActReminderSmsParam param = new PsnTransActReminderSmsParam();
        ((BussFragment) view).showLoadingDialog();
        param.setNotifyId(uiModel.getNotifyId());
        psnPaymentService.psnTransActReminderSms(param)
                .compose(mRxLifecycleManager.<PsnTransActReminderSmsResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnTransActReminderSmsResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransActReminderSmsResult psnTransActReminderSmsResult) {
                        ((BussFragment) view).closeProgressDialog();
                        uiModel.setSmsStatus(psnTransActReminderSmsResult.getStatus());
                        view.psnTransActReminderSmsReturned();
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }
                });

    }

    @Override
    public void psnTransActUndoReminderOrder() {
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        ((BussFragment) view).showLoadingDialog();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        conversationId = s;
                        params.setConversationId(conversationId);
                        return globalService.psnGetTokenId(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnTransActUndoReminderOrderResult>>() {
                    @Override
                    public Observable<PsnTransActUndoReminderOrderResult> call(String s) {
                        PsnTransActReminderOrderDetailParams param = new PsnTransActReminderOrderDetailParams();
                        param.setToken(s);
                        param.setNotifyId(uiModel.getNotifyId());
                        param.setConversationId(conversationId);
                        return psnPaymentService.psnTransActUndoReminderOrder(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnTransActUndoReminderOrderResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransActUndoReminderOrderResult psnTransActUndoReminderOrderResult) {
                        ((BussFragment) view).closeProgressDialog();
                        view.psnTransActUndoReminderOrderReturned();
                    }
                });
    }
}
