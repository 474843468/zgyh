package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult;

/**
 * Created by fanbin on 16/9/29.
 */
public class QRPayGetPayeeResultParams {
    //二维码流水号
    private String qrNo;
    //会话id
    private String conversationId;
    public String getQrNo() {
        return qrNo;
    }

    public void setQrNo(String qrNo) {
        this.qrNo = qrNo;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
