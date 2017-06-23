package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountCancel;

/**
 * 理财账户解除登记_参数
 * Created by Wan mengxin on 2016/9/20.
 */
public class PsnXpadAccountCancelParams {

    //账户标识
    private String accountId;
    //防重机制
    private String token;
    //会话
    private String conversationId;
    //账户类型
    private String accountType;
    //帐号
    private String bancAccountNo;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBancAccountNo() {
        return bancAccountNo;
    }

    public void setBancAccountNo(String bancAccountNo) {
        this.bancAccountNo = bancAccountNo;
    }
}
