package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.BailAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * @author wangyang
 *         2016/12/15 17:26
 *         双向宝购买
 */
public interface PurchaseContract {

    interface Presenter extends BasePresenter {
        /**
         * 查询结算币种与牌价
         * @param model
         */
        void queryCurrencyAndSingleRate(PurchaseModel model);
        /**
         * 查询牌价
         * @param model
         */
        void querySingleRate(PurchaseModel model);
        /**
         * 查询保证金账户列表
         */
        void queryBailAccount();
        /**
         * 定时刷新牌价
         * @param model
         */
        void intervalQuerySingleRate(PurchaseModel model);
        /**
         * 查询平仓交易
         * @param model
         */
        void queryTransaction(PurchaseModel model);
    }

    interface PurchaseView extends BaseView<BasePresenter> {
        void querySingleRate(PurchaseModel model,boolean isCloseProgress);

        void queryCurrencyList(List<String> currencyList);

        void queryBailAccount(List<BailAccount> accounts);
    }
}
