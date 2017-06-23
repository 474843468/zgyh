package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdTransferPayOff;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;

/**
 * 作者：xwg on 16/11/21 17:27
 * 关联信用卡还款确认
 */
public class PsnCrcdTransferPayOffResult {
    //交易流水号
    private int transactionId;
    //卡效期
    private String cardDate;
    //转账金额
    private String amount;

    private PsnCommonQueryAllChinaBankAccountResult fromAccount;
    private PsnCommonQueryAllChinaBankAccountResult toAccount;
    //网银账户标识
    private int accountId;
    //账户名称
    private String accountName;
    //账号
    private String accountNumber;
    //联行号
    private String accountIbkNum;
    //账户类型(账户，卡，存折等)
    private String accountType;
    //所属银行机构标识
    private int branchId;
    //账户别名
    private String nickName;
    //账户状态
    private String accountStatus;
    //使用客户标识
    private int customerId;
    //货币码
    private String currencyCode;
    //货币代码2 ->{目前双币信用卡使用}
    private String currencyCode2;
    //所属网银机构名称
    private String branchName;
    //卡类型描述,供页面展示{如：都市信用卡}
    private String cardDescription;
    //是否有旧账号
    private String hasOldAccountFlag;
    //手续费
    private String tranFee;


    public PsnCommonQueryAllChinaBankAccountResult getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(PsnCommonQueryAllChinaBankAccountResult fromAccount) {
        this.fromAccount = fromAccount;
    }

    public PsnCommonQueryAllChinaBankAccountResult getToAccount() {
        return toAccount;
    }

    public void setToAccount(PsnCommonQueryAllChinaBankAccountResult toAccount) {
        this.toAccount = toAccount;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
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

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCurrencyCode2() {
        return currencyCode2;
    }

    public void setCurrencyCode2(String currencyCode2) {
        this.currencyCode2 = currencyCode2;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public String getHasOldAccountFlag() {
        return hasOldAccountFlag;
    }

    public void setHasOldAccountFlag(String hasOldAccountFlag) {
        this.hasOldAccountFlag = hasOldAccountFlag;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTranFee() {
        return tranFee;
    }

    public void setTranFee(String tranFee) {
        this.tranFee = tranFee;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
