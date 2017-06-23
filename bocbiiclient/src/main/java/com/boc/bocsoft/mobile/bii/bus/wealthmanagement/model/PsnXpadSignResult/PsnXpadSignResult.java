package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignResult;

/**
 * Created by wangtong on 2016/11/1.
 */
public class PsnXpadSignResult {

    private String tranSeq;
    private String contractSeq;
    private String operateDate;
    private int startPeriod;
    private int endPeriod;
    private String accountId;
    private String accountType;
    private String currency;
    private String totalLimit;
    private String totalBalance;
    private String cashLimit;
    private String cashBalance;
    private String installmentLimit;
    private String installmentBalance;
    private String currentBalance;
    private String basePoint;
    private String savingInterest;
    private String savingInterestTax;
    private String showFlag;

    public String getTranSeq() {
        return tranSeq;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public String getContractSeq() {
        return contractSeq;
    }

    public void setContractSeq(String contractSeq) {
        this.contractSeq = contractSeq;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public int getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(int startPeriod) {
        this.startPeriod = startPeriod;
    }

    public int getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(int endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(String totalLimit) {
        this.totalLimit = totalLimit;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(String cashLimit) {
        this.cashLimit = cashLimit;
    }

    public String getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(String cashBalance) {
        this.cashBalance = cashBalance;
    }

    public String getInstallmentLimit() {
        return installmentLimit;
    }

    public void setInstallmentLimit(String installmentLimit) {
        this.installmentLimit = installmentLimit;
    }

    public String getInstallmentBalance() {
        return installmentBalance;
    }

    public void setInstallmentBalance(String installmentBalance) {
        this.installmentBalance = installmentBalance;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(String basePoint) {
        this.basePoint = basePoint;
    }

    public String getSavingInterest() {
        return savingInterest;
    }

    public void setSavingInterest(String savingInterest) {
        this.savingInterest = savingInterest;
    }

    public String getSavingInterestTax() {
        return savingInterestTax;
    }

    public void setSavingInterestTax(String savingInterestTax) {
        this.savingInterestTax = savingInterestTax;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

//    private AccountInfoBean accountInfo;
//    public static class AccountInfoBean {
//        private String currencyCode;
//        private Object customerId;
//        private int branchId;
//        private String accountNumber;
//        private int accountId;
//        private String accountName;
//        private String accountType;
//        private String accountStatus;
//        private String accountIbkNum;
//        private String branchName;
//        private Object cardDescription;
//        private Object hasOldAccountFlag;
//        private Object currencyCode2;
//        private String nickName;
//
//        public String getCurrencyCode() {
//            return currencyCode;
//        }
//
//        public void setCurrencyCode(String currencyCode) {
//            this.currencyCode = currencyCode;
//        }
//
//        public Object getCustomerId() {
//            return customerId;
//        }
//
//        public void setCustomerId(Object customerId) {
//            this.customerId = customerId;
//        }
//
//        public int getBranchId() {
//            return branchId;
//        }
//
//        public void setBranchId(int branchId) {
//            this.branchId = branchId;
//        }
//
//        public String getAccountNumber() {
//            return accountNumber;
//        }
//
//        public void setAccountNumber(String accountNumber) {
//            this.accountNumber = accountNumber;
//        }
//
//        public int getAccountId() {
//            return accountId;
//        }
//
//        public void setAccountId(int accountId) {
//            this.accountId = accountId;
//        }
//
//        public String getAccountName() {
//            return accountName;
//        }
//
//        public void setAccountName(String accountName) {
//            this.accountName = accountName;
//        }
//
//        public String getAccountType() {
//            return accountType;
//        }
//
//        public void setAccountType(String accountType) {
//            this.accountType = accountType;
//        }
//
//        public String getAccountStatus() {
//            return accountStatus;
//        }
//
//        public void setAccountStatus(String accountStatus) {
//            this.accountStatus = accountStatus;
//        }
//
//        public String getAccountIbkNum() {
//            return accountIbkNum;
//        }
//
//        public void setAccountIbkNum(String accountIbkNum) {
//            this.accountIbkNum = accountIbkNum;
//        }
//
//        public String getBranchName() {
//            return branchName;
//        }
//
//        public void setBranchName(String branchName) {
//            this.branchName = branchName;
//        }
//
//        public Object getCardDescription() {
//            return cardDescription;
//        }
//
//        public void setCardDescription(Object cardDescription) {
//            this.cardDescription = cardDescription;
//        }
//
//        public Object getHasOldAccountFlag() {
//            return hasOldAccountFlag;
//        }
//
//        public void setHasOldAccountFlag(Object hasOldAccountFlag) {
//            this.hasOldAccountFlag = hasOldAccountFlag;
//        }
//
//        public Object getCurrencyCode2() {
//            return currencyCode2;
//        }
//
//        public void setCurrencyCode2(Object currencyCode2) {
//            this.currencyCode2 = currencyCode2;
//        }
//
//        public String getNickName() {
//            return nickName;
//        }
//
//        public void setNickName(String nickName) {
//            this.nickName = nickName;
//        }
//    }
}
