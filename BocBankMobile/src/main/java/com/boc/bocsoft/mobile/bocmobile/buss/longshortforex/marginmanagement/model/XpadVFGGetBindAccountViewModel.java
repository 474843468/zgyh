package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.model;

/**
 * Created by hty7062 on 2016/11/24.
 * 交易账户
 */
public class XpadVFGGetBindAccountViewModel {

    /**
     * currencyCode : null
     * customerId : null
     * branchId : null
     * accountNumber : 4563510800034835115
     * accountId : 71796185
     * accountName : 内测八
     * accountType : 119
     * accountStatus : null
     * accountIbkNum : null
     * branchName : null
     * cardDescription : null
     * cardDescriptionCode : null
     * isECashAccount : null
     * hasOldAccountFlag : null
     * currencyCode2 : null
     * nickName : 长城电子借记卡
     */

    private String accountId;
    private String accountName;
    private String accountNumber;
    private String accountIbkNum;
    private String accountType;
    private Integer branchId;
    private String nickName;
    private String accountStatus;
    private String customerId;
    private String currencyCode;
    private String currencyCode2;
    private String branchName;
    private String cardDescription;
    private String hasOldAccountFlag;
    private String cardDescriptionCode;
    private String isECashAccount;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCardDescriptionCode() {
        return cardDescriptionCode;
    }

    public void setCardDescriptionCode(String cardDescriptionCode) {
        this.cardDescriptionCode = cardDescriptionCode;
    }

    public String getIsECashAccount() {
        return isECashAccount;
    }

    public void setIsECashAccount(String isECashAccount) {
        this.isECashAccount = isECashAccount;
    }

    public String getHasOldAccountFlag() {
        return hasOldAccountFlag;
    }

    public void setHasOldAccountFlag(String hasOldAccountFlag) {
        this.hasOldAccountFlag = hasOldAccountFlag;
    }

    public String getCurrencyCode2() {
        return currencyCode2;
    }

    public void setCurrencyCode2(String currencyCode2) {
        this.currencyCode2 = currencyCode2;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
