package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundChangeCard.PsnFundChangeCardParams;

/**
 * 由049接口 PsnFundChangeCard 变更资金账户
 * 上送参数：newAccountId token
 * 返回参数：fundAccount，newAccountNo
 * Created by lyf7084 on 2016/12/13.
 */
public class ChangeCardReqModel {

    /**
     * 新帐户Id
     */
    private String newAccountId;

    /**
     * 防重机制token
     */
    private String token;

    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getNewAccountId() {
        return newAccountId;
    }

    public void setNewAccountId(String newAccountId) {
        this.newAccountId = newAccountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PsnFundChangeCardParams getParams() {
        PsnFundChangeCardParams params = new PsnFundChangeCardParams();
        params.setNewAccountId(this.newAccountId);
        params.setToken(this.token);
        params.setConversationId(this.conversationId);
        return params;

    }


}
