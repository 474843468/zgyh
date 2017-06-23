package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery;

/**
 * 查询客户最近操作的理财账户
 * Created by zhx on 2016/9/5
 */
public class PsnXpadRecentAccountQueryResult {

    /**
     * bancID : 0325
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * accountType : CD
     * xpadAccount : 210007777739
     * xpadAccountSatus : 1
     * accountNo : 100220096407
     */
    // 客户理财账户
    private String xpadAccount;
    // 资金账号
    private String accountNo;
    // 账户类型
    private String accountType;
    // 账户开户行（核心系统中的账号开户行）
    private String bancID;
    // 资金账号缓存标识（若客户已取消该账号，则返回空）
    private String accountKey;
    // 客户账户状态（0：停用 1：可用 不输代表查询全部）
    private String xpadAccountSatus;

    public void setBancID(String bancID) {
        this.bancID = bancID;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setXpadAccount(String xpadAccount) {
        this.xpadAccount = xpadAccount;
    }

    public void setXpadAccountSatus(String xpadAccountSatus) {
        this.xpadAccountSatus = xpadAccountSatus;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBancID() {
        return bancID;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getXpadAccount() {
        return xpadAccount;
    }

    public String getXpadAccountSatus() {
        return xpadAccountSatus;
    }

    public String getAccountNo() {
        return accountNo;
    }
}
