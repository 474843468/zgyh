package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog;

/**
 * Created by fanbin on 16/10/14.
 */
public class QRPayGetAccountCatalogParams {
    //默认支付卡账户流水号  如果不上送，则取默认卡，如果默认卡未设置则抛错
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
