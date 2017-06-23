package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model;

import java.io.Serializable;

/**
 * 中银E贷-单笔提前还款请求-view层
 * Created by liuzc on 2016/9/2.
 */
public class SinglePrepaySubmitReq implements Serializable {
    private String quoteNo; //额度编号
    private String accountId; //账户标识， 还款账户的ID
    private String payAccount; //还款账户， 还款账户的账号
    private String loanType; //贷款品种
    private String loanAccount; //贷款账号
    private String currency; //币种
    /**
     * 提前还款利息, 取值为007接口返回项“thisIssueRepayInterest”；且此值精确度必须为两位小数，
     * 若不为两位小数需前端做四舍五入处理为两位小数后再上送
     */
    private String advanceRepayInterest;
    private String advanceRepayCapital; //提前还款本金
    private String repayAmount; //提前还款金额

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
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
}
