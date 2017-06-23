package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFADisengageBind;

/**
 * 解除理财账户绑定_参数
 * Created by Wan mengxin on 2016/9/20.
 */
public class PsnOFADisengageBindParams {

    //会话
    private String conversationId;
    // 理财账户ID
    private String financialAccountId;
    //银行账号缓存标识
    private String accountKey;
    // 防重机制
    private String token;


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFinancialAccountId() {
        return financialAccountId;
    }

    public void setFinancialAccountId(String financialAccountId) {
        this.financialAccountId = financialAccountId;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
