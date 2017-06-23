package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdGetCashDivCommissionCharge;

/**
 * Created by cry7096 on 2016/11/22.
 */
public class PsnCrcdGetCashDivCommissionChargeParams {

    /**
     * accountId : 信用卡账户ID
     * currency : 币种
     * divAmount : 分期金额
     * divPeriod : 分期期数
     * chargeMode : 手续费收取方式
     */
    private String accountId;
    private String currency;
    private String divAmount;
    private String divPeriod;
    private String chargeMode;
    private String divRate;

    public void setDivRate(String divRate) {
        this.divRate = divRate;
    }

    public String getDivRate() {
        return divRate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDivAmount() {
        return divAmount;
    }

    public void setDivAmount(String divAmount) {
        this.divAmount = divAmount;
    }

    public String getDivPeriod() {
        return divPeriod;
    }

    public void setDivPeriod(String divPeriod) {
        this.divPeriod = divPeriod;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }
}
