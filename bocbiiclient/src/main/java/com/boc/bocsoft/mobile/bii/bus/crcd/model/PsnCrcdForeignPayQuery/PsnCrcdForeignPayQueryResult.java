package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayQuery;

/**
 * 作者：xwg on 16/11/21 18:09
 * 购汇还款信用卡外币账户列表查询
 */
public class PsnCrcdForeignPayQueryResult {
    /**
     *网银账户标识
     */
    private int accountId;
    /**
     *  账户名称
     */
    private String accountName;
    /**
     *账号
     */
    private String accountNumber;
    /**
    *   联行号
    */
    private String accountIbkNum;
    /**
     *  账户类型(账户，卡，存折等)
     */
    private String accountType;
    /**
     *  账户别名
     */
    private String nickName;
    /**
     *  账户状态
     */
    private String accountStatus;
    /**
     *  账户状态
     */
    private String currencyCode;
    /**
     *  货币代码2 ->{目前双币信用卡使用}
     */
    private String currencyCode2;
    /**
     *  卡类型描述,供页面展示{如：都市信用卡}
     */
    private String cardDescription;


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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
