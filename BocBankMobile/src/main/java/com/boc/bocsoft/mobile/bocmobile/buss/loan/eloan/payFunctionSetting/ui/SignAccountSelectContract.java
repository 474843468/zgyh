package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.LoanAccountCheckResult;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class SignAccountSelectContract {

    public interface View {
        void onCheckAssignAccountSuccess();
    }

    public interface Presenter extends BasePresenter {
        void setInitData(String conversationId, String loanCurrencyCode);

        void checkAssignAccount(AccountBean accountBean);
    }
}