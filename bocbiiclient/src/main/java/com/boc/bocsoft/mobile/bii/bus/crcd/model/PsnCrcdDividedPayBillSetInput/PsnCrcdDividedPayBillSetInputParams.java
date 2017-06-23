package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput;

/**
 * 办理账单分期输入
 * Created by wangf on 2016/11/22.
 */
public class PsnCrcdDividedPayBillSetInputParams {

    //账户标识
    private String accountId;
    //币种
    private String currencyCode;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
