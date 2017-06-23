package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.FinancePledgeParamsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeProductBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class PledgeLoanFinanceProductSelectContract {

    public interface View {
        void onQryFinancePledgeParamsDataSuccess(FinancePledgeParamsData financePledgeParamsData);
    }

    public interface Presenter extends BasePresenter {
        void setConversationId(String conversationId);
        void qryFinancePledgeParameter(PledgeProductBean pledgeProductBean);
    }
}