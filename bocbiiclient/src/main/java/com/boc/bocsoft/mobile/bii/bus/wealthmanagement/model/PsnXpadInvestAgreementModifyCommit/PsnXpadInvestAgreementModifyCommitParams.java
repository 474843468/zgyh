package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementModifyCommit;

/**
 * 投资协议修改提交
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadInvestAgreementModifyCommitParams {
    private String accountKey;//	账号缓存标识
    private String agrCode;//	产品协议编号
    private String token;//	防重机制，

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;//	回话Id，

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
