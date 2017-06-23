package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 双向宝交易查询--历史委托查询列表界面
 * Created by zc on 2016/11/17
 */
public class HistoryEntrustContract {

    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 获取结算币种
         */
        void psnXpadGetRegCurrencySuccess(List<String> result);

        /**
         * 失败回调：
         * 获取结算币种
         */
        void psnXpadGetRegCurrencyFail(BiiResultErrorException biiResultErrorException);
        /**
         * 成功回调：
         * 历史委托状况查询
         */
        void psnXpadHistoryEntrustQuerySuccess(XpadVFGTradeInfoQueryModel viewModel);

        /**
         * 失败回调：
         * 历史委托状况查询
         */
        void psnXpadHistoryEntrustQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 历史委托状况查询
         */
        void psnXpadHistoryEntrustQuery(XpadVFGTradeInfoQueryModel model);
        /**
         * 获取结算币种
         */
        void psnXpadGetRegCurrency(XpadPsnVFGGetRegCurrencyModel model);
    }

}
