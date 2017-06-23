package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeInfo;

/**
 * 4.18	QRPayGetPayeeInfo 查询收款人信息（新增）
 * Created by fanbin on 16/10/8.
 */
public class QRPayGetPayeeInfoParams {
    //二维码流水号
    private String qrNo;
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
