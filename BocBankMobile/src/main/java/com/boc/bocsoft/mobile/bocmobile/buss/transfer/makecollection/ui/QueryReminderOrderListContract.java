package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryReminderOrderListViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:催款指令查询
 * Created by zhx on 2016/7/5
 */
public class QueryReminderOrderListContract {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 催款指令查询
         */
        void queryReminderOrderListSuccess(QueryReminderOrderListViewModel queryReminderOrderListViewModel);

        /**
         * 失败回调：
         * 催款指令查询
         */
        void queryReminderOrderListFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 催款指令查询
         */
        void queryReminderOrderList(QueryReminderOrderListViewModel queryReminderOrderListViewModel);
    }
}
