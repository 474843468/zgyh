package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCheckPaymentInfo;

/**
 * 作者：xwg on 16/11/21 17:12
 */
public class PsnCrcdCheckPaymentInfoParams {
    private String accountId;
    private String conversationId;
    /**
    * 币种
    */
    private String currency;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
