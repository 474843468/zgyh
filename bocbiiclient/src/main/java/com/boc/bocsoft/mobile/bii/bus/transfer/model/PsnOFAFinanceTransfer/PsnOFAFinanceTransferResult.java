package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer;

import java.math.BigDecimal;

/**
 * Created by WYme on 2016/11/1.
 */
public class PsnOFAFinanceTransferResult {


    /**
     * accountId : 29475139
     * currencyCode2 : null
     * cardDescription : null
     * hasOldAccountFlag : null
     * accountName : 付瑜
     * accountNumber : 6013***********0499
     * accountIbkNum : 47669
     * accountType : 119
     * branchId : null
     * nickName : 长城电子借记卡
     * accountStatus : null
     * customerId : null
     * currencyCode : null
     * branchName : null
     * cardDescriptionCode : null
     * isECashAccount : null
     * isMedicalAccount : null
     * ecard : null
     * verifyFactor : null
     * accountKey : null
     */

    private BankAccountBean bankAccount;
    /**
     * bankAccount : {"accountId":29475139,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":null,"accountName":"付瑜","accountNumber":"6013***********0499","accountIbkNum":"47669","accountType":"119","branchId":null,"nickName":"长城电子借记卡","accountStatus":null,"customerId":null,"currencyCode":null,"branchName":null,"cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":null,"ecard":null,"verifyFactor":null,"accountKey":null}
     * transStatus : A
     * transId : 1556078426
     * batSeq : 1082541247
     * financialAccount : {"accountId":117945814,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":null,"accountName":"付瑜","accountNumber":"7744****5663","accountIbkNum":"47669","accountType":"190","branchId":null,"nickName":"网上专属理财账户","accountStatus":null,"customerId":null,"currencyCode":null,"branchName":null,"cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":null,"ecard":null,"verifyFactor":null,"accountKey":null}
     */

    private String transStatus;
    private BigDecimal transId;
    private BigDecimal batSeq;
    /**
     * accountId : 117945814
     * currencyCode2 : null
     * cardDescription : null
     * hasOldAccountFlag : null
     * accountName : 付瑜
     * accountNumber : 7744****5663
     * accountIbkNum : 47669
     * accountType : 190
     * branchId : null
     * nickName : 网上专属理财账户
     * accountStatus : null
     * customerId : null
     * currencyCode : null
     * branchName : null
     * cardDescriptionCode : null
     * isECashAccount : null
     * isMedicalAccount : null
     * ecard : null
     * verifyFactor : null
     * accountKey : null
     */

    private FinancialAccountBean financialAccount;

    public BankAccountBean getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountBean bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public BigDecimal getTransId() {
        return transId;
    }

    public void setTransId(BigDecimal transId) {
        this.transId = transId;
    }

    public BigDecimal getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(BigDecimal batSeq) {
        this.batSeq = batSeq;
    }

    public FinancialAccountBean getFinancialAccount() {
        return financialAccount;
    }

    public void setFinancialAccount(FinancialAccountBean financialAccount) {
        this.financialAccount = financialAccount;
    }

    public static class BankAccountBean {
        private int accountId;
        private String currencyCode2;
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

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
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

    public static class FinancialAccountBean {
        private int accountId;
        private String currencyCode2;
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

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
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
