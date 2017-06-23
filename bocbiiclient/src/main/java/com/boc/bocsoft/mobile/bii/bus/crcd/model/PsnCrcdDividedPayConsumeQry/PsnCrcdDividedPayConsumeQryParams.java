package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeQry;

/**
 * Created by lq7090 on 2016/11/17.
 * 消费账单查询
 */
public class PsnCrcdDividedPayConsumeQryParams {

    /**
     * "method":"PsnCrcdDividedPayConsumeQry",
     * "params":{"accountId":71013506,
     * "currencyCode": "001",
     * "currentIndex":"1",
     * "pageSize":"10"}
     *
     * accountId	账户标识	Integer
     currencyCode	币种	String
     currentIndex	当前页数	String
     pageSize	每页显示条数	String
     */

    private String accountId;
    private String currencyCode;
    private String currentIndex;
    private String pageSize;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
