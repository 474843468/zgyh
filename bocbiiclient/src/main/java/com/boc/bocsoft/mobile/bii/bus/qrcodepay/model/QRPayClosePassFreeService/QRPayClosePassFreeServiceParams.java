package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayClosePassFreeService;

/**
 * Created by wangf on 2016/8/30.
 */
public class QRPayClosePassFreeServiceParams {

    //会话id
    private String conversationId;
    //支付卡流水
    private String actSeq;
    //防重令牌
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
