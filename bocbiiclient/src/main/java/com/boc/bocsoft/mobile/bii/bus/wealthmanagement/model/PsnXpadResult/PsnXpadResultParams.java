package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadResult;

/**
 * 理财账号登记结果_参数
 * Created by Wan mengxin on 2016/9/20.
 */
public class PsnXpadResultParams {

    // 网银账户标识
    private String accountId;
    //	防重机制token
    private String token;
    // 会话ID
    private String conversationId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

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
}
