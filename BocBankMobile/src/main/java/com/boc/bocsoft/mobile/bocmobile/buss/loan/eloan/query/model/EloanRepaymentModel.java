package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;

import java.io.Serializable;

/**
 * Created by huixiaobo on 2016/6/28.
 * 还款计划
 */
public class EloanRepaymentModel implements Serializable{
    /**还款日期*/
    private String repayDate;
    /**期号*/
    private String loanId;
    /**累计逾期未还次数*/
    private String overdueIssueSum;
    /**逾期还款总额*/
    private String overdueAmountSum;
    /**逾期应还本金*/
    private String overdueCapitalSum ;
    /**逾期应还利息*/
    private String overdueInterestSum ;
    /**还款本金*/
    private String repayCapital;
    /**还款利息*/
    private String repayInterest;
    /**还款金额*/
    private String repayAmount;
    /**判断预期*/
    private boolean isOverdue;
    /**判断历史记录*/
    private boolean isHistory;
    

    public String getRepayDate() {
        return repayDate;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getOverdueIssueSum() {
        return overdueIssueSum;
    }

    public String getOverdueAmountSum() {
        return overdueAmountSum;
    }

    public String getOverdueCapitalSum() {
        return overdueCapitalSum;
    }

    public String getOverdueInterestSum() {
        return overdueInterestSum;
    }

    public String getRepayCapital() {
        return repayCapital;
    }

    public String getRepayInterest() {
        return repayInterest;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public void setOverdueIssueSum(String overdueIssueSum) {
        this.overdueIssueSum = overdueIssueSum;
    }

    public void setOverdueAmountSum(String overdueAmountSum) {
        this.overdueAmountSum = overdueAmountSum;
    }

    public void setOverdueCapitalSum(String overdueCapitalSum) {
        this.overdueCapitalSum = overdueCapitalSum;
    }

    public void setOverdueInterestSum(String overdueInterestSum) {
        this.overdueInterestSum = overdueInterestSum;
    }

    public void setRepayCapital(String repayCapital) {
        this.repayCapital = repayCapital;
    }

    public void setRepayInterest(String repayInterest) {
        this.repayInterest = repayInterest;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public void setHistory(boolean history) {
        isHistory = history;
    }

    @Override
    public String toString() {
        return "EloanRepaymentModel{" +
                "repayDate='" + repayDate + '\'' +
                ", loanId='" + loanId + '\'' +
                ", overdueIssueSum='" + overdueIssueSum + '\'' +
                ", overdueAmountSum='" + overdueAmountSum + '\'' +
                ", overdueCapitalSum='" + overdueCapitalSum + '\'' +
                ", overdueInterestSum='" + overdueInterestSum + '\'' +
                ", repayCapital='" + repayCapital + '\'' +
                ", repayInterest='" + repayInterest + '\'' +
                ", repayAmount='" + repayAmount + '\'' +
                ", isOverdue=" + isOverdue +
                ", isHistory=" + isHistory +
                '}';
    }	
}
