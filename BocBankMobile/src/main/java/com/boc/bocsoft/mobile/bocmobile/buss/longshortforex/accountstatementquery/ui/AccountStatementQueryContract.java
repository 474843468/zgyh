package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.model.AccountStatementQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created by wjk7118 on 2016/11/29.
 */
public class AccountStatementQueryContract {

    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * 保证金账户列表
         */
        void psnXpadGetRegCurrencySuccess(List<String> result);

        /**
         * 失败回调：
         * 保证金账户列表
         */
        void psnXpadGetRegCurrencyFail(BiiResultErrorException biiResultErrorException);
        /**
         * 对账单查询列表成功回调
         */
        void queryAccountStatementListSuccess(AccountStatementQueryModel accountStatementQueryModel);

        /**
         * 对账单查询列表失败回调
         */
        void queryAccountStatementListFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * 对账单查询列表
         */
        void queryAccountStatementList(AccountStatementQueryModel accountStatementQueryModel);
        /**
         * 保证金账户列表
         */
        void psnXpadGetRegCurrency(XpadPsnVFGGetRegCurrencyModel model);
    }
}
