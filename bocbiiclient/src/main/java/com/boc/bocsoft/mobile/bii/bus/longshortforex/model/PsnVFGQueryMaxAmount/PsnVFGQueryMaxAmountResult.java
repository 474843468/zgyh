package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount;

import java.math.BigDecimal;

/**
 * Result：保证金账户可转出最大金额查询
 * Created by zhx on 2016/11/28
 */
public class PsnVFGQueryMaxAmountResult {

    /**
     * balance : 100213
     * maxBalance : 1326
     * account : 103513132023113
     */
    private BigDecimal balance; // 账户余额
    private BigDecimal maxBalance; // 最大转帐金额
    private String account; // 保证金帐号

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setMaxBalance(BigDecimal maxBalance) {
        this.maxBalance = maxBalance;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getMaxBalance() {
        return maxBalance;
    }

    public String getAccount() {
        return account;
    }
}
