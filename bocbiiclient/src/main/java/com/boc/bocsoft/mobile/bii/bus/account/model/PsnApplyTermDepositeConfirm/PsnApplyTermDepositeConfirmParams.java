package com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeConfirm;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityParams;

/**
 * Created by liuyang on 2016/6/12.
 */
public class PsnApplyTermDepositeConfirmParams extends PublicSecurityParams{

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
    /**
     * 账户用途
     */
    private String accountPurpose;
    /**
     * 开户原因
     */
    private String openingReason;

    public String getOpeningReason() {
        return openingReason;
    }

    public void setOpeningReason(String openingReason) {
        this.openingReason = openingReason;
    }

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

    public String getAccountPurpose() {
        return accountPurpose;
    }

    public void setAccountPurpose(String accountPurpose) {
        this.accountPurpose = accountPurpose;
    }

}
