package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceOpen;

/**
 * 开通二维码服务
 * Created by wangf on 2016/9/13.
 */
public class QRServiceOpenParams {

    //无上送参数
    //会话id
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
