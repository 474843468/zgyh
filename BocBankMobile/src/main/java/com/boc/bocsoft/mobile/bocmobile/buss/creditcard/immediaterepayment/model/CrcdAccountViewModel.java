package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model;

import java.math.BigDecimal;

/**
 * 作者：xwg on 16/11/25 10:26
 *
 * 信用卡 账户详情
 */
public class CrcdAccountViewModel {

    private String currency;

    private String accountId;

    /**
     *  当前余额 标示位  0-欠款 1-存款  2-余额为0
     */
    private String currentBalanceflag;
    /**
    *  当前余额
    */
    private BigDecimal currentBalance;

    /**
     *  实时余额标示位  ”0“-欠款
     */
    private String loanBalanceLimitFlag;

    /**
    *  实时余额
    */
    private BigDecimal loanBalanceLimit;

    /**
    *   钞汇标示
    */
    private String cashRemit;


    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getCurrentBalanceflag() {
        return currentBalanceflag;
    }

    public void setCurrentBalanceflag(String currentBalanceflag) {
        this.currentBalanceflag = currentBalanceflag;
    }

    public BigDecimal getLoanBalanceLimit() {
        return loanBalanceLimit;
    }

    public void setLoanBalanceLimit(BigDecimal loanBalanceLimit) {
        this.loanBalanceLimit = loanBalanceLimit;
    }

    public String getLoanBalanceLimitFlag() {
        return loanBalanceLimitFlag;
    }

    public void setLoanBalanceLimitFlag(String loanBalanceLimitFlag) {
        this.loanBalanceLimitFlag = loanBalanceLimitFlag;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
