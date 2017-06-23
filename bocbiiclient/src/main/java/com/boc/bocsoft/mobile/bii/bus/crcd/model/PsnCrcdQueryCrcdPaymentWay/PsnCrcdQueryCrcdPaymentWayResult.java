package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay;

/**
 * 作者：xwg on 16/11/21 16:33
 * 还款方式查询
 */
public class PsnCrcdQueryCrcdPaymentWayResult {
    /**
    * 本币还款方式 0 = 主动还款; 1 = 自动还款
     */
    private String localCurrencyPayment;
    /**
    * 本币还款账号
    */
    private String localCurrencyPaymentAccountNo;
    /**
    * 本币还款币种
    */
    private String localCurrencyAccountCurrency;
    /**
    * 本币自动还款金额设定  “FULL”全额；“MINP”最小还款
    */
    private String localCurrencyPaymentMode;
    /**
    * 外币还款方式 0 = 主动还款；1 = 自动还款
     */
    private String foreignCurrencyPayment;
    /**
    * 外币还款账号
    */
    private String foreignCurrencyAccountNo;

    /**
    * 外币自动还款金额设定		“FULL”全额；“MINP”最小还款
    */
    private String foreignCurrencyPaymentMode;

    /**
    * 外币还款外部账户币种（还款账户对应币种）
    */
    private String foreignCurrencyAccountCurrency;

    /**
     * 外币自动还款设定-帐户币种
     */
    private String foreignCurrencyPaymentCurrency;


    public String getForeignCurrencyPaymentCurrency() {
        return foreignCurrencyPaymentCurrency;
    }

    public void setForeignCurrencyPaymentCurrency(String foreignCurrencyPaymentCurrency) {
        this.foreignCurrencyPaymentCurrency = foreignCurrencyPaymentCurrency;
    }

    public String getForeignCurrencyPaymentMode() {
        return foreignCurrencyPaymentMode;
    }

    public void setForeignCurrencyPaymentMode(String foreignCurrencyPaymentMode) {
        this.foreignCurrencyPaymentMode = foreignCurrencyPaymentMode;
    }

    public String getForeignCurrencyAccountCurrency() {
        return foreignCurrencyAccountCurrency;
    }

    public void setForeignCurrencyAccountCurrency(String foreignCurrencyAccountCurrency) {
        this.foreignCurrencyAccountCurrency = foreignCurrencyAccountCurrency;
    }

    public String getForeignCurrencyAccountNo() {
        return foreignCurrencyAccountNo;
    }

    public void setForeignCurrencyAccountNo(String foreignCurrencyAccountNo) {
        this.foreignCurrencyAccountNo = foreignCurrencyAccountNo;
    }

    public String getForeignCurrencyPayment() {
        return foreignCurrencyPayment;
    }

    public void setForeignCurrencyPayment(String foreignCurrencyPayment) {
        this.foreignCurrencyPayment = foreignCurrencyPayment;
    }

    public String getLocalCurrencyAccountCurrency() {
        return localCurrencyAccountCurrency;
    }

    public void setLocalCurrencyAccountCurrency(String localCurrencyAccountCurrency) {
        this.localCurrencyAccountCurrency = localCurrencyAccountCurrency;
    }

    public String getLocalCurrencyPayment() {
        return localCurrencyPayment;
    }

    public void setLocalCurrencyPayment(String localCurrencyPayment) {
        this.localCurrencyPayment = localCurrencyPayment;
    }

    public String getLocalCurrencyPaymentAccountNo() {
        return localCurrencyPaymentAccountNo;
    }

    public void setLocalCurrencyPaymentAccountNo(String localCurrencyPaymentAccountNo) {
        this.localCurrencyPaymentAccountNo = localCurrencyPaymentAccountNo;
    }

    public String getLocalCurrencyPaymentMode() {
        return localCurrencyPaymentMode;
    }

    public void setLocalCurrencyPaymentMode(String localCurrencyPaymentMode) {
        this.localCurrencyPaymentMode = localCurrencyPaymentMode;
    }
}
