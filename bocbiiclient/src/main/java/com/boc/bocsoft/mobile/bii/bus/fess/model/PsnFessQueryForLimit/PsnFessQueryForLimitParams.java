package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit;

/**
 * 4.13 013PsnFessQueryForLimit查询个人结售汇额度
 * Created by gwluo on 2016/11/16.
 * 需要与“PsnFessQueryAccount查询结售汇账户列表”接口使用同一conversation
 */

public class PsnFessQueryForLimitParams {
    private String accountId;//	账户ID	String 	M
    private String fessFlag;//	结售汇业务类型 M	结汇：01  购汇：11
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFessFlag() {
        return fessFlag;
    }

    public void setFessFlag(String fessFlag) {
        this.fessFlag = fessFlag;
    }
}
