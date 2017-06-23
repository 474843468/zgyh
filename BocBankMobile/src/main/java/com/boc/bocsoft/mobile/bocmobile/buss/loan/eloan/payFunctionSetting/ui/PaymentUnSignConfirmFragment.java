package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import android.os.Bundle;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter.PaymentUnSignConfirmPresenter;

/**
 *
 */
public class PaymentUnSignConfirmFragment extends PaymentConfirmFragment {

    public static PaymentUnSignConfirmFragment newInstance(PaymentSignViewModel paymentSignViewModel,
            VerifyBean verifyBean) {
        Bundle args = getBundleForNew(paymentSignViewModel, verifyBean);
        PaymentUnSignConfirmFragment paymentUnSignConfirmFragment =
                new PaymentUnSignConfirmFragment();
        paymentUnSignConfirmFragment.setArguments(args);
        return paymentUnSignConfirmFragment;
    }

    @Override
    protected BaseConfirmContract.Presenter<PaymentSignViewModel> initPresenter() {
        return new PaymentUnSignConfirmPresenter(this);
    }
}