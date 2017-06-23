package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTransDetail;

/**
 * 4.8 008查询信用卡已出账单交易明细PsnCrcdQueryBilledTransDetail
 * Created by liuweidong on 2016/12/14.
 */

public class PsnCrcdQueryBilledTransDetailParams {
    private String conversationId;
    private String creditcardId;// 信用卡账户标识
    private String statementMonth;// 已出账单月份
    private String accountType;// 帐户类型
    private String pageNo;// 页码
    private String primary;// 分页查询唯一号
    private String lineNum;// 每页返回行数

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCreditcardId() {
        return creditcardId;
    }

    public void setCreditcardId(String creditcardId) {
        this.creditcardId = creditcardId;
    }

    public String getStatementMonth() {
        return statementMonth;
    }

    public void setStatementMonth(String statementMonth) {
        this.statementMonth = statementMonth;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }
}
