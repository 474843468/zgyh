package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model;

import java.io.Serializable;

/**
 * 投资交易账号绑定信息
 * Created by lzc4524 on 2016/11/28.
 */
public class InvstBindingInfoViewModel implements Serializable {
    private String accountId; //资金账户ID
    private String accountType; //资金账户类型
    private String investAccount; //投资交易账户
    private String account; //资金账号
    private String accountNickName; //资金账号别名
    private String bankId; //网银机构号
    private String ibkNum; //省行联行号

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getInvestAccount() {
        return investAccount;
    }

    public void setInvestAccount(String investAccount) {
        this.investAccount = investAccount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
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
}
