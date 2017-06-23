package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import android.os.Bundle;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter.PaymentSignConfirmPresenter;

/**
 *
 */
public class PaymentSignConfirmFragment extends PaymentConfirmFragment {

    public static PaymentSignConfirmFragment newInstance(PaymentSignViewModel paymentSignViewModel,
            VerifyBean verifyBean) {
        Bundle args = getBundleForNew(paymentSignViewModel, verifyBean);
        PaymentSignConfirmFragment paymentSignConfirmFragment = new PaymentSignConfirmFragment();
        paymentSignConfirmFragment.setArguments(args);
        return paymentSignConfirmFragment;
    }

    @Override
    protected BaseConfirmContract.Presenter<PaymentSignViewModel> initPresenter() {
        return new PaymentSignConfirmPresenter(this);
    }
}