package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAIncreaseBind;

/**
 * 重新绑定理财账户_参数
 * Created by Wan mengxin on 2016/9/20.
 */
public class PsnOFAIncreaseBindParams {

    //会话
    private String conversationId;
    //理财账户ID
    private String financialAccountId;
    // 银行账号ID
    private String bankAccountId;
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

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
