package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount;

/**
 * Result：首次/重新设定双向宝账户
 * Created by zhx on 2016/11/21
 */
public class PsnVFGSetTradeAccountResult {

    /**
     * currencyCode : null
     * accountId : 71796185
     * cardDescriptionCode : null
     * cardDescription : null
     * isECashAccount : null
     * accountNumber : 4563510800034835115
     * accountName : 内测八
     * customerId : null
     * nickName : 长城电子借记卡
     * branchId : null
     * accountStatus : null
     * accountType : 119
     * branchName : null
     * accountIbkNum : null
     * currencyCode2 : null
     * hasOldAccountFlag : null
     */
    // 网银账户标识
    private int accountId;
    // 账户名称
    private String accountName;
    // 账号
    private String accountNumber;
    // 联行号
    private String accountIbkNum;
    // 账户类型
    private String accountType;
    // 所属银行机构标识
    private String branchId;
    // 账户别名
    private String nickName;
    // 账户状态
    private String accountStatus;
    // 使用客户标识
    private String customerId;
    // 货币码
    private String currencyCode;
    // 货币代码2 ->{目前双币信用卡使用}
    private String currencyCode2;
    // 所属网银机构名称
    private String branchName;
    // 卡类型描述
    private String cardDescription;
    // 是否有旧账
    private String hasOldAccountFlag;
    // 信用卡品种代码虚拟信用卡需要
    private String cardDescriptionCode;
    // 是否开通电子现金功能
    private String isECashAccount;

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setCardDescriptionCode(String cardDescriptionCode) {
        this.cardDescriptionCode = cardDescriptionCode;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public void setIsECashAccount(String isECashAccount) {
        this.isECashAccount = isECashAccount;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public void setCurrencyCode2(String currencyCode2) {
        this.currencyCode2 = currencyCode2;
    }

    public void setHasOldAccountFlag(String hasOldAccountFlag) {
        this.hasOldAccountFlag = hasOldAccountFlag;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getCardDescriptionCode() {
        return cardDescriptionCode;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String getIsECashAccount() {
        return isECashAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public String getCurrencyCode2() {
        return currencyCode2;
    }

    public String getHasOldAccountFlag() {
        return hasOldAccountFlag;
    }
}
