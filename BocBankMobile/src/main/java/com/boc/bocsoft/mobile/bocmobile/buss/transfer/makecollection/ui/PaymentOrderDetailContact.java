package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PaymentOrderDetailViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:付款指令记录详情
 * Created by zhx on 2016/7/6
 */
public class PaymentOrderDetailContact {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 付款指令记录详情
         */
        void paymentOrderDetailSuccess(PaymentOrderDetailViewModel paymentOrderDetailViewModel);

        /**
         * 失败回调：
         * 付款指令记录详情
         */
        void paymentOrderDetailFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 付款指令记录详情
         */
        void paymentOrderDetail(PaymentOrderDetailViewModel paymentOrderDetailViewModel);
    }
}
