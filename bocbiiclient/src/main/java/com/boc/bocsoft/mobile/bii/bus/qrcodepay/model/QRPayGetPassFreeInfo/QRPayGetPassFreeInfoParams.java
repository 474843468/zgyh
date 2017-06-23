package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo;

/**
 * 查询小额免密信息
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetPassFreeInfoParams {


    private String actSeq;
    private String conversationId;

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
