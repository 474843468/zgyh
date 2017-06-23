package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;

/**
 * Created by cry7096 on 2016/11/30.
 */
public class CashAccountSelectContract {
    public interface View {
        void onQueryCashDivBalanceSuccess(String availableBalance);
    }

    public interface Presenter extends BasePresenter {

        void cashSelectAccount(AccountBean accountBean);
    }
}
