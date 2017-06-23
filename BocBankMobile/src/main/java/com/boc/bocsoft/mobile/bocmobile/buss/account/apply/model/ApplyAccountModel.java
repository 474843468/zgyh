package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model;


import java.io.Serializable;

/**
 * 申请定期/活期账户数据模型
 * Created by liuyang on 2016/6/8.
 */
public class ApplyAccountModel implements Serializable {

    /**h
     * 申请活期
     */
    public static final int APPLY_TYPE_CURRENT = 1;
    /**
     * 申请定期
     */
    public static final int APPLY_TYPE_REGULAR = 2;

    public static final int APPLY_ENTY = 3;

    //账户绑定介质ID
    private String accountId;
    //账户绑定介质卡号
    private String accountNumber;
    //申请账户类型
    private String accountType;
    //申请账户类型名称
    private String accountTypeSMS;
    // 账户用途编码
    private String accountPurpose;
    // 账户用途
    private String accountPurposeString;
    // 开户原因编码
    private String openingReason;
    //开户原因
    private String openingReasonString;
    //客户姓名
    private String Name;
    //新卡账户的账号
    private String accountNewNumber;
    //申请账户状态
    private String applyStatus;
    //关联网银状态
    private String linkStatus;

    public String getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(String linkStatus) {
        this.linkStatus = linkStatus;
    }

    public String getAccountNewNumber() {
        return accountNewNumber;
    }

    public void setAccountNewNumber(String accountNewNumber) {
        this.accountNewNumber = accountNewNumber;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
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

    public String getAccountTypeSMS() {
        return accountTypeSMS ;
    }

    public void setAccountTypeSMS(String accountTypeSMS) {
        this.accountTypeSMS = accountTypeSMS;
    }

    public String getAccountPurpose() {
        return accountPurpose + "000";
    }

    public void setAccountPurpose(String accountPurpose) {
        this.accountPurpose = accountPurpose;
    }

    public String getOpeningReason() {
        return openingReason + "000000000";
    }

    public void setOpeningReason(String openingReason) {
        this.openingReason = openingReason;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAccountPurposeString() {
        return accountPurposeString;
    }

    public void setAccountPurposeString(String accountPurposeString) {
        this.accountPurposeString = accountPurposeString;
    }

    public String getOpeningReasonString() {
        return openingReasonString;
    }

    public void setOpeningReasonString(String openingReasonString) {
        this.openingReasonString = openingReasonString;
    }

    public boolean isSuccess() {
        return "1".equals(applyStatus);
    }
}
