package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.threeten.bp.LocalDate;

/**
 * Created by XieDu on 2016/7/9.
 */
public class PsnLOANAccountListAndDetailQueryResult {
    private int recordNumber; //笔数
    private String moreFlag; //是否有更多记录; 1：有更多记录 0：无更多记录
    private List<ListBean> accountList; //额度用款列表及详情

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getMoreFlag() {
        return moreFlag;
    }

    public void setMoreFlag(String moreFlag) {
        this.moreFlag = moreFlag;
    }

    public List<ListBean> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<ListBean> accountList) {
        this.accountList = accountList;
    }


    public static class ListBean implements Serializable{
        /**
         * 币种
         */
        private String currencyCode;
        /**
         * 贷款账号
         */
        private String accountNumber;
        /**
         * 到期日
         */
        private String loanToDate;
        /**
         * 贷款品种
         */
        private String loanType;
        /**
         * 计息方式(还款方式)
         */
        private String interestType;
        /**
         * 贷款金额
         */
        private BigDecimal loanAmount;
        /**
         * 期限
         */
        private int loanPeriod;
        /**
         * 贷款期限单位
         */
        private String loanPeriodUnit;
        /**
         * 当前已计未结息
         */
        private String noclosedInterest;
        /**
         * 逾期期数
         */
        private int overdueIssue;
        /**
         * 已还期数
         */
        private int payedIssueSum;
        /**
         * 剩余应还本金
         */
        private BigDecimal remainCapital;
        /**
         * 贷款剩余期数
         */
        private int remainIssue;
        /**
         * 本期应还款总额（本金+利息）
         */
        private BigDecimal thisIssueRepayAmount;
        /**
         * 本期还款日
         */
        private String thisIssueRepayDate;
        /**
         * 本期截止当前应还利息
         */
        private BigDecimal thisIssueRepayInterest;
        /**
         * 还款账号
         */
        private String payAccountNumber;
        /**
         * 贷款利率
         */
        private String loanRate;
        /**
         * *贷款放款日期
         */
        private String loanDate;

        /**
         * 累计逾期期数
         */
        private String overdueIssueSum;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getLoanToDate() {
            return loanToDate;
        }

        public void setLoanToDate(String loanToDate) {
            this.loanToDate = loanToDate;
        }

        public String getLoanType() {
            return loanType;
        }

        public void setLoanType(String loanType) {
            this.loanType = loanType;
        }

        public String getInterestType() {
            return interestType;
        }

        public void setInterestType(String interestType) {
            this.interestType = interestType;
        }

        public BigDecimal getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(BigDecimal loanAmount) {
            this.loanAmount = loanAmount;
        }

        public int getLoanPeriod() {
            return loanPeriod;
        }

        public void setLoanPeriod(int loanPeriod) {
            this.loanPeriod = loanPeriod;
        }

        public String getLoanPeriodUnit() {
            return loanPeriodUnit;
        }

        public void setLoanPeriodUnit(String loanPeriodUnit) {
            this.loanPeriodUnit = loanPeriodUnit;
        }

        public String getNoclosedInterest() {
            return noclosedInterest;
        }

        public void setNoclosedInterest(String noclosedInterest) {
            this.noclosedInterest = noclosedInterest;
        }

        public int getOverdueIssue() {
            return overdueIssue;
        }

        public void setOverdueIssue(int overdueIssue) {
            this.overdueIssue = overdueIssue;
        }

        public int getPayedIssueSum() {
            return payedIssueSum;
        }

        public void setPayedIssueSum(int payedIssueSum) {
            this.payedIssueSum = payedIssueSum;
        }

        public BigDecimal getRemainCapital() {
            return remainCapital;
        }

        public void setRemainCapital(BigDecimal remainCapital) {
            this.remainCapital = remainCapital;
        }

        public int getRemainIssue() {
            return remainIssue;
        }

        public void setRemainIssue(int remainIssue) {
            this.remainIssue = remainIssue;
        }

        public BigDecimal getThisIssueRepayAmount() {
            return thisIssueRepayAmount;
        }

        public void setThisIssueRepayAmount(BigDecimal thisIssueRepayAmount) {
            this.thisIssueRepayAmount = thisIssueRepayAmount;
        }

        public String getThisIssueRepayDate() {
            return thisIssueRepayDate;
        }

        public void setThisIssueRepayDate(String thisIssueRepayDate) {
            this.thisIssueRepayDate = thisIssueRepayDate;
        }

        public BigDecimal getThisIssueRepayInterest() {
            return thisIssueRepayInterest;
        }

        public void setThisIssueRepayInterest(BigDecimal thisIssueRepayInterest) {
            this.thisIssueRepayInterest = thisIssueRepayInterest;
        }

        public String getPayAccountNumber() {
            return payAccountNumber;
        }

        public void setPayAccountNumber(String payAccountNumber) {
            this.payAccountNumber = payAccountNumber;
        }

        public String getLoanRate() {
            return loanRate;
        }

        public void setLoanRate(String loanRate) {
            this.loanRate = loanRate;
        }

        public String getLoanDate() {
            return loanDate;
        }

        public void setLoanDate(String loanDate) {
            this.loanDate = loanDate;
        }

        public String getOverdueIssueSum() {
            return overdueIssueSum;
        }

        public void setOverdueIssueSum(String overdueIssueSum) {
            this.overdueIssueSum = overdueIssueSum;
        }

        @Override
        public String toString() {
            return "PsnLOANAccountListAndDetailQueryResult{" +
                    "currencyCode='" + currencyCode + '\'' +
                    ", accountNumber='" + accountNumber + '\'' +
                    ", loanToDate=" + loanToDate +
                    ", loanType='" + loanType + '\'' +
                    ", interestType='" + interestType + '\'' +
                    ", loanAmount=" + loanAmount +
                    ", loanPeriod=" + loanPeriod +
                    ", loanPeriodUnit='" + loanPeriodUnit + '\'' +
                    ", noclosedInterest='" + noclosedInterest + '\'' +
                    ", overdueIssue=" + overdueIssue +
                    ", payedIssueSum=" + payedIssueSum +
                    ", remainCapital=" + remainCapital +
                    ", remainIssue=" + remainIssue +
                    ", thisIssueRepayAmount=" + thisIssueRepayAmount +
                    ", thisIssueRepayDate=" + thisIssueRepayDate +
                    ", thisIssueRepayInterest=" + thisIssueRepayInterest +
                    ", payAccountNumber='" + payAccountNumber + '\'' +
                    ", loanRate=" + loanRate +
                    ", overdueIssueSum=" + overdueIssueSum +
                    '}';
        }
    }
}
