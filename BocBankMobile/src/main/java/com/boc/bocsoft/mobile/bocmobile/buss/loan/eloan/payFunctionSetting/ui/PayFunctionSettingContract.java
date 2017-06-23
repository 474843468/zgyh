package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentInfo;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.QryPaymentInfoParams;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class PayFunctionSettingContract {

    public interface View {

        void onQryPaymentInfoSuccess(String conversationId, PaymentInfo paymentInfo);

        void onQryPaymentInfoFailed(String errorMessage);

        void onQryContractSuccess(String contract);

        void onQrySecurityFactorSuccess(SecurityFactorModel securityFactorModel);

        void onVerifySignSuccess(VerifyBean verifyBean);

        void onVerifyUnSignSuccess(VerifyBean verifyBean);

        void onVerifyChangeSuccess(VerifyBean verifyBean);
    }

    public interface Presenter extends BasePresenter {
        void qryPaymentInfo(QryPaymentInfoParams params);

        void qryContract(String eLanguage, String dealType);

        void qrySecurityFactor(String serviceId);

        void verifySign(PaymentSignViewModel paymentSignViewModel);

        void verifyUnSign(PaymentSignViewModel paymentSignViewModel);

        void verifyChange(PaymentSignViewModel paymentSignViewModel);
    }
}