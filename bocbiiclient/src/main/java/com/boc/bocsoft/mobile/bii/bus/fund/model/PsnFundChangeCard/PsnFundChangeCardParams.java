package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundChangeCard;

/**
 * 049接口 变更资金账户
 * Created by lyf7084 on 2016/11/24.
 */
public class PsnFundChangeCardParams {


    /**
     * newAccountId : 115539838
     * token : ccarucjw
     */
    private String newAccountId;
    private String token;
    private String conversationId; //会话ID

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setToken(String token) {this.token = token;}

    public void setNewAccountId(String newAccountId) {
        this.newAccountId = newAccountId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getToken() {
        return this.token;
    }

    public String getNewAccountId() {
        return this.newAccountId;
    }
}
