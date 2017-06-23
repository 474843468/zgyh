package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 双向宝交易查询--斩仓交易查询列表界面
 * Created by zc on 2016/11/17
 */
public class TakeTradeContract {

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
         * 斩仓交易状况查询
         */
        void psnXpadTakeTradeQuerySuccess(XpadVFGTradeInfoQueryModel viewModel);

        /**
         * 失败回调：
         * 斩仓交易状况查询
         */
        void psnXpadTakeTradeQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 接口请求
         * 斩仓交易状况查询
         */
        void psnXpadTakeTradeQuery(XpadVFGTradeInfoQueryModel model);
        /**
         * 获取结算币种
         */
        void psnXpadGetRegCurrency(XpadPsnVFGGetRegCurrencyModel model);
    }

}
