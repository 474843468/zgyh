package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery;

/**
 * Created by huixiaobo on 2016/6/24.
 * 中银E贷用款详情返回参数
 */
public class PsnDrawingDetailResult {
    /**贷款品种*/
    private String loanType;
    /**贷款账号*/
    private String loanAccount;
    /**币种*/
    private String currency;
    /**贷款金额*/
    private String loanAmount;
    /**期限*/
    private String loanPeriod;
    /**到期日*/
    private String loanToDate;
    /**剩余应还本金*/
    private String remainCapital;
    /**截止当前应还利息*/
    private String thisIssueRepayInterest;
    /**本期应还款总额（本金+利息）*/
    private String thisIssueRepayAmount;
    /**计息方式(还款方式)*/
    private String interestType;
    /**贷款期限单位*/
    private String loanPeriodUnit;
    /**逾期期数*/
    private Integer overdueIssue;
    /**已还期数*/
    private Integer payedIssueSum;
    /**贷款剩余期数*/
    private Integer remainIssue;
    /**本期还款日*/
    private String thisIssueRepayDate;
    /**贷款放款日期*/
    private String loanDate;
    /**贷款利率*/
    private String loanRate;
    /**还款账号*/
    private String payAccountNumber;
    /**还款账户标识*/
    private String payAccountFlag;
    /**还款周期 01：每月 02：每两月 03：每季度 04：每四个月 06：每半年
     * 12：每年 14：每两周98：到期
     */
    private String loanRepayPeriod;
    /**累计逾期期数*/
    private String overdueIssueSum;

    public String getLoanType() {
        return loanType;
    }

    public String getLoanAccount() {
        return loanAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public String getLoanToDate() {
        return loanToDate;
    }

    public String getRemainCapital() {
        return remainCapital;
    }

    public String getThisIssueRepayInterest() {
        return thisIssueRepayInterest;
    }

    public String getThisIssueRepayAmount() {
        return thisIssueRepayAmount;
    }

    public String getInterestType() {
        return interestType;
    }

    public String getLoanPeriodUnit() {
        return loanPeriodUnit;
    }

    public Integer getOverdueIssue() {
        return overdueIssue;
    }

    public Integer getPayedIssueSum() {
        return payedIssueSum;
    }

    public Integer getRemainIssue() {
        return remainIssue;
    }

    public String getThisIssueRepayDate() {
        return thisIssueRepayDate;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public String getPayAccountNumber() {
        return payAccountNumber;
    }

    public String getPayAccountFlag() {
        return payAccountFlag;
    }

    public String getLoanRepayPeriod() {
        return loanRepayPeriod;
    }

    public String getOverdueIssueSum() {
        return overdueIssueSum;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public void setLoanToDate(String loanToDate) {
        this.loanToDate = loanToDate;
    }

    public void setThisIssueRepayInterest(String thisIssueRepayInterest) {
        this.thisIssueRepayInterest = thisIssueRepayInterest;
    }

    public void setRemainCapital(String remainCapital) {
        this.remainCapital = remainCapital;
    }

    public void setThisIssueRepayAmount(String thisIssueRepayAmount) {
        this.thisIssueRepayAmount = thisIssueRepayAmount;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public void setLoanPeriodUnit(String loanPeriodUnit) {
        this.loanPeriodUnit = loanPeriodUnit;
    }

    public void setOverdueIssue(Integer overdueIssue) {
        this.overdueIssue = overdueIssue;
    }

    public void setPayedIssueSum(Integer payedIssueSum) {
        this.payedIssueSum = payedIssueSum;
    }

    public void setRemainIssue(Integer remainIssue) {
        this.remainIssue = remainIssue;
    }

    public void setThisIssueRepayDate(String thisIssueRepayDate) {
        this.thisIssueRepayDate = thisIssueRepayDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public void setPayAccountNumber(String payAccountNumber) {
        this.payAccountNumber = payAccountNumber;
    }

    public void setPayAccountFlag(String payAccountFlag) {
        this.payAccountFlag = payAccountFlag;
    }

    public void setLoanRepayPeriod(String loanRepayPeriod) {
        this.loanRepayPeriod = loanRepayPeriod;
    }

    public void setOverdueIssueSum(String overdueIssueSum) {
        this.overdueIssueSum = overdueIssueSum;
    }

    @Override
    public String toString() {
        return "PsnDrawingDetailResult{" +
                "loanType='" + loanType + '\'' +
                ", loanAccount='" + loanAccount + '\'' +
                ", currency='" + currency + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                ", loanPeriod='" + loanPeriod + '\'' +
                ", loanToDate='" + loanToDate + '\'' +
                ", remainCapital='" + remainCapital + '\'' +
                ", thisIssueRepayInterest='" + thisIssueRepayInterest + '\'' +
                ", thisIssueRepayAmount='" + thisIssueRepayAmount + '\'' +
                ", interestType='" + interestType + '\'' +
                ", loanPeriodUnit='" + loanPeriodUnit + '\'' +
                ", overdueIssue=" + overdueIssue +
                ", payedIssueSum=" + payedIssueSum +
                ", remainIssue=" + remainIssue +
                ", thisIssueRepayDate='" + thisIssueRepayDate + '\'' +
                ", loanDate='" + loanDate + '\'' +
                ", loanRate='" + loanRate + '\'' +
                ", payAccountNumber='" + payAccountNumber + '\'' +
                ", payAccountFlag='" + payAccountFlag + '\'' +
                ", loanRepayPeriod='" + loanRepayPeriod + '\'' +
                ", overdueIssueSum='" + overdueIssueSum + '\'' +
                '}';
    }
}
