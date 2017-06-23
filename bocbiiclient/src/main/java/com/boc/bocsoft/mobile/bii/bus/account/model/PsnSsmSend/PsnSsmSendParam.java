package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSend;

/**
 * Created by wangtong on 2016/6/16.
 */
public class PsnSsmSendParam {

    private String conversationId;

    private String pushterm;

    public String getPushterm() {
        return pushterm;
    }

    public void setPushterm(String pushterm) {
        this.pushterm = pushterm;
    }


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
