package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeDetailQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 双向宝交易查询--成交状况查询详情界面
 * Created by zc on 2016/11/17
 */
public class SuccessConDetailContract {

    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 成交状况详情查询
         */
        void psnXpadSuccessConDetailQuerySuccess(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult);

        /**
         * 失败回调：
         * 成交状况详情查询
         */
        void psnXpadSuccessConDetailQueryFail(BiiResultErrorException biiResultErrorException);
    }
    public interface Presenter extends BasePresenter {
        /**
         * 接口请求
         * 成交状况详情查询
         */
        void psnXpadSuccessConDetailQuery(XpadVFGTradeDetailQueryModel model);
    }

}
