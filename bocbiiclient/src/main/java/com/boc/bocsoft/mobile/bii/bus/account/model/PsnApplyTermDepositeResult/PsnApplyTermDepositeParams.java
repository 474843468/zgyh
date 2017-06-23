package com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityParams;

/**
 * Created by Administrator on 2016/6/12.
 */
public class PsnApplyTermDepositeParams extends PublicSecurityParams{


    /**
     * 账户绑定介质ID
     */
    private String accountId;
    /**
     * 申请账户类型
     */
    private String accountType;
    /**
     * 申请账户类型名称
     */
    private String accountTypeSMS;
    /**
     * 客户姓名
     */
    private String Name;

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

    public String getAccountTypeSMS() {
        return accountTypeSMS;
    }

    public void setAccountTypeSMS(String accountTypeSMS) {
        this.accountTypeSMS = accountTypeSMS;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

}
