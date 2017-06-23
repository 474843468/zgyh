package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo;

/**
 * 信用卡设置类信息查询
 * Created by wangf on 2016/11/22.
 */
public class PsnCrcdQuerySettingsInfoParams {

    //账户ID
    private String accountId;
    //会话id
    private String conversationId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
