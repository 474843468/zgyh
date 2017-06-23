package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;

import java.io.Serializable;

/**
 * Created by huixiaobo on 2016/6/22.
 * 中银E贷查询签约额度列表ui
 */
public class LoanQuoteViewModel extends EloanQuoteViewModel implements Serializable{

    /**贷款利率*/
    private String rate;
    /**还款账户*/
    private String repayAcct;
    /**贷款品种*/
    private String loanType;
    /**额度号码*/
    private String quoteNo;
    /**额度状态 05:正常 10：取消 40：关闭 20：冻结*/
    private String quoteState;
    /**还款日*/
    private String issueRepayDate;
    /**额度到期日*/
    private String loanToDate;
    /**签约类型  01：WLCF 02：PLCF 03: 账户签约*/
    private String quoteType;
    /**签约状态  0：有效 9：无效*/
    private String registerStates;
    /**币种*/
    private String currency;
    /**额度金额*/
    private String loanBanlance;
    /**可用金额*/
    private String availableAvl;
    /**已用金额*/
    private String useAvl;
    /**下次还款日标示  0：第一个还款日还款（非整期后）
     1：一个整期后还款日还款
     3：按标准日结息
     */
    private String nextRepayDate;

    public String getRate() {
        return rate;
    }

    public String getRepayAcct() {
        return repayAcct;
    }

    public String getLoanType() {
        return loanType;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public String getQuoteState() {
        return quoteState;
    }

    public String getIssueRepayDate() {
        return issueRepayDate;
    }

    public String getLoanToDate() {
        return loanToDate;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public String getRegisterStates() {
        return registerStates;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLoanBanlance() {
        return loanBanlance;
    }

    public String getAvailableAvl() {
        return availableAvl;
    }

    public String getUseAvl() {
        return useAvl;
    }

    public String getNextRepayDate() {
        return nextRepayDate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setRepayAcct(String repayAcct) {
        this.repayAcct = repayAcct;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public void setQuoteState(String quoteState) {
        this.quoteState = quoteState;
    }

    public void setIssueRepayDate(String issueRepayDate) {
        this.issueRepayDate = issueRepayDate;
    }

    public void setLoanToDate(String loanToDate) {
        this.loanToDate = loanToDate;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public void setRegisterStates(String registerStates) {
        this.registerStates = registerStates;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setLoanBanlance(String loanBanlance) {
        this.loanBanlance = loanBanlance;
    }

    public void setAvailableAvl(String availableAvl) {
        this.availableAvl = availableAvl;
    }

    public void setUseAvl(String useAvl) {
        this.useAvl = useAvl;
    }

    public void setNextRepayDate(String nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    @Override
    public String toString() {
        return "EquoteDetailBean{" +
                "rate='" + rate + '\'' +
                ", repayAcct='" + repayAcct + '\'' +
                ", loanType='" + loanType + '\'' +
                ", quoteNo='" + quoteNo + '\'' +
                ", quoteState='" + quoteState + '\'' +
                ", issueRepayDate='" + issueRepayDate + '\'' +
                ", loanToDate='" + loanToDate + '\'' +
                ", quoteType='" + quoteType + '\'' +
                ", registerStates='" + registerStates + '\'' +
                ", currency='" + currency + '\'' +
                ", loanBanlance='" + loanBanlance + '\'' +
                ", availableAvl='" + availableAvl + '\'' +
                ", useAvl='" + useAvl + '\'' +
                ", nextRepayDate='" + nextRepayDate + '\'' +
                '}';
    }

}
