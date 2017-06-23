package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundTAAccount;

/**
 * 010接口 登记基金TA帐户
 * Created by lyf7084 on 2016/11/25.
 */
public class PsnFundTAAccountParams {

    /**
     * taAccount : 123456789012
     * regOrgCode : 011
     * token : ccarucjw
     */
    private String taAccount;
    private String regOrgCode;
    private String token;
    private String conversationId; //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setTaAccount(String taAccount) {
        this.taAccount = taAccount;
    }

    public void setRegOrgCode(String regOrgCode) {
        this.regOrgCode = regOrgCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTaAccount() {
        return taAccount;
    }

    public String getRegOrgCode() {
        return regOrgCode;
    }

    public String getToken() {
        return token;
    }
}
