package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model;

/**
 * 作者：XieDu
 * 创建时间：2016/10/15 20:29
 * 描述：
 */
public class QryPaymentInfoParams {
    private String quoteOrActNo;
    private boolean loanTypeFlag;

    public boolean isLoanTypeFlag() {
        return loanTypeFlag;
    }

    public void setLoanTypeFlag(boolean loanTypeFlag) {
        this.loanTypeFlag = loanTypeFlag;
    }

    public String getQuoteOrActNo() {
        return quoteOrActNo;
    }

    public void setQuoteOrActNo(String quoteOrActNo) {
        this.quoteOrActNo = quoteOrActNo;
    }
}
