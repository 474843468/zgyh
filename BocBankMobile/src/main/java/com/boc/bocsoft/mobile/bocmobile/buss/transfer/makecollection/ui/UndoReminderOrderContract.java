package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.UndoReminderOrderViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:撤消催款指令
 * Created by zhx on 2016/7/6
 */
public class UndoReminderOrderContract {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 撤消催款指令
         */
        void undoReminderOrderSuccess(UndoReminderOrderViewModel undoReminderOrderViewModel);

        /**
         * 失败回调：
         * 撤消催款指令
         */
        void undoReminderOrderFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 撤消催款指令
         */
        void undoReminderOrder(UndoReminderOrderViewModel undoReminderOrderViewModel);
    }
}
