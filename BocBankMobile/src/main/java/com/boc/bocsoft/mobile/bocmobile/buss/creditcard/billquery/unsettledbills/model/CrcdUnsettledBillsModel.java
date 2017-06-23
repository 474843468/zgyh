package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model;

/**
 * 作者：xwg on 16/12/23 20:56
 */
public class CrcdUnsettledBillsModel {
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
