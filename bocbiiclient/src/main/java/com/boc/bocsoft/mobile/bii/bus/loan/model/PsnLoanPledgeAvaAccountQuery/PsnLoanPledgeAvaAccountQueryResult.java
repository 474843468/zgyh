package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPledgeAvaAccountQuery;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PsnLoanPledgeAvaAccountQueryResult {

    /**
     * 客户标识
     */
    private String customerId;
    /**
     * 所属银行机构标识
     */
    private String branchId;
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 网银账户标识
     */
    private String accountId;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 账户状态
     */
    private String accountStatus;
    /**
     * 联行号（所属地区）
     */
    private String accountIbkNum;
    /**
     * 所属网银机构名称
     */
    private String branchName;
    /**
     * 卡类型描述
     */
    private String cardDescription;
    /**
     * 信用卡品种代码
     * 1	CCDZ	长城电子支付信用卡(CVV卡)
     * 2	PRMC	中银万事达白金信用卡精英版
     * 3	CBWG	长城北京外企联名卡银联金
     * 4	CBWC	长城北京外企联名卡银联普
     * 5	MBWG	长城北京外企联名卡万事达金
     * 6	MBWC	长城北京外企联名卡万事达普
     * 7	VMBG	中国银行公务卡
     * 8	CHBG	长城交管信用卡银联金卡
     * 9	CCDT	长城车贷通信用卡
     * 10	CXAC	长城缴费通信用卡普卡
     */
    private String cardDescriptionCode;
    /**
     * 是否开通电子现金账户功能
     * 1：有
     * 0：无
     */
    private String isECashAccount;
    /**
     * 是否有旧账号
     * 1：有旧账户
     * 0：无旧账户
     */
    private String hasOldAccountFlag;
    /**
     * 币种
     * \001:人民币
     * 014:美元
     * 013:港币元
     * 012:英镑
     * 038:欧元
     * 027:日元
     * 028:加拿大元
     * 029:澳大利亚元
     * 015:瑞士法郎
     * 018:新加坡元
     * 081:澳门元
     */
    private String currencyCode;
    /**
     * 币种2	String	014=美元
     * 027=日元
     */
    private String currencyCode2;
    /**
     * 账户别名
     */
    private String nickName;

    public String getCurrencyCode() { return currencyCode;}

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode;}

    public String getCustomerId() { return customerId;}

    public void setCustomerId(String customerId) { this.customerId = customerId;}

    public String getBranchId() { return branchId;}

    public void setBranchId(String branchId) { this.branchId = branchId;}

    public String getAccountNumber() { return accountNumber;}

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber;}

    public String getAccountId() { return accountId;}

    public void setAccountId(String accountId) { this.accountId = accountId;}

    public String getAccountName() { return accountName;}

    public void setAccountName(String accountName) { this.accountName = accountName;}

    public String getAccountType() { return accountType;}

    public void setAccountType(String accountType) { this.accountType = accountType;}

    public String getAccountStatus() { return accountStatus;}

    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus;}

    public String getAccountIbkNum() { return accountIbkNum;}

    public void setAccountIbkNum(String accountIbkNum) { this.accountIbkNum = accountIbkNum;}

    public String getBranchName() { return branchName;}

    public void setBranchName(String branchName) { this.branchName = branchName;}

    public String getCardDescription() { return cardDescription;}

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCardDescriptionCode() { return cardDescriptionCode;}

    public void setCardDescriptionCode(String cardDescriptionCode) {
        this.cardDescriptionCode = cardDescriptionCode;
    }

    public String getIsECashAccount() { return isECashAccount;}

    public void setIsECashAccount(String isECashAccount) { this.isECashAccount = isECashAccount;}

    public String getHasOldAccountFlag() { return hasOldAccountFlag;}

    public void setHasOldAccountFlag(String hasOldAccountFlag) {
        this.hasOldAccountFlag = hasOldAccountFlag;
    }

    public String getCurrencyCode2() { return currencyCode2;}

    public void setCurrencyCode2(String currencyCode2) { this.currencyCode2 = currencyCode2;}

    public String getNickName() { return nickName;}

    public void setNickName(String nickName) { this.nickName = nickName;}
}
