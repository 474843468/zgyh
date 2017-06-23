package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount;

/**
 * Params：首次/重新设定双向宝账户
 * Created by zhx on 2016/11/21
 */
public class PsnVFGSetTradeAccountParams {

    /**
     * accountId : 71796185
     * token : 1rs0mwa4
     */
    // 借记卡账户ID(客户在网银关联的借记卡)
    private String accountId;
    // 防重提交令牌
    private String token;
    private String conversationId;

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getToken() {
        return token;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
