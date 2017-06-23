package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 作者：XieDu
 * 创建时间：2016/9/6 8:46
 * 描述：
 */
public class PaymentSignContract {
    public interface View {
        void onQrySecurityFactorSuccess(SecurityFactorModel securityFactorModel);

        void onVerifySuccess(VerifyBean verifyBean);
    }

    public interface Presenter extends BasePresenter {
        void qrySecurityFactor(String serviceId);

        void verifySign(PaymentSignViewModel paymentSignViewModel);
    }
}
