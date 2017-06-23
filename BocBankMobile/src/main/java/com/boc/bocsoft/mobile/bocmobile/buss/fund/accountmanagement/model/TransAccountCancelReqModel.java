package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount.PsnCancelFundAccountParams;

/**
 * Created by lyf7084 on 2016/12/21.
 */
public class TransAccountCancelReqModel {

    /**
     * 防重机制token
     */
    private String token;

    /**
     * 会话ID
     */
    private String conversationId;

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

    public PsnCancelFundAccountParams getParams() {
        PsnCancelFundAccountParams params = new PsnCancelFundAccountParams();
        params.setConversationId(this.conversationId);
        params.setToken(this.token);
        return params;
    }
}
