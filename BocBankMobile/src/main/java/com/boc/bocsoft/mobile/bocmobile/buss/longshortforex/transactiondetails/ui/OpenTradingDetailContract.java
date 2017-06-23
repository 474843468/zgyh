package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeDetailQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 双向宝交易查询--未平仓交易查询详情界面
 * Created by zc on 2016/11/17
 */
public class OpenTradingDetailContract {

    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 未平仓交易详情查询
         */
        void psnXpadHisEntruDetailQuerySuccess(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult);

        /**
         * 失败回调：
         * 未平仓交易详情查询
         */
        void psnXpadHisEntruDetailQueryFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * 接口请求
         * 未平仓交易详情查询
         */
        void psnXpadHisEntruDetailQuery(XpadVFGTradeDetailQueryModel model);

    }

}
