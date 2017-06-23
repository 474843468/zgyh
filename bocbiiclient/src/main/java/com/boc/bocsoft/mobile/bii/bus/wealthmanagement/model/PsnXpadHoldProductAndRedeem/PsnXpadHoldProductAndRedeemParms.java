package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem;

/**
 * I42-4.13 013持有产品赎回  PsnXpadHoldProductAndRedeem   请求Model
 * Created by cff on 2016/9/7.
 */
public class PsnXpadHoldProductAndRedeemParms {
    //防重标识
    private String token;
    //指令交易后台交易ID   指令交易上送字段不可为空
    private String dealCode;
    //会话ID
    private String conversationId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
