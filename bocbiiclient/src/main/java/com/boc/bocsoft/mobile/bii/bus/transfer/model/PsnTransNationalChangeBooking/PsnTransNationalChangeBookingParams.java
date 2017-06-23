package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking;

/**
 * Created by WM on 2016/7/1.
 */
public class PsnTransNationalChangeBookingParams {
    private String transType;
    private String    token;
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
