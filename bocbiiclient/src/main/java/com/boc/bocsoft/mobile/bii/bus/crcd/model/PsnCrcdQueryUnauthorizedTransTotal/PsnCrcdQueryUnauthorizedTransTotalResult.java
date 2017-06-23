package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTransTotal;

/**
 * Created by liuweidong on 2016/12/14.
 */

public class PsnCrcdQueryUnauthorizedTransTotalResult {

    private String currency;
    private String debitSum;// 借方合计
    private String creditSum;// 贷方合计

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDebitSum() {
        return debitSum;
    }

    public void setDebitSum(String debitSum) {
        this.debitSum = debitSum;
    }

    public String getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(String creditSum) {
        this.creditSum = creditSum;
    }
}
