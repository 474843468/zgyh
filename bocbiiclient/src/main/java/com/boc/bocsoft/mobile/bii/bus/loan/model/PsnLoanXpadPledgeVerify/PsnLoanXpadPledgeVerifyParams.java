package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PsnLoanXpadPledgeVerifyParams {

    private String conversationId;
    /**
     * 安全工具组合
     */
    private String _combinId;
    /**
     * 贷款品种
     * 固定值为“FIN-LOAN”
     */
    private String loanType;
    /**
     * 本次用款金额
     */
    private String amount;
    /**
     * 币种
     */
    private String currencyCode;

    /**
     * 贷款期限
     */
    private String loanPeriod;
    /**
     * 贷款利率
     */
    private String loanRate;
    /**
     * 还款方式
     * 1：到期一次性还清本息
     * 2：按期还息到期还本
     * 贷款期限6个月（含）以下，还款方式字段显示为“到期一次性还清本息”
     * 贷款期限6个月以上，还款方式显示为“按期还息到期还本”
     */
    private String payType;
    /**
     * 还款周期
     * 1：到期时
     * 2：按月
     * 贷款期限6个月（含）以下，还款周期显示为“到期时”
     * 贷款期限6个月以上，还款周期显示为“按月”
     */
    private String payCycle;
    /**
     * 收款账户
     */
    private String toAcctNum;
    /**
     * 收款账户Id
     */
    private String toAccountId;
    /**
     * 还款账户Id
     */
    private String loanRepayAccountId;

    /**
     * 质押率
     */
    private String pledgeRate;


    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayCycle() {
        return payCycle;
    }

    public void setPayCycle(String payCycle) {
        this.payCycle = payCycle;
    }

    public String getToAcctNum() {
        return toAcctNum;
    }

    public void setToAcctNum(String toAcctNum) {
        this.toAcctNum = toAcctNum;
    }

    public String getPledgeRate() {
        return pledgeRate;
    }

    public void setPledgeRate(String pledgeRate) {
        this.pledgeRate = pledgeRate;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getLoanRepayAccountId() {
        return loanRepayAccountId;
    }

    public void setLoanRepayAccountId(String loanRepayAccountId) {
        this.loanRepayAccountId = loanRepayAccountId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
