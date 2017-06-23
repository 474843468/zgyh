package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeLoanInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.LoanRateQueryParams;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 作者：XieDu
 * 创建时间：2016/8/19 21:37
 * 描述：
 */
public class PledgeLoanInfoFillContract {

    public interface View {

        void onLoadEmpty();

        void onQryRateSuccess(String loanRate);

        void onQrySecurityFactorSuccess(SecurityFactorModel securityFactorModel);

        void onVerifySuccess(VerifyBean VerifyBean);
    }

    public interface Presenter<T extends IPledgeLoanInfoFillViewModel> extends BasePresenter {

        void setConversationId(String conversationId);

        void qryRate(LoanRateQueryParams loanRateQueryParams);

        void qrySecurityFactor(String serviceId);
        /**
         * 预交易
         */
        void verify(final T pledgeLoanInfoFillViewModel);
    }
}
