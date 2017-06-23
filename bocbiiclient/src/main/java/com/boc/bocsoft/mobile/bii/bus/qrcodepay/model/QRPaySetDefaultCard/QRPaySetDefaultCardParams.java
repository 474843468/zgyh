package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetDefaultCard;

/**
 * 设置/修改默认卡
 * Created by wangf on 2016/8/29.
 */
public class QRPaySetDefaultCardParams {

    private String actSeq;//默认支付卡账户流水号
    private String token;//防重令牌
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
