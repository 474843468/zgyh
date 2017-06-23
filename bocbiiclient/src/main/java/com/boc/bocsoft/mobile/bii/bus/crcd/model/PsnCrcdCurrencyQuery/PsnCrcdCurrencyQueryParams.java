package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery;

/**
 * Created by xdy4486 on 2016/6/27.
 */
public class PsnCrcdCurrencyQueryParams {
    /**
     * 账号
     */
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public PsnCrcdCurrencyQueryParams(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public PsnCrcdCurrencyQueryParams() {
    }
}
