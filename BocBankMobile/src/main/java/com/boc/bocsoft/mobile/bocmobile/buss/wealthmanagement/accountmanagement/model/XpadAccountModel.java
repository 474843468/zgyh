package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model;

/**
 * 理财——所有账户信息
 * Created by Wan mengxin on 2016/10/9.
 */
public class XpadAccountModel {

    //客户理财账户
    private String xpadAccount;
    //理财银行账户
    private String bankAccount;
    //帐号ID
    private String accountId;
    //账号
    private String accountNumber;
    //账户类型
    private String accountType;
    //联行号
    private String accountIbkNum;
    //账户别名（"网上专属理财账户"）
    private String nickName;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getXpadAccount() {
        return xpadAccount;
    }

    public void setXpadAccount(String xpadAccount) {
        this.xpadAccount = xpadAccount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
