package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPaymentOrderListViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:付款指令查询
 * Created by zhx on 2016/7/6
 */
public class QueryPaymentOrderListContract {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 付款指令查询
         */
        void queryPaymentOrderListSuccess(QueryPaymentOrderListViewModel queryPaymentOrderListViewModel);

        /**
         * 失败回调：
         * 付款指令查询
         */
        void queryPaymentOrderListFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 付款指令查询
         */
        void queryPaymentOrderList(QueryPaymentOrderListViewModel queryPaymentOrderListViewModel);
    }
}
