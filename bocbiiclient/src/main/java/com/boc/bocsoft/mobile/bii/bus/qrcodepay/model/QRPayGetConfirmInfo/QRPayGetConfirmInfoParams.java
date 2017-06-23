package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo;

/**
 * 查询反扫后的交易确认通知
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetConfirmInfoParams {

    //无上送参数

    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


}
