package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.model.CashInstallmentViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class CashInstallmentContract {

    public interface View {
        void onQueryCashDivCommissionChargeSuccess(CashInstallmentViewModel cashInstallmentViewModel);
        void onQueryCashDivCommissionChargeFail(CashInstallmentViewModel cashInstallmentViewModel);
        void onQrySecurityFactorSuccess(SecurityFactorModel securityFactorModel);
        void onCrcdApplyCashDivPreSuccess(VerifyBean verifyBean, String conversationId);
        void onCrcdApplyCashDivPreFail();
    }

    public interface Presenter extends BasePresenter {
        void getCashDivCommissionCharge(CashInstallmentViewModel cashInstallmentViewModel);
        void qrySecurityFactor(String serviceId);
        void qryCrcdApplyCashDivPre(CashInstallmentViewModel cashInstallmentViewModel);
    }

}