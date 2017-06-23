package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;

/**
 * 作者：XieDu
 * 创建时间：2016/9/18 10:46
 * 描述：
 */
public class PaymentSignSubmitBean {
    private PaymentSignViewModel paymentSignViewModel;
    private BaseSubmitBean baseSubmitBean;

    public PaymentSignViewModel getPaymentSignViewModel() {
        return paymentSignViewModel;
    }

    public void setPaymentSignViewModel(PaymentSignViewModel paymentSignViewModel) {
        this.paymentSignViewModel = paymentSignViewModel;
    }

    public BaseSubmitBean getBaseSubmitBean() {
        return baseSubmitBean;
    }

    public void setBaseSubmitBean(BaseSubmitBean baseSubmitBean) {
        this.baseSubmitBean = baseSubmitBean;
    }
}
