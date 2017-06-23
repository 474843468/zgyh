package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit;

/**
 * Created by zn on 2016/9/12.
 * 4.71 071  PsnXpadShareTransitionCommit锁定期份额转换成交交易  {请求model}
 */
public class PsnXpadShareTransitionCommitParams {

    /**
     * 帐号缓存标识	String
     */
    private String accountKey;
    /**
     * 防重机制，通过PSNGetTokenId接口获取	String
     */
    private String token;
    //会话ID
    private String conversationId;

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
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
