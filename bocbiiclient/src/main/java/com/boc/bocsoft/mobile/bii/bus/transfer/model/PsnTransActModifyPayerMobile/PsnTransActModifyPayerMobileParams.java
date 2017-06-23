package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile;

/**
 * 修改付款人手机
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActModifyPayerMobileParams {


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;
    /**
     * 付款人ID
     */
    private String payerId;
    /**
     * 付款人更改后手机号
     */
    private String payerMobile;
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

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
