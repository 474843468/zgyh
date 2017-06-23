package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan;

import java.math.BigDecimal;

/**
 * 贷款管理-中银E贷-提款-还款计划试算请求参数
 * Created by xintong on 2016/6/16.
 */
public class PsnLoanRepaymentPlanParams {

    //产品大类(填写规则：1160)
    private String productBigType;
    //产品子类(填写规则：1001)
    private String productCatType;
    //贷款利率
    private String loanRate;
    //贷款期限
    private String loanTerm;
    /**
     * 还款方式
     * 1 一次性还本付息  B 98
     * 2 按月还息到期一次性还本B 01
     * 3 按月等额本息  F 01
     * 4 按月等额本金G 01用款期限1-3个月，一次性还本付息 B；用款期限4-12个月，按月付息到期还本 B
     */
    private String repayFlag;
    //还款账户
    private String loanRepayAccount;
    //还款日
    private String issueRepayDate;
    //本次用款金额
    private BigDecimal loanAmount;

    //下次还款日标示, 0：第一个还款日还款（非整期后） 1：一个整期后还款日还款  3：按标准日结息
    private String nextRepayDate;


    public String getProductBigType() {
        return productBigType;
    }

    public void setProductBigType(String productBigType) {
        this.productBigType = productBigType;
    }

    public String getProductCatType() {
        return productCatType;
    }

    public void setProductCatType(String productCatType) {
        this.productCatType = productCatType;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public String getLoanPeriod() {
        return loanTerm;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanTerm = loanPeriod;
    }

    public String getRepayFlag() {
        return repayFlag;
    }

    public void setRepayFlag(String repayFlag) {
        this.repayFlag = repayFlag;
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
        return loanAmount;
    }

    public void setAmount(BigDecimal amount) {
        this.loanAmount = amount;
    }

    public String getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(String nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }
}
