package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSignConfirmation4Focus;

/**
 * 4.14 014PsnFessSignConfirmation4Focus重点关注对象确认书签署
 * Created by gwluo on 2016/11/16.
 * 需要与“PsnFessQueryAccount查询结售汇账户列表”接口使用同一conversation
 */

public class PsnFessSignConfirmation4FocusParams {

    private String accountId;//	账户ID
    private String token;//	防止重复提交令牌
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
