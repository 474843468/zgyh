package com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor;

/**
 * Created by feibin on 2016/6/16.
 * 安全因子请求Model
 */
public class PsnGetSecurityFactorParams {

    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 服务码
     */
    private String serviceId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
