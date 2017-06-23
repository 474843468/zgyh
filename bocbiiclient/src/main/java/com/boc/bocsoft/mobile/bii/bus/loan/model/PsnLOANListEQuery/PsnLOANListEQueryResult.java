package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery;

import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huixiaobo on 2016/6/16.
 * 分条件查询贷款账户列表返回参数 list
 */
public class PsnLOANListEQueryResult {

    /**客户是否有逾期贷款 0：无逾期 1：有逾期*/
    private String overState;
    /**当前日期*/
    private String endDate;
    /**总笔数*/
    private int recordNumber;
    /**是否有更多记录*/
    private String moreFlag;

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

    public String getOverState() {
        return overState;
    }

    /**贷款账户下查询列表*/
    private List<PsnLOANListEQueryBean> loanList;

    public void setLoanList(List<PsnLOANListEQueryBean> loanList) {
        this.loanList = loanList;
    }

    public void setOverState(String overState) {
        this.overState = overState;
    }

    public List<PsnLOANListEQueryBean> getLoanList() {
        return loanList;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "PsnLOANListEQueryResult{" +
                "overState='" + overState + '\'' +
                ", endDate='" + endDate + '\'' +
                ", loanList=" + loanList +
                '}';
    }

    public class PsnLOANListEQueryBean implements Serializable{
        /**贷款账户状态  01：新账户 05：已核准 07：部分放款 08：全额放款 10：取消 12：已展期
         40：结清
         50：到期
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
        /**累计放款金额（贷款金额）*/
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
        /**循环类型 R：循环动用 F：不循环，一次动用 M：不循环，分次动用*/
        private String cycleType;
        /**贷款放款日期*/
        private String loanDate;
        /**逾期状态 00：正常 01：逾期*/
        private String overDueStat;
        /**还款账户标识 0：存款账号 1：长城卡*/
        private String payAccountFlag;
        /**剩余期数*/
        private String remainIssue;
        /**还款方式 F：等额本息G：等额本金B：只还利息N：协议还款*/
        private String interestType;
        /**贷款期限单位 贷款期限的单位，当前的取值只有： M-月*/
        private String loanPeriodUnit;
        /**剩余应还本金*/
        private String remainCapital;
        /**本期应还总金额*/
        private String thisIssueRepayAmount;
        /**本期还款日*/
        private String thisIssueRepayDate;
        /** 逾期期数*/
        private String overdueIssue;
        /** 本期截至当前应还利*/
        private String thisIssueRepayInterest;
        /**标识该账户可执行的交易 1“advance”可以执行提前还款 2、none”，不可执行提前还款*/
        private String transFlag;
        /**可循环贷款标示*/
        private String cycleFlag;
        /**应还未还总金额*/
        private String nopayamtAmount;

        public String getNopayamtAmount() {
            return nopayamtAmount;
        }

        public void setNopayamtAmount(String nopayamtAmount) {
            this.nopayamtAmount = nopayamtAmount;
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

        public String getLoanCycleAvaAmount() {
            return loanCycleAvaAmount;
        }

        public String getLoanCycleDrawdownDate() {
            return loanCycleDrawdownDate;
        }

        public String getLoanCycleMatDate() {
            return loanCycleMatDate;
        }

        public String getLoanCycleLifeTerm() {
            return loanCycleLifeTerm;
        }

        public String getLoanCycleRate() {
            return transRateFormat(loanCycleRate);
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

        public String getLoanDate() {
            return loanDate;
        }

        public String getOverDueStat() {
            return overDueStat;
        }

        public String getPayAccountFlag() {
            return payAccountFlag;
        }

        public String getRemainIssue() {
            return remainIssue;
        }

        public String getInterestType() {
            return interestType;
        }

        public String getLoanPeriodUnit() {
            return loanPeriodUnit;
        }

        public String getRemainCapital() {
            return remainCapital;
        }

        public String getThisIssueRepayAmount() {
            return thisIssueRepayAmount;
        }

        public String getThisIssueRepayDate() {
            return thisIssueRepayDate;
        }

        public String getOverdueIssue() {
            return overdueIssue;
        }

        public String getThisIssueRepayInterest() {
            return thisIssueRepayInterest;
        }

        public String getTransFlag() {
            return transFlag;
        }

        public String getCycleFlag() {
            return cycleFlag;
        }

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

        public void setCycleType(String cycleType) {
            this.cycleType = cycleType;
        }

        public void setOnlineFlag(String onlineFlag) {
            this.onlineFlag = onlineFlag;
        }

        public void setLoanDate(String loanDate) {
            this.loanDate = loanDate;
        }

        public void setOverDueStat(String overDueState) {
            this.overDueStat = overDueState;
        }

        public void setPayAccountFlag(String payAccountFlag) {
            this.payAccountFlag = payAccountFlag;
        }

        public void setRemainIssue(String remainIssue) {
            this.remainIssue = remainIssue;
        }

        public void setInterestType(String interestType) {
            this.interestType = interestType;
        }

        public void setLoanPeriodUnit(String loanPeriodUnit) {
            this.loanPeriodUnit = loanPeriodUnit;
        }

        public void setRemainCapital(String remainCapital) {
            this.remainCapital = remainCapital;
        }

        public void setThisIssueRepayAmount(String thisIssueRepayAmount) {
            this.thisIssueRepayAmount = thisIssueRepayAmount;
        }

        public void setThisIssueRepayDate(String thisIssueRepayDate) {
            this.thisIssueRepayDate = thisIssueRepayDate;
        }

        public void setOverdueIssue(String overdueIssue) {
            this.overdueIssue = overdueIssue;
        }

        public void setThisIssueRepayInterest(String thisIssueRepayInterest) {
            this.thisIssueRepayInterest = thisIssueRepayInterest;
        }

        public void setTransFlag(String transFlag) {
            this.transFlag = transFlag;
        }

        public void setCycleFlag(String cycleFlag) {
            this.cycleFlag = cycleFlag;
        }

        @Override
        public String toString() {
            return "PsnLOANListEQueryBean{" +
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
                    ", loanDate='" + loanDate + '\'' +
                    ", overDueStat='" + overDueStat + '\'' +
                    ", payAccountFlag='" + payAccountFlag + '\'' +
                    ", remainIssue='" + remainIssue + '\'' +
                    ", interestType='" + interestType + '\'' +
                    ", loanPeriodUnit='" + loanPeriodUnit + '\'' +
                    ", remainCapital='" + remainCapital + '\'' +
                    ", thisIssueRepayAmount='" + thisIssueRepayAmount + '\'' +
                    ", thisIssueRepayDate='" + thisIssueRepayDate + '\'' +
                    ", overdueIssue='" + overdueIssue + '\'' +
                    ", thisIssueRepayInterest='" + thisIssueRepayInterest + '\'' +
                    ", transFlag='" + transFlag + '\'' +
                    ", cycleFlag='" + cycleFlag + '\'' +
                    ", nopayamtAmount='" + nopayamtAmount + '\'' +
                    '}';
        }

        /**
         * 将利率格式化成百分比的形式——1.23456，舍弃末尾的零
         */
        public String transRateFormat(String rate) {

            if (StringUtils.isEmpty(rate)) {
                return "";
            }
            if (rate.indexOf(".") > 0) {
                rate = rate.replaceAll("0+?$", "");//去掉多余的0
                rate = rate.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return rate;
        }
    }
}
