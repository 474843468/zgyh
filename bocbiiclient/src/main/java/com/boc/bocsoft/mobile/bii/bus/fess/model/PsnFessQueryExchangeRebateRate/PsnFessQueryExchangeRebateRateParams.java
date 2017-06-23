package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRebateRate;

/**
 *I49 4.21 024PsnFessQueryExchangeRebateRate查询结购汇优惠后牌价信息
 * Created by wzn7074 on 2016/11/16.
 */
public class PsnFessQueryExchangeRebateRateParams {

    /**
     * accountId : 125949654
     * cashRemit : 02
     * currency : 014
     * fessFlag : B
     */
    private String accountId;             //账户id
    private String cashRemit;            //汇率类型       01现钞,02现汇
    private String currency;               //String
    private String fessFlag;                //交易类型       11-售汇,01-结汇
    private String amount;                //交易金额

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setFessFlag(String fessFlag) {
        this.fessFlag = fessFlag;
    }

    public void setAmount(String amount) {this.amount = amount;}

    public String getAccountId() {
        return accountId;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getCurrency() {
        return currency;
    }

    public String getFessFlag() {
        return fessFlag;
    }

    public String getAmount() {return amount;}
}

