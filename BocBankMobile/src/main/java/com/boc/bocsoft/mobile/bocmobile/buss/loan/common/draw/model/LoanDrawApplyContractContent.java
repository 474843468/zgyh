package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model;

/**
 * Created by lzc4524 on 2016/10/15.
 */
public class LoanDrawApplyContractContent {
    private String lender; //贷款人
    private String borrower; //借款人
    private String cdtype; //身份证件类型
    private String cdcard; //身份证件号码
    private String loanaccount; //网上银行专属贷款账号
    private String loancurrency; //借款币种
    private String loanamount; //借款金额
    private String loanperiod; //借款期限
    private String reciver; //贷款发放账户户名
    private String reciveraccount; //贷款发放账户账号
    private String reciveperiod; //借款人定期报告周期
    private String repayment; //还款账户户名
    private String repaymentaccount; //还款账户账号

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getCDType() {
        return cdtype;
    }

    public void setCDType(String CDType) {
        this.cdtype = CDType;
    }

    public String getCDCard() {
        return cdcard;
    }

    public void setCDCard(String CDCard) {
        this.cdcard = CDCard;
    }

    public String getLoanAccount() {
        return loanaccount;
    }

    public void setLoanAccount(String loanAccount) {
        loanaccount = loanAccount;
    }

    public String getLoanCurrency() {
        return loancurrency;
    }

    public void setLoanCurrency(String loanCurrency) {
        loancurrency = loanCurrency;
    }

    public String getLoanAmount() {
        return loanamount;
    }

    public void setLoanAmount(String loanAmount) {
        loanamount = loanAmount;
    }

    public String getLoanPeriod() {
        return loanperiod;
    }

    public void setLoanPeriod(String loanPeriod) {
        loanperiod = loanPeriod;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getReciverAccount() {
        return reciveraccount;
    }

    public void setReciverAccount(String reciverAccount) {
        reciveraccount = reciverAccount;
    }

    public String getRecivePeriod() {
        return reciveperiod;
    }

    public void setRecivePeriod(String recivePeriod) {
        reciveperiod = recivePeriod;
    }

    public String getRepayment() {
        return repayment;
    }

    public void setRepayment(String repayment) {
        this.repayment = repayment;
    }

    public String getRepaymentAccount() {
        return repaymentaccount;
    }

    public void setRepaymentAccount(String repaymentAccount) {
        repaymentaccount = repaymentAccount;
    }
}
