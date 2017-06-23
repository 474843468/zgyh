package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincAccountList;

/**
 *  I41 基金076接口 获取资金账号列表
 * Created by lyf7084 on 2016/11/24.
 *  接口文档字段不完整，以下参考2016/12/23返回报文
 */
public class PsnFincAccountListResultBean {

    /**
     * ecard : 0
     * accountCatalog : null
     * accountId : 125966203
     * currencyCode2 : null
     * cardDescription : null
     * hasOldAccountFlag : 0
     * accountName : 胖胖
     * accountNumber : 4563513600036932697
     * accountIbkNum : 43016
     * accountType : 119
     * branchId : 50978
     * nickName : 长城电子借记卡
     * accountStatus : V
     * customerId : 114323317
     * currencyCode : 001
     * branchName : 陕西省分行营业部
     * cardDescriptionCode : null
     * isECashAccount : null
     * isMedicalAccount : 0
     * verifyFactor : null
     */

    private String ecard;
    private Object accountCatalog;
    private int accountId;
    private Object currencyCode2;
    private Object cardDescription;
    private String hasOldAccountFlag;
    private String accountName;
    private String accountNumber;
    private String accountIbkNum;
    private String accountType;
    private int branchId;
    private String nickName;
    private String accountStatus;
    private int customerId;
    private String currencyCode;
    private String branchName;
    private Object cardDescriptionCode;
    private Object isECashAccount;
    private String isMedicalAccount;
    private Object verifyFactor;

    public String getEcard() {
        return ecard;
    }

    public void setEcard(String ecard) {
        this.ecard = ecard;
    }

    public Object getAccountCatalog() {
        return accountCatalog;
    }

    public void setAccountCatalog(Object accountCatalog) {
        this.accountCatalog = accountCatalog;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Object getCurrencyCode2() {
        return currencyCode2;
    }

    public void setCurrencyCode2(Object currencyCode2) {
        this.currencyCode2 = currencyCode2;
    }

    public Object getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(Object cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getHasOldAccountFlag() {
        return hasOldAccountFlag;
    }

    public void setHasOldAccountFlag(String hasOldAccountFlag) {
        this.hasOldAccountFlag = hasOldAccountFlag;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Object getCardDescriptionCode() {
        return cardDescriptionCode;
    }

    public void setCardDescriptionCode(Object cardDescriptionCode) {
        this.cardDescriptionCode = cardDescriptionCode;
    }

    public Object getIsECashAccount() {
        return isECashAccount;
    }

    public void setIsECashAccount(Object isECashAccount) {
        this.isECashAccount = isECashAccount;
    }

    public String getIsMedicalAccount() {
        return isMedicalAccount;
    }

    public void setIsMedicalAccount(String isMedicalAccount) {
        this.isMedicalAccount = isMedicalAccount;
    }

    public Object getVerifyFactor() {
        return verifyFactor;
    }

    public void setVerifyFactor(Object verifyFactor) {
        this.verifyFactor = verifyFactor;
    }
}
