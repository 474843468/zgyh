package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplyVerify;

import java.math.BigDecimal;

/**
 * 贷款管理-循环/非循环类贷款（非中银E贷）-用款预交易请求参数
 * Created by liuzc on 2016/8/23.
 */
public class PsnLOANCycleLoanApplyVerifyParams {


    /**
     * conversationId : a6746c8d-2098-44f6-8ef7-86c650e6a532
     * _combinId : 32
     * loanType : 1047
     * loanActNum : 100940097865
     * availableAmount : 499944
     * currencyCode : CNY
     * amount : 1569.00
     * loanPeriod : 6
     * loanRate : 5.600000
     * loanCycleDrawdownDate : 2016/12/21
     * loanCycleMatDate : 2016/12/26
     * toAccountId : 40403028
     * loanCycleToActNum : 4563510100892770235
     * payAccount : 100760095454
     * payType : B
     * payCycle : 98
     */

    private String conversationId; //会话ID
    private String _combinId; //安全工具组合
    private String loanType; //贷款品种
    private String loanActNum; //贷款账号
    private BigDecimal availableAmount; //可用金额
    private String currencyCode; //币种
    private BigDecimal amount; //本次用款金额
    private String loanPeriod; //贷款期限
    private String loanRate; //贷款利率
    private String loanCycleDrawdownDate; //放款截止日
    private String loanCycleMatDate; //贷款到期日
    private String toAccountId; //收款账户Id
    private String loanCycleToActNum; //收款账户
    private String payAccount; //还款账户
    private String payType; //还款方式
    private String payCycle; //还款周期

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

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

    public String getLoanActNum() {
        return loanActNum;
    }

    public void setLoanActNum(String loanActNum) {
        this.loanActNum = loanActNum;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getLoanCycleDrawdownDate() {
        return loanCycleDrawdownDate;
    }

    public void setLoanCycleDrawdownDate(String loanCycleDrawdownDate) {
        this.loanCycleDrawdownDate = loanCycleDrawdownDate;
    }

    public String getLoanCycleMatDate() {
        return loanCycleMatDate;
    }

    public void setLoanCycleMatDate(String loanCycleMatDate) {
        this.loanCycleMatDate = loanCycleMatDate;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getLoanCycleToActNum() {
        return loanCycleToActNum;
    }

    public void setLoanCycleToActNum(String loanCycleToActNum) {
        this.loanCycleToActNum = loanCycleToActNum;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
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
}
