package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model;

/**
 * ViewModel：查询客户最近操作的理财账号
 * Created by zhx on 2016/9/7
 */
public class XpadRecentAccountQueryViewModel {
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

    public String getXpadAccount() {
        return xpadAccount;
    }

    public void setXpadAccount(String xpadAccount) {
        this.xpadAccount = xpadAccount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBancID() {
        return bancID;
    }

    public void setBancID(String bancID) {
        this.bancID = bancID;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getXpadAccountSatus() {
        return xpadAccountSatus;
    }

    public void setXpadAccountSatus(String xpadAccountSatus) {
        this.xpadAccountSatus = xpadAccountSatus;
    }
}
