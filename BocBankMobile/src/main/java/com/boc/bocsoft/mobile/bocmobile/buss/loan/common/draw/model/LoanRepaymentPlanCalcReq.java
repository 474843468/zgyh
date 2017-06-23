package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model;

import java.math.BigDecimal;

/**
 * Created by xintong on 2016/6/23.
 */
public class LoanRepaymentPlanCalcReq {

    //产品大类(填写规则：1160)
    private String actType;
    //产品子类(填写规则：1001)
    private String cat;
    //贷款利率
    private String loanRate;
    //贷款期限
    private String loanPeriod;
    /**
     * 还款方式
     * 1 一次性还本付息  B 98
     * 2 按月还息到期一次性还本B 01
     * 3 按月等额本息  F 01
     * 4 按月等额本金G 01用款期限1-3个月，一次性还本付息 B；用款期限4-12个月，按月付息到期还本 B
     */
    private String payType;
    //还款账户
    private String loanRepayAccount;
    //还款日
    private String issueRepayDate;
    //本次用款金额
    private BigDecimal amount;


    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getLoanRepayAccount() {
        return loanRepayAccount;
    }

    public void setLoanRepayAccount(String loanRepayAccount) {
        this.loanRepayAccount = loanRepayAccount;
    }

    public String getIssueRepayDate() {
        return issueRepayDate;
    }

    public void setIssueRepayDate(String issueRepayDate) {
        this.issueRepayDate = issueRepayDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
