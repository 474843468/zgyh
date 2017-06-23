package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeDeletePayee;

/**
 * 删除收款人
 * Created by zhx on 2016/7/27
 */
public class PsnTransManagePayeeDeletePayeeParams {
    /**
     * 收款人ID
     */
    private String[] payeeId;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;
    private String conversationId;

    public String[] getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String[] payeeId) {
        this.payeeId = payeeId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}