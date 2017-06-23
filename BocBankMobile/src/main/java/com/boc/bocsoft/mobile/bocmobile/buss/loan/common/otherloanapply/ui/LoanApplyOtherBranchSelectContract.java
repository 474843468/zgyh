package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.BranchSelectViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class LoanApplyOtherBranchSelectContract {

    public interface View {
        void onLoadSuccess(BranchSelectViewModel viewModel);
        void onLoadFailed();

    }

    public interface Presenter extends BasePresenter {
        void getOnLineLoanBranch(BranchSelectViewModel viewModel);
    }
}