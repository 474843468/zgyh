package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PaymentSubmitViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:主动收款付款
 * Created by zhx on 2016/7/7
 */
public class PaymentSubmitContract {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 主动收款付款
         */
        void paymentSubmitSuccess(PaymentSubmitViewModel paymentSubmitViewModel);

        /**
         * 失败回调：
         * 主动收款付款
         */
        void paymentSubmitFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 主动收款付款
         */
        void paymentSubmit(PaymentSubmitViewModel paymentSubmitViewModel);
    }
}
