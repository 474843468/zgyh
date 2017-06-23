package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * 医保账户详情
 * Created by niuguobin on 2016/7/5.
 */
public class MedicalModel {
    /**
     * 账户Id
     */
    private String accountId;
    /**
     * 账户状态
     */
    private String accountStatus;
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 开户日期
     * YYYY/MM/DD
     */
    private LocalDate accOpenDate;
    /**
     * 开户行
     */
    private String accOpenBank;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * 账户详情列表
     */
    private List<MedicalDetail> medicalDetailList;

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

    public LocalDate getAccOpenDate() {
        return accOpenDate;
    }

    public void setAccOpenDate(LocalDate accOpenDate) {
        this.accOpenDate = accOpenDate;
    }

    public String getAccOpenBank() {
        return accOpenBank;
    }

    public void setAccOpenBank(String accOpenBank) {
        this.accOpenBank = accOpenBank;
    }

    public List<MedicalDetail> getMedicalDetailList() {
        return medicalDetailList;
    }

    public void setMedicalDetailList(List<MedicalDetail> medicalDetailList) {
        this.medicalDetailList = medicalDetailList;
    }

    public static class MedicalDetail {
        /**
         * 币种
         */
        private String currencyCode;
        /**
         * 钞汇标识
         */
        private String cashRemit;
        /**
         * 当前余额
         */
        private BigDecimal bookBalance;
        /**
         * 可用余额
         */
        private BigDecimal availableBalance;
        /**
         * 开户日期
         * yyyy/mm/dd
         */
        private LocalDate openDate;
        /**
         * 自子账户装态
         */
        private String status;
        /**
         *结清日期
         * yyyy/mm/dd
         */
        private LocalDate settlementDate;
        /**
         * 冻结金额
         */
        private BigDecimal holdAmount;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public BigDecimal getBookBalance() {
            return bookBalance;
        }

        public void setBookBalance(BigDecimal bookBalance) {
            this.bookBalance = bookBalance;
        }

        public BigDecimal getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(BigDecimal availableBalance) {
            this.availableBalance = availableBalance;
        }

        public LocalDate getOpenDate() {
            return openDate;
        }

        public void setOpenDate(LocalDate openDate) {
            this.openDate = openDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getSettlementDate() {
            return settlementDate;
        }

        public void setSettlementDate(LocalDate settlementDate) {
            this.settlementDate = settlementDate;
        }

        public BigDecimal getHoldAmount() {
            return holdAmount;
        }

        public void setHoldAmount(BigDecimal holdAmount) {
            this.holdAmount = holdAmount;
        }
    }
}
