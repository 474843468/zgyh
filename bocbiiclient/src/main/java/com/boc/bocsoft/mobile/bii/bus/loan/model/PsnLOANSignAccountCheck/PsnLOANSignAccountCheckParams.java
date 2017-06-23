package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANSignAccountCheck;

/**
 * 作者：XieDu
 * 创建时间：2016/9/2 15:35
 * 描述：
 */
public class PsnLOANSignAccountCheckParams {
    private String accountId;
    private String accountNumber;
    private String conversationId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
