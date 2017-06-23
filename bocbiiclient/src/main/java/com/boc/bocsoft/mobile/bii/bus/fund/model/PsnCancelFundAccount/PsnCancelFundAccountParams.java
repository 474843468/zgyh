package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount;

/**
 * 048接口 基金交易账户销户
 * Created by lyf7084 on 2016/11/24.
 */
public class PsnCancelFundAccountParams {

    /**
     * token : 278y40tk
     */
    private String token;
    private String conversationId; //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
