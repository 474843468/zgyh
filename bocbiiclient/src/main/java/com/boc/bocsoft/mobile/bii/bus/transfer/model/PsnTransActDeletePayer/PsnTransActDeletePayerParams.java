package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer;

/**
 * 删除付款人
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActDeletePayerParams {
    /**
     * 会话id
     */
    private String conversationId;
    /**
     * 付款人ID
     */
    private String payerId;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
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