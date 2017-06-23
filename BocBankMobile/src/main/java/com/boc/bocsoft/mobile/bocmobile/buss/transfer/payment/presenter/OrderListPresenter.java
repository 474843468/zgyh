package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnPaymentOrderListParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnPaymentOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnReminderOrderListParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnReminderOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.OrderListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui.OrderListContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by wangtong on 2016/6/28.
 */
public class OrderListPresenter extends RxPresenter implements OrderListContact.Presenter {

    private OrderListContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private TransferService psnPaymentService;
    private OrderListModel uiModel;

    public void setUiModel(OrderListModel uiModel) {
        this.uiModel = uiModel;
    }

    public OrderListPresenter(OrderListContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        psnPaymentService = new TransferService();
        uiModel = view.getModel();
    }

    @Override
    public void psnTransActQueryReminderOrderList() {
        PsnReminderOrderListParam param = new PsnReminderOrderListParam();
        param.setCurrentIndex(uiModel.getCurrentReminderIndex() + "");
        param.setPageSize(uiModel.getPageSize());
        param.setStartDate(uiModel.getStartReminderDate());
        param.setEndDate(uiModel.getEndReminderDate());
        ((BussFragment) view).showLoadingDialog();
        psnPaymentService.psnTransActQueryReminderOrderList(param)
                .compose(mRxLifecycleManager.<PsnReminderOrderListResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnReminderOrderListResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnReminderOrderListResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        view.requestFailed(biiResultErrorException);
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnReminderOrderListResult result) {
                        uiModel.convertFromPsnReminderOrderListResult(result);
                        view.transActQueryReminderOrderListSucceed();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnTransActQueryPaymentOrderList() {
        PsnPaymentOrderListParam param = new PsnPaymentOrderListParam();
        param.setCurrentIndex(uiModel.getCurrentPayIndex() + "");
        param.setPageSize(uiModel.getPageSize());
        param.setStartDate(uiModel.getStartPayDate());
        param.setEndDate(uiModel.getEndPayDate());
        //显示loading对话框
        ((BussFragment) view).showLoadingDialog();
        psnPaymentService.psnTransActQueryPaymentOrderList(param)
                .compose(mRxLifecycleManager.<PsnPaymentOrderListResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnPaymentOrderListResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnPaymentOrderListResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        view.requestFailed(biiResultErrorException);
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnPaymentOrderListResult result) {
                        uiModel.convertFromPsnPaymentOrderListResult(result);
                        view.transActQueryPaymentOrderListSucceed();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }
}
