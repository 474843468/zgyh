package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model;

/**
 * Created by xintong on 2016/7/5.
 */
public class QueryAllChinaBankAccountRes {

    //结果列表
    /**
     * 网银账户标识
     */
    private String accountId;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 是否开通电子现金账户功能
     * 1：有
     * 0：无
     */
    private String isECashAccount;
    /**
     * 1：有
     * 0：无
     */
    private String isMedicalAccount;
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 联行号（所属地区）
     */
    private String accountIbkNum;
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 所属银行机构标识
     */
    private String branchId;
    /**
     * 账户别名
     */
    private String nickName;
    /**
     * 账户状态
     */
    private String accountStatus;
    /**
     * 客户标识
     */
    private String customerId;
    /**
     * 币种
     * 001:人民币
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
     * 币种2
     * 014=美元
     * 027=日元
     */
    private String currencyCode2;
    /**
     * 所属网银机构名称
     */
    private String branchName;
    /**
     * 卡类型描述
     */
    private String cardDescription;
    /**
     * 是否有旧账号
     * 1：有旧账户
     * 0：无旧账户
     */
    private String hasOldAccountFlag;
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
     * 验证因子HMAC加密串
     * 微信银行使用，加密数据源组成：userId+# accountName+#+accountNumber+#+accountType
     * 加密盐值：3%4d0R1h4￥1
     */
    private String verifyFactor;
    /**
     * 电子卡账户标识
     * 1：是
     * 0：否
     */
    private String ecard;

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

    public String getIsECashAccount() {
        return isECashAccount;
    }

    public void setIsECashAccount(String isECashAccount) {
        this.isECashAccount = isECashAccount;
    }

    public String getIsMedicalAccount() {
        return isMedicalAccount;
    }

    public void setIsMedicalAccount(String isMedicalAccount) {
        this.isMedicalAccount = isMedicalAccount;
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

    public String getVerifyFactor() {
        return verifyFactor;
    }

    public void setVerifyFactor(String verifyFactor) {
        this.verifyFactor = verifyFactor;
    }

    public String getEcard() {
        return ecard;
    }

    public void setEcard(String ecard) {
        this.ecard = ecard;
    }

    @Override
    public String toString() {
        return "QueryAllChinaBankAccountRes{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", isECashAccount='" + isECashAccount + '\'' +
                ", isMedicalAccount='" + isMedicalAccount + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountIbkNum='" + accountIbkNum + '\'' +
                ", accountType='" + accountType + '\'' +
                ", branchId=" + branchId +
                ", nickName='" + nickName + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", customerId=" + customerId +
                ", currencyCode='" + currencyCode + '\'' +
                ", currencyCode2='" + currencyCode2 + '\'' +
                ", branchName='" + branchName + '\'' +
                ", cardDescription='" + cardDescription + '\'' +
                ", hasOldAccountFlag='" + hasOldAccountFlag + '\'' +
                ", cardDescriptionCode='" + cardDescriptionCode + '\'' +
                ", verifyFactor='" + verifyFactor + '\'' +
                ", ecard='" + ecard + '\'' +
                '}';
    }

}
