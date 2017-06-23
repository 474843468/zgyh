package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.model;

/**
 * Created by pactera on 2016/12/19.
 */

public class InvtBindingInfoModel {
    //请求参数
    private String invtType;

    public String getInvtType() {
        return invtType;
    }

    public void setInvtType(String invtType) {
        this.invtType = invtType;
    }

    //返回参数
    private String accountId;
    private String accountType;
    private String investAccount;
    private String account;
    private String accountNickName;
    private String bankId;
    private String ibkNum;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getIbkNum() {
        return ibkNum;
    }

    public void setIbkNum(String ibkNum) {
        this.ibkNum = ibkNum;
    }

    public String getInvestAccount() {
        return investAccount;
    }

    public void setInvestAccount(String investAccount) {
        this.investAccount = investAccount;
    }
}
