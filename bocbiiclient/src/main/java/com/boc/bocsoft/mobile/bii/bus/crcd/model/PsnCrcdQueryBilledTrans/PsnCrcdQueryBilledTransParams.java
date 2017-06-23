package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTrans;

/**
 * 查询信用卡已出账单
 * Created by wangf on 2016/11/22.
 */
public class PsnCrcdQueryBilledTransParams {

    private String conversationId;
    //账户ID
    private String accountId;
    //已出账单月份 - 格式:yyyy/MM 当日期为9999/99时为查询最近一期账单
    private String statementMonth;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStatementMonth() {
        return statementMonth;
    }

    public void setStatementMonth(String statementMonth) {
        this.statementMonth = statementMonth;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
