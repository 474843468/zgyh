package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayIsPassSet;

/**
 * 查询是否设置支付密码
 * Created by wangf on 2016/8/30.
 */
public class QRPayIsPassSetParams {

    private String conversationId;
    private String token;
    private String passType;

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

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }
}
