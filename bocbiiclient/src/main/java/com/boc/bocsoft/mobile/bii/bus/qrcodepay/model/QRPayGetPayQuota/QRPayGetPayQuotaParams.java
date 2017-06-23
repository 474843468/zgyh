package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayQuota;

/**
 * 查询支付限额
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetPayQuotaParams {

    //无上送参数。

    private String conversationId;
    private String actSeq;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
