package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessBuyExchangeHibs;

/**
 * I49 4.16 016PsnFessBuyExchangeHibs	购汇(HIBS新)
 * Created by wzn7074 on 2016/11/16.
 * 需要与“PsnFessQueryAccount查询结售汇账户列表”接口使用同一conversation
 */
public class PsnFessBuyExchangeHibsParams {
    private String accountId;                         //	账户ID
    private String fundUseInfo;                     //	资金用途
    private String currency;                           //  币种
    private String cashRemit;                        //  钞汇
    private String amount;                            //  购汇金额
    private String conversationId;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFundUseInfo() {
        return fundUseInfo;
    }

    public void setFundUseInfo(String accountId) {
        this.fundUseInfo = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}


/**
 * 购汇资金属性代码
 */