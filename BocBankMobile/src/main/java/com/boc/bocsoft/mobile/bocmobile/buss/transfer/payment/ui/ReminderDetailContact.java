package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.ReminderDetailModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/6/28.
 */
public class ReminderDetailContact {
    public interface View extends BaseView<Presenter> {

        /**
         * 付款人详情数据模型
         */
        public ReminderDetailModel getModel();

        /**
         * 收款人详情结果
         */
        public void reminderOrderDetailReturned();

        /**
         * 批量主动收款催收短信返回
         */
        public void psnTransActReminderSmsReturned();

        /**
         * 撤销催款
         */
        public void psnTransActUndoReminderOrderReturned();

    }

    public interface Presenter extends BasePresenter {
        /**
         * 收款人详情
         */
        public void psnTransActReminderOrderDetail();

        /**
         * 批量主动收款催收短信
         */
        public void psnTransActReminderSms();

        /**
         * 批量主动收款催收短信
         */
        public void psnTransActUndoReminderOrder();

    }
}
