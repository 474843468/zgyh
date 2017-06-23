package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepaySubmit;

import java.math.BigDecimal;

/**
 * PsnLOANAdvanceRepaySubmit提前还款提交交易,返回参数
 * Created by xintong on 2016/6/23.
 */
public class PsnLOANAdvanceRepaySubmitResult {

    //网银交易序号
    private String transNo;
    //贷款品种
    private String loanType;
    //贷款账号
    private String loanAccount;
    //币种
    private String currency;
    //贷款金额
    private BigDecimal loanAmount;
    //期限
    private String loanPeriod;
    //到期日
    private String loanToDate;
    //提前还款金额
    private BigDecimal repayAmount;
    //转出账户
    private String fromAccount;
    //贷款期限单位
    private String loanPeriodUnit;
    //手续费
    private BigDecimal charges;
    //提前还款后每期应还金额
    private BigDecimal afterRepayissueAmount;
    //提前还款后剩余还款额
    private BigDecimal afterRepayRemainAmount;


    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getLoanToDate() {
        return loanToDate;
    }

    public void setLoanToDate(String loanToDate) {
        this.loanToDate = loanToDate;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getLoanPeriodUnit() {
        return loanPeriodUnit;
    }

    public void setLoanPeriodUnit(String loanPeriodUnit) {
        this.loanPeriodUnit = loanPeriodUnit;
    }

    public BigDecimal getCharges() {
        return charges;
    }

    public void setCharges(BigDecimal charges) {
        this.charges = charges;
    }

    public BigDecimal getAfterRepayissueAmount() {
        return afterRepayissueAmount;
    }

    public void setAfterRepayissueAmount(BigDecimal afterRepayissueAmount) {
        this.afterRepayissueAmount = afterRepayissueAmount;
    }

    public BigDecimal getAfterRepayRemainAmount() {
        return afterRepayRemainAmount;
    }

    public void setAfterRepayRemainAmount(BigDecimal afterRepayRemainAmount) {
        this.afterRepayRemainAmount = afterRepayRemainAmount;
    }
}
