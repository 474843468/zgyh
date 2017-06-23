package com.boc.bocsoft.mobile.bii.bus.login.model.PSNGetTokenIdLoginPre;

import java.io.Serializable;

/**
 * Created by feibin on 2016/5/19.
 * I00 公共接口 3.15 015 PSNGetTokenIdLoginPre获取token（登录前）
 */
public class PSNGetTokenIdLoginPreParams implements Serializable {

    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
