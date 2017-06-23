package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnPaymentOrderDetailParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnPaymentOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui.PaymentDetailContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by wangtong on 2016/6/28.
 */
public class PaymentDetailPresenter extends RxPresenter implements PaymentDetailContact.Presenter {

    private PaymentDetailContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private TransferService psnPaymentService;
    private PaymentDetailModel uiModel;

    public PaymentDetailPresenter(PaymentDetailContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        psnPaymentService = new TransferService();
        uiModel = view.getModel();
    }

    @Override
    public void psnTransActPaymentOrderDetail() {
        PsnPaymentOrderDetailParam param = new PsnPaymentOrderDetailParam();
        param.setNotifyId(uiModel.getNotifyId());
        ((BussFragment) view).showLoadingDialog();
        psnPaymentService.psnTransActPaymentOrderDetail(param)
                .compose(mRxLifecycleManager.<PsnPaymentOrderDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnPaymentOrderDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnPaymentOrderDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) view).closeProgressDialog();
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnPaymentOrderDetailResult psnTransOrderListResult) {
                        uiModel.transFromPresent(psnTransOrderListResult);
                        ((BussFragment) view).closeProgressDialog();
                        view.paymentOrderDetailReturned();
                    }
                });
    }
}
