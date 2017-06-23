package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import java.math.BigDecimal;

/**
 * Contact：双向宝-账户管理-保证金账户详情
 * Created by zhx on 2016/11/23
 */
public class VFGQueryMaxAmountViewModel {
    private String cashRemit; // 0 ：人民币 1：钞 2：汇
    private String currencyCode; // 签约货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
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
