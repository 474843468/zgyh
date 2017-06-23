package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitResult;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class LoanApplyOtherConfirmationContract {

    public interface View {
        void onSubmitSuccess(OnLineLoanSubmitResult submitResult);
    }

    public interface Presenter extends BasePresenter {
        void submit(final OnLineLoanSubmitModel submitModel);
    }
}