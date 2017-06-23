package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

/**
 * Contact：双向宝-账户管理-首次/重新设定双向宝账户
 * Created by zhx on 2016/12/15
 */
public class VFGSetTradeAccountViewModel {
    // 借记卡账户ID(客户在网银关联的借记卡)
    private String accountId;

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode2() {
        return currencyCode2;
    }

    public void setCurrencyCode2(String currencyCode2) {
        this.currencyCode2 = currencyCode2;
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

    public String getHasOldAccountFlag() {
        return hasOldAccountFlag;
    }

    public void setHasOldAccountFlag(String hasOldAccountFlag) {
        this.hasOldAccountFlag = hasOldAccountFlag;
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
}
