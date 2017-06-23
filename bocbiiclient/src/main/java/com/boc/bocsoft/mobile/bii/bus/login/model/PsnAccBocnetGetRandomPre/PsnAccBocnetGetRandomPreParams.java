package com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetGetRandomPre;

/**
 * Created by feib on 16/8/2.
 * PsnAccBocnetGetRandomPre登录前获取加密随机数
 * 用于快速余额查询
 */
public class PsnAccBocnetGetRandomPreParams {
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
