package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount;

/**
 * Created by liuzc on 2016/9/19.
 *中银E贷提前还款手续费试算上传参数
 */
public class PsnELOANRepayCountParams {
    private String loanType; //贷款品种
    private String loanAccount; //贷款账号

    private String currency; //币种
    private String advanceRepayInterest; //提前还款利息
    private String advanceRepayCapital; //提前还款本金
    private String repayAmount; //提前还款金额
    private String accountId; //账户标识
    private String payAccount; //还款账户

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAdvanceRepayInterest() {
        return advanceRepayInterest;
    }

    public void setAdvanceRepayInterest(String advanceRepayInterest) {
        this.advanceRepayInterest = advanceRepayInterest;
    }

    public String getAdvanceRepayCapital() {
        return advanceRepayCapital;
    }

    public void setAdvanceRepayCapital(String advanceRepayCapital) {
        this.advanceRepayCapital = advanceRepayCapital;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }
}
