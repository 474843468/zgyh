package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * 更新客户最近操作的理财账号请求
 * Created by liuweidong on 2016/9/13.
 */
public class PsnXpadRecentAccountUpdateParams extends PublicParams{

    /** 账号Id */
    private String xpadAccount;
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 账户开户行
     */
    private String bancID;
    /**
     * 账户状态
     */
    private String accountStatus;
    /**
     * 资金账号
     */
    private String capitalActNoKey;

    public String getCapitalActNoKey() {
        return capitalActNoKey;
    }

    public void setCapitalActNoKey(String capitalActNoKey) {
        this.capitalActNoKey = capitalActNoKey;
    }

    public String getXpadAccount() {
        return xpadAccount;
    }

    public void setXpadAccount(String xpadAccount) {
        this.xpadAccount = xpadAccount;
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

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
