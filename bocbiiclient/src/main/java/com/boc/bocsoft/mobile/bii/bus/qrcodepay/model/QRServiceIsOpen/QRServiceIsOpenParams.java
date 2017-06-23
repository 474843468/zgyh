package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRServiceIsOpen;

/**
 * 查询客户是否开通二维码服务
 * Created by wangf on 2016/9/19.
 */
public class QRServiceIsOpenParams {

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
