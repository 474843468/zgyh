package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnTransActReminderOrderDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnTransActReminderOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.ReminderOrderDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.ReminderOrderDetailContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

/**
 * Presenter:催款指令详情
 * Created by zhx on 2016/7/5
 */
public class ReminderOrderDetailPresenter implements ReminderOrderDetailContract.Presenter {
    ReminderOrderDetailContract.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;

    public ReminderOrderDetailPresenter(ReminderOrderDetailContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        transferService = new TransferService();
    }

    @Override
    public void reminderOrderDetail(ReminderOrderDetailViewModel reminderOrderDetailViewModel) {
        PsnTransActReminderOrderDetailParams params = new PsnTransActReminderOrderDetailParams();
        params.setNotifyId(reminderOrderDetailViewModel.getNotifyId());
        transferService.psnTransActReminderOrderDetail(params)
                .compose(mRxLifecycleManager.<PsnTransActReminderOrderDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransActReminderOrderDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActReminderOrderDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.reminderOrderDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActReminderOrderDetailResult psnMobileWithdrawalQueryResult) {

//                        mView.deletePayerSuccess(null);
                    }
                });

    }

    /**
     * 催款指令详情
     * 转换到UI层model
     */
    private ReminderOrderDetailViewModel copyResult2UIModel(PsnTransActReminderOrderDetailResult psnMobileWithdrawalQueryResult) {
        ReminderOrderDetailViewModel reminderOrderDetailViewModel = new ReminderOrderDetailViewModel();

        reminderOrderDetailViewModel.setStatus(psnMobileWithdrawalQueryResult.getStatus());
        reminderOrderDetailViewModel.setTransactionId(psnMobileWithdrawalQueryResult.getTransactionId());
        reminderOrderDetailViewModel.setCreateDate(psnMobileWithdrawalQueryResult.getCreateDate());
        reminderOrderDetailViewModel.setPayeeName(psnMobileWithdrawalQueryResult.getPayeeName());
        reminderOrderDetailViewModel.setPayerName(psnMobileWithdrawalQueryResult.getPayerName());
        reminderOrderDetailViewModel.setPayerAccountNumber(psnMobileWithdrawalQueryResult.getPayerAccountNumber());
        reminderOrderDetailViewModel.setPaymentDate(psnMobileWithdrawalQueryResult.getPaymentDate());
        reminderOrderDetailViewModel.setPayeeAccountNumber(psnMobileWithdrawalQueryResult.getPayeeAccountNumber());
        reminderOrderDetailViewModel.setPayerCustomerId(psnMobileWithdrawalQueryResult.getPayerCustomerId());
        reminderOrderDetailViewModel.setFurInfo(psnMobileWithdrawalQueryResult.getFurInfo());
        reminderOrderDetailViewModel.setPayeeIbk(psnMobileWithdrawalQueryResult.getPayeeIbk());
        reminderOrderDetailViewModel.setPayeeMobile(psnMobileWithdrawalQueryResult.getPayeeMobile());
        reminderOrderDetailViewModel.setPayerMobile(psnMobileWithdrawalQueryResult.getPayerMobile());
        reminderOrderDetailViewModel.setPayerIbknum(psnMobileWithdrawalQueryResult.getPayerIbknum());
        reminderOrderDetailViewModel.setPayeeAccountType(psnMobileWithdrawalQueryResult.getPayeeAccountType());
        reminderOrderDetailViewModel.setPayerAccountType(psnMobileWithdrawalQueryResult.getPayerAccountType());
        reminderOrderDetailViewModel.setTrfAmount(psnMobileWithdrawalQueryResult.getTrfAmount());
        reminderOrderDetailViewModel.setNotifyId(psnMobileWithdrawalQueryResult.getNotifyId());
        reminderOrderDetailViewModel.setTrfCur(psnMobileWithdrawalQueryResult.getTrfCur());
        reminderOrderDetailViewModel.setRequestAmount(psnMobileWithdrawalQueryResult.getRequestAmount());
        reminderOrderDetailViewModel.setCreateChannel(psnMobileWithdrawalQueryResult.getCreateChannel());
        reminderOrderDetailViewModel.setTrfChannel(psnMobileWithdrawalQueryResult.getTrfChannel());
        return reminderOrderDetailViewModel;
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
