package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

/**
 * Task:变更签约账户
 * Created by zhx on 2016/12/20
 */
public class ChangeTask {
    // 借记卡卡号
    private String accountNumber;
    // 保证金账号
    private String marginAccountNo;
    // 变更后借记卡账户标识
    private String accountId;
    // 结算货币
    private String settleCurrency;

    private int state = 1; // 1表示正在变更，2表示变更成功，3表示变更失败

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
