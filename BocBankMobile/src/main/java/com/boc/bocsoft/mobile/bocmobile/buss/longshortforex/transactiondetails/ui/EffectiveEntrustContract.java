package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 双向宝交易查询--有效委托查询列表界面
 * Created by zc on 2016/11/17
 */
public class EffectiveEntrustContract {

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
         * 有效委托状况查询
         */
        void psnXpadEffectiveEntrustQuerySuccess(XpadVFGTradeInfoQueryModel viewModel);


        /**
         * 失败回调：
         * 有效委托状况查询
         */
        void psnXpadEffectiveEntrustQueryFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * 接口请求
         * 有效委托状况查询
         */
        void psnXpadEffectiveEntrustQuery(XpadVFGTradeInfoQueryModel model);
        /**
         * 获取结算币种
         */
        void psnXpadGetRegCurrency(XpadPsnVFGGetRegCurrencyModel model);
    }

}
