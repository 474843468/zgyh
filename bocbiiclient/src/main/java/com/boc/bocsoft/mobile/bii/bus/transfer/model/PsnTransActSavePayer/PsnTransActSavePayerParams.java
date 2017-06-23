package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActSavePayer;

/**
 * Params:主动收款保存常用付款人
 * Created by zhx on 2016/7/5
 */
public class PsnTransActSavePayerParams {
    /**
     * 收款人类型
     */
    private String payerChannel;
    /**
     * 付款人客户号
     */
    private String payerCustId;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;

    private String conversationId;

    public String getPayerChannel() {
        return payerChannel;
    }

    public void setPayerChannel(String payerChannel) {
        this.payerChannel = payerChannel;
    }

    public String getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(String payerCustId) {
        this.payerCustId = payerCustId;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
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
