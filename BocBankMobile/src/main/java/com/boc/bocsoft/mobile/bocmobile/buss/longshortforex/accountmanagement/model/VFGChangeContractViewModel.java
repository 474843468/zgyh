package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

/**
 * ViewModel：变更签约账户
 * Created by zhx on 2016/12/14
 */
public class VFGChangeContractViewModel {
    // 原借记卡卡号
    private String oldAccountNumber;
    // 变更后借记卡账户标识
    private String accountId;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String settleCurrency;
    // 保证金账号
    private String marginAccountNo;
    private String conversationId;

    public String getOldAccountNumber() {
        return oldAccountNumber;
    }

    public void setOldAccountNumber(String oldAccountNumber) {
        this.oldAccountNumber = oldAccountNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
