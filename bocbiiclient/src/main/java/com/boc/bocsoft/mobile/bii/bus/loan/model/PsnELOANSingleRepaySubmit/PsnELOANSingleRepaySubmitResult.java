package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit;

/**
 * Created by liuzc on 2016/8/31.
 *中银E贷-单笔提前还款返回
 */
public class PsnELOANSingleRepaySubmitResult {
    private String loanAccount; //贷款账号
    private String transactionId; //网银交易序号
    private String payAccount; //还款账户
    private String advanceRepayCapital; //提前还款本金
    private String advanceRepayInterest; //提前还款利息

    public String getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getAdvanceRepayCapital() {
        return advanceRepayCapital;
    }

    public void setAdvanceRepayCapital(String advanceRepayCapital) {
        this.advanceRepayCapital = advanceRepayCapital;
    }

    public String getAdvanceRepayInterest() {
        return advanceRepayInterest;
    }

    public void setAdvanceRepayInterest(String advanceRepayInterest) {
        this.advanceRepayInterest = advanceRepayInterest;
    }
}
