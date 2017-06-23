package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 双向宝交易查询--成交状况查询列表界面
 * Created by zc on 2016/11/17
 */
public class SuccessConditionContract {

    public interface View extends BaseView<Presenter>{
        /**
         * 成功回调：
         * 保证金账户列表
         */
        void psnXpadGetRegCurrencySuccess(List<String> result);

        /**
         * 失败回调：
         * 保证金账户列表
         */
        void  psnXpadGetRegCurrencyFail(BiiResultErrorException biiResultErrorException);
        /**
         * 成功回调：
         * 成交状况查询
         */
        void psnXpadSuccessConditionQuerySuccess(XpadVFGTradeInfoQueryModel viewModel);

        /**
         * 失败回调：
         * 成交状况查询
         */
        void psnXpadSuccessConditionQueryFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 成交状况状况查询
         */
        void psnXpadSuccessConditionQuery(XpadVFGTradeInfoQueryModel model);
        /**
         * 保证金账户列表
         */
        void psnXpadGetRegCurrency(XpadPsnVFGGetRegCurrencyModel model);
    }

}
