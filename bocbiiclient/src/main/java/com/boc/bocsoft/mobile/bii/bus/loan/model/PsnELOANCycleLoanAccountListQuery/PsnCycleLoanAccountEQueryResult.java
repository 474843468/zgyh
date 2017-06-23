package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery;

import java.util.List;

/**
 * Created by Administrator on 2016/6/16.
 * 个人循环贷款查询返回参数 list
 */
public class PsnCycleLoanAccountEQueryResult {


        /**贷款账户状态  01：新账户 05：已核准 07：部分放款 08：全额放款 10：取消 12：已展期
         40：结清 50：到期
         */
        private String loanAccountStats;
        /**贷款品种*/
        private String loanType;
        /**贷款账号*/
        private String accountNumber;
        /**币种*/
        private String currencyCode;
        /**核准金额*/
        private String loanCycleAppAmount;
        /**累计放款金额*/
        private String loanCycleAdvVal;
        /**贷款余额*/
        private String loanCycleBalance;
        /**可用金额*/
        private String loanCycleAvaAmount;
        /**放款截止日*/
        private String loanCycleDrawdownDate;
        /**到期日*/
        private String loanCycleMatDate;
        /**贷款期限*/
        private String loanCycleLifeTerm;
        /**贷款利率*/
        private String loanCycleRate;
        /**还款账户*/
        private String loanCycleRepayAccount;
        /**线上标识 0：非线上 1：线上*/
        private String onlineFlag;
        /**循环类型  R：循环动用 F：不循环，一次动用 M：不循环，分次动用*/
        private String cycleType;
        /**逾期状态 00：正常 01：逾期*/
        private String overDueStat;
        /**额度编号*/
        private String quoteNo;

        public void setLoanAccountStats(String loanAccountStats) {
            this.loanAccountStats = loanAccountStats;
        }

        public void setLoanType(String loanType) {
            this.loanType = loanType;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public void setLoanCycleAppAmount(String loanCycleAppAmount) {
            this.loanCycleAppAmount = loanCycleAppAmount;
        }

        public void setLoanCycleAdvVal(String loanCycleAdvVal) {
            this.loanCycleAdvVal = loanCycleAdvVal;
        }

        public void setLoanCycleBalance(String loanCycleBalance) {
            this.loanCycleBalance = loanCycleBalance;
        }

        public void setLoanCycleAvaAmount(String loanCycleAvaAmount) {
            this.loanCycleAvaAmount = loanCycleAvaAmount;
        }

        public void setLoanCycleDrawdownDate(String loanCycleDrawdownDate) {
            this.loanCycleDrawdownDate = loanCycleDrawdownDate;
        }

        public void setLoanCycleMatDate(String loanCycleMatDate) {
            this.loanCycleMatDate = loanCycleMatDate;
        }

        public void setLoanCycleLifeTerm(String loanCycleLifeTerm) {
            this.loanCycleLifeTerm = loanCycleLifeTerm;
        }

        public void setLoanCycleRate(String loanCycleRate) {
            this.loanCycleRate = loanCycleRate;
        }

        public void setLoanCycleRepayAccount(String loanCycleRepayAccount) {
            this.loanCycleRepayAccount = loanCycleRepayAccount;
        }

        public void setOnlineFlag(String onlineFlag) {
            this.onlineFlag = onlineFlag;
        }

        public void setCycleType(String cycleType) {
            this.cycleType = cycleType;
        }

        public void setOverDueStat(String overDueStat) {
            this.overDueStat = overDueStat;
        }

        public void setQuoteNo(String quoteNo) {
            this.quoteNo = quoteNo;
        }

        public String getLoanAccountStats() {
            return loanAccountStats;
        }

        public String getLoanType() {
            return loanType;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getLoanCycleAppAmount() {
            return loanCycleAppAmount;
        }

        public String getLoanCycleAdvVal() {
            return loanCycleAdvVal;
        }

        public String getLoanCycleBalance() {
            return loanCycleBalance;
        }

        public String getLoanCycleDrawdownDate() {
            return loanCycleDrawdownDate;
        }

        public String getLoanCycleAvaAmount() {
            return loanCycleAvaAmount;
        }

        public String getLoanCycleMatDate() {
            return loanCycleMatDate;
        }

        public String getLoanCycleLifeTerm() {
            return loanCycleLifeTerm;
        }

        public String getLoanCycleRate() {
            return loanCycleRate;
        }

        public String getLoanCycleRepayAccount() {
            return loanCycleRepayAccount;
        }

        public String getOnlineFlag() {
            return onlineFlag;
        }

        public String getCycleType() {
            return cycleType;
        }

        public String getOverDueStat() {
            return overDueStat;
        }

        public String getQuoteNo() {
            return quoteNo;
        }

        @Override
        public String toString() {
            return "PsnCycleLoanAccountEQueryBean{" +
                    "loanAccountStats='" + loanAccountStats + '\'' +
                    ", loanType='" + loanType + '\'' +
                    ", accountNumber='" + accountNumber + '\'' +
                    ", currencyCode='" + currencyCode + '\'' +
                    ", loanCycleAppAmount='" + loanCycleAppAmount + '\'' +
                    ", loanCycleAdvVal='" + loanCycleAdvVal + '\'' +
                    ", loanCycleBalance='" + loanCycleBalance + '\'' +
                    ", loanCycleAvaAmount='" + loanCycleAvaAmount + '\'' +
                    ", loanCycleDrawdownDate='" + loanCycleDrawdownDate + '\'' +
                    ", loanCycleMatDate='" + loanCycleMatDate + '\'' +
                    ", loanCycleLifeTerm='" + loanCycleLifeTerm + '\'' +
                    ", loanCycleRate='" + loanCycleRate + '\'' +
                    ", loanCycleRepayAccount='" + loanCycleRepayAccount + '\'' +
                    ", onlineFlag='" + onlineFlag + '\'' +
                    ", cycleType='" + cycleType + '\'' +
                    ", overDueStat='" + overDueStat + '\'' +
                    ", quoteNo='" + quoteNo + '\'' +
                    '}';
        }
}
