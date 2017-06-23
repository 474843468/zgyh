package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanDetailQry;

/**
 * 查询单笔贷款记录详情请求参数
 * Created by liuzc on 2016/8/16.
 */
public class PsnOnLineLoanDetailQryParams {
    private String loanNumber = null; //贷款编号
    private String conversationId; //会话ID

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
