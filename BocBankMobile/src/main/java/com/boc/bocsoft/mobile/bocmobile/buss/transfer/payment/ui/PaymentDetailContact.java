package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentDetailModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/6/28.
 */
public class PaymentDetailContact {

    public interface View extends BaseView<Presenter> {

        /**
         * 付款人详情数据模型
         */
        public PaymentDetailModel getModel();

        /**
         * 付款人详情结果
         */
        public void paymentOrderDetailReturned();

    }

    public interface Presenter extends BasePresenter {

        /**
         * 付款人详情
         */
        public void psnTransActPaymentOrderDetail();
    }
}
