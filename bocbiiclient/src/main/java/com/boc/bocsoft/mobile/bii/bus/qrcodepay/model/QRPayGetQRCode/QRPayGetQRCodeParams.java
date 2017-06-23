package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode;

/**
 * 获取二维码
 * Created by wangf on 2016/8/29.
 */
public class QRPayGetQRCodeParams {
    private String type;//业务类型
    private String actSeq;//支付卡流水
    private String amount;//交易金额 业务类型为02时可上送
    private String payeeComments;//收款方附言
    private String token;
    private String conversationId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayeeComments() {
        return payeeComments;
    }

    public void setPayeeComments(String payeeComments) {
        this.payeeComments = payeeComments;
    }
}
