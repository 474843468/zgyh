package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryForeignPayOff;

/**
 * 作者：xwg on 16/11/21 18:20
 * 信用卡查询购汇还款信息
 */
public class PsnCrcdQueryForeignPayOffParams {
    /**
    *   转入账户标识
    */
    private String accountId;
    /**
    *   购汇还款账户币种
    */
    private String currType;

    private String conversationId;

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

    public String getCurrType() {
        return currType;
    }

    public void setCurrType(String currType) {
        this.currType = currType;
    }
}
