package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyCommit;

/**
 * 智能协议申请提交交易--请求
 * Created by liuweidong on 2016/11/8.
 */
public class PsnXpadAptitudeTreatyApplyCommitParams {
    private String accountId;
    private String productCode;// 产品代码
    private String productName;
    private String proCur;
    private String token;
    private String conversationId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProCur() {
        return proCur;
    }

    public void setProCur(String proCur) {
        this.proCur = proCur;
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
}
