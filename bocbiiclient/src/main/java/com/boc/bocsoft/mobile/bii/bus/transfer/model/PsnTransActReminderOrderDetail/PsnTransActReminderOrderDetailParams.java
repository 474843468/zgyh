package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail;

/**
 * Params:催款指令详情
 * Created by zhx on 2016/7/5
 */
public class PsnTransActReminderOrderDetailParams {

    private String conversationId;

    private String notifyId;

    private String token;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }
}
