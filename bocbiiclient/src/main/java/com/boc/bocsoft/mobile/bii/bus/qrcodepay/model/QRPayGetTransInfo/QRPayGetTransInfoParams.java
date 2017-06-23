package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransInfo;

/**
 * 查询反扫支付交易信息
 * Created by wangf on 2016/9/19.
 */
public class QRPayGetTransInfoParams {

    //会话id
    private String conversationId;
    private String settleKey;



    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSettleKey() {
        return settleKey;
    }

    public void setSettleKey(String settleKey) {
        this.settleKey = settleKey;
    }
}
