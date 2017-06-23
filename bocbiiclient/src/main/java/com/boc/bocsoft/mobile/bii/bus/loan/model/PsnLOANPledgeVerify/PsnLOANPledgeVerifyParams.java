package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify;

/**
 * 存款质押贷款预交易
 * Created by XieDu on 2016/8/2.
 */
public class PsnLOANPledgeVerifyParams {

    /**
     * 安全工具组合
     */
    private String _combinId;
    /**
     * 贷款品种	String	M	固定值为“PLEA”
     */
    private String loanType;
    /**
     * 可用金额
     */
    private String availableAmount;
    /**
     * 本次用款金额
     */
    private String amount;
    private String currencyCode;
    private String loanPeriod;
    private String loanRate;
    /**
     * 还款方式
     * B：只还利息
     * F：等额本息
     * G：等额本金
     * N：协议还款
     */
    private String payType;
    /**
     * 还款周期
     * 固定值为“01”
     */
    private String payCycle;
    /**
     * 收款账户
     */
    private String toActNum;
    /**
     * 收款账户Id
     */
    private String toAccount;
    /**
     * 还款账户Id
     */
    private String payAccount;
    /**
     * 需与PsnLOANMultipleQuery、PsnLOANPledgeSubmit接口保持一致
     */
    private String conversationId;

    public String get_combinId() { return _combinId;}

    public void set_combinId(String _combinId) { this._combinId = _combinId;}

    public String getLoanType() { return loanType;}

    public void setLoanType(String loanType) { this.loanType = loanType;}

    public String getAvailableAmount() { return availableAmount;}

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getAmount() { return amount;}

    public void setAmount(String amount) { this.amount = amount;}

    public String getCurrencyCode() { return currencyCode;}

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode;}

    public String getLoanPeriod() { return loanPeriod;}

    public void setLoanPeriod(String loanPeriod) { this.loanPeriod = loanPeriod;}

    public String getLoanRate() { return loanRate;}

    public void setLoanRate(String loanRate) { this.loanRate = loanRate;}

    public String getPayType() { return payType;}

    public void setPayType(String payType) { this.payType = payType;}

    public String getPayCycle() { return payCycle;}

    public void setPayCycle(String payCycle) { this.payCycle = payCycle;}

    public String getToActNum() { return toActNum;}

    public void setToActNum(String toActNum) { this.toActNum = toActNum;}

    public String getToAccount() { return toAccount;}

    public void setToAccount(String toAccount) { this.toAccount = toAccount;}

    public String getPayAccount() { return payAccount;}

    public void setPayAccount(String payAccount) { this.payAccount = payAccount;}

    public String getConversationId() { return conversationId;}

    public void setConversationId(String conversationId) { this.conversationId = conversationId;}
}
