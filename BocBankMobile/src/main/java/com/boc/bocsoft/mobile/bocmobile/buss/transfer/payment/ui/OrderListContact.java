package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.OrderListModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/6/28.
 */
public class OrderListContact {

    public interface View extends BaseView<Presenter> {
        /**
         * 接口请求失败
         */
        public void requestFailed(BiiResultErrorException biiResultErrorException);

        /**
         *  获取Ui数据模型
         */
        public OrderListModel getModel();

        /**
         * 请求收款列表成功
         */
        public void transActQueryReminderOrderListSucceed();
        /**
         * 请求收款列表成功
         */
        public void transActQueryPaymentOrderListSucceed();

    }

    public interface Presenter extends BasePresenter {
        /**
         * 请求收款列表
         */
        public void psnTransActQueryReminderOrderList();
        /**
         * 请求付款列表
         */
        public void psnTransActQueryPaymentOrderList();
    }
}
