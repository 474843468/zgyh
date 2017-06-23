package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.ReminderOrderDetailViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:催款指令详情
 * Created by zhx on 2016/7/5
 */
public class ReminderOrderDetailContract {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 催款指令详情
         */
        void reminderOrderDetailSuccess(ReminderOrderDetailViewModel reminderOrderDetailViewModel);

        /**
         * 失败回调：
         * 催款指令详情
         */
        void reminderOrderDetailFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 催款指令详情
         */
        void reminderOrderDetail(ReminderOrderDetailViewModel reminderOrderDetailViewModel);
    }
}
