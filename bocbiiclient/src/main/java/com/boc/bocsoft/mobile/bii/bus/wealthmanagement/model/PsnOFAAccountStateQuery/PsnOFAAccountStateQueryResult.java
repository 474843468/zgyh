package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAAccountStateQuery;

/**
 * 网上专属理财账户状态查询_结果
 * Created by Wan mengxin on 2016/9/20.
 */
public class PsnOFAAccountStateQueryResult {


    //开通状态
    private String openStatus;

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    //理财账户 financialAccount对象（以下是该对象中的属性）
    public financialAccount financialAccount;
    public class financialAccount {
        //产品币种
        private String currencyCode;
        //使用客户标识
        private String customerId;
        //所属银行机构标识
        private String branchId;
        //银行账号缓存标识
        private String accountKey;
        //账号
        private String accountNumber;
        //帐号ID
        private String accountId;
        //账户名
        private String accountName;
        //账户类型
        private String accountType;
        //账户状态
        private String accountStatus;
        //联行号
        private String accountIbkNum;
        //所属网银机构名称
        private String branchName;
        //卡类型描述,供页面展示{如：都市信用卡}
        private String cardDescription;
        private String cardDescriptionCode;
        private String isECashAccount;
        //是否有旧账号
        private String hasOldAccountFlag;
        //货币代码2 ->{目前双币信用卡使用}
        private String currencyCode2;
        //账户别名（"网上专属理财账户"）
        private String nickName;

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

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
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

    // 通过后台查回来的银行账户（根据理财账户查询银行账户）mainAccount对象（以下是该对象中的属性
    public mainAccount mainAccount;
    public class mainAccount {
        // 账号
        private String accountNumber;
        // 账号类型
        private String accountType;
        // 账号缓存标识
        private String accountKey;
        //帐号名称
        private String nickName;

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

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

    //银行账户对象 bankAccount
    public bankAccount bankAccount;
    public class bankAccount {
        // 账号Id
        private String accountId;
        //货币代码2
        private String currencyCode2;
        // 账号缓存标识
        private String cardDescription;
        private String hasOldAccountFlag;
        private String accountName;
        private String accountNumber;
        private String accountIbkNum;
        private String accountType;
        private String branchId;
        private String nickName;
        private String accountStatus;
        private String customerId;
        private String currencyCode;
        private String branchName;
        private String cardDescriptionCode;
        private String isECashAccount;
        private String isMedicalAccount;
        private String ecard;
        private String verifyFactor;
        private String accountKey;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getCurrencyCode2() {
            return currencyCode2;
        }

        public void setCurrencyCode2(String currencyCode2) {
            this.currencyCode2 = currencyCode2;
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

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
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

        public String getIsMedicalAccount() {
            return isMedicalAccount;
        }

        public void setIsMedicalAccount(String isMedicalAccount) {
            this.isMedicalAccount = isMedicalAccount;
        }

        public String getEcard() {
            return ecard;
        }

        public void setEcard(String ecard) {
            this.ecard = ecard;
        }

        public String getVerifyFactor() {
            return verifyFactor;
        }

        public void setVerifyFactor(String verifyFactor) {
            this.verifyFactor = verifyFactor;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }
    }

}
