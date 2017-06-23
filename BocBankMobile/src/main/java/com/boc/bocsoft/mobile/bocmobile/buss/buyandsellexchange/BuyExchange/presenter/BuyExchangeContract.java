package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 购汇
 * Created by gwluo on 2016/11/30.
 */

public class BuyExchangeContract {
    /**
     * 确认界面
     */
    public interface ConfirmView extends BaseView<Presenter> {
        /**
         * 提交处理
         */
        void onSubmit();
    }

    /**
     * 购汇委托页
     */
    public interface BuyTransView extends BaseTransView {
        /**
         * 账户余额和结售汇额度
         */
        void updateAccBalanceLimit();
    }

    /**
     * 结汇委托页
     */
    public interface SellTransView extends BaseTransView {

        /**
         * 剩余额度
         */
        void onQueryLimit();
    }


    /**
     * 结构汇委托页基类
     */
    public interface BaseTransView extends BaseView<Presenter> {
        /**
         * 请求账户成功
         */
        void onAccListSucc();

        /**
         * 提交前接口请求完处理
         */
        void onPreSubmit();

        /**
         * 请求牌价成功
         */
        void onExchangeRateSucc(String rate);

        /**
         * 设置已用、剩余额度
         *
         * @param annAmtUsd
         * @param annRmeAmtUsd
         */
        void upDateFessLimit(String annAmtUsd, String annRmeAmtUsd);

        /**
         * 计算最大限额
         */
        void caculateMaxAmount();
    }

    /**
     * 结汇币种页
     */
    public interface CurrencyAccView extends BaseView<Presenter> {
        /**
         * 账户余额
         *
         * @param isSingle 是否请求单个余额接口
         */
        void updateAccBalance(boolean isSingle);

        /**
         * 请求账户成功
         */
        void onAccListSucc();

        BuyExchangeModel getModel();
    }

    public interface ResultView extends CurrencyAccView {

    }

    public interface Presenter extends BasePresenter {

    }
}
