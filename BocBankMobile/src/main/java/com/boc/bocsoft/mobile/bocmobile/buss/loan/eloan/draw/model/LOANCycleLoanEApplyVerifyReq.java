package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xintong on 2016/6/23.
 */
public class LOANCycleLoanEApplyVerifyReq implements Serializable{

    //创建交易会话后的id
    private String conversationId;
    //安全工具组合
    private String _combinId;

    //签约类型(01：WLCF 02：PLCF 03: 账户签约)
    private String quoteType;

    //贷款品种
    private String loanType;

    //额度编号
    private String quoteNo;

    //可用金额
    private BigDecimal loanCycleAvaAmount;

    //币种(CNY)
    private String currencyCode;

    //本次用款金额
    private BigDecimal amount;

    /**
     * 用途
     * 日常消费，教育，装修，医疗，购车，旅游，婚嫁
     * 上送格式为：渠道中文标示+半角分号+前端上送的“贷款用途”+半角分号，
     * 如APP选择“日常消费”提款时上送为“APP提款;日常消费;”
     *
     */
    private String remark;

    //收款账户
    private String loanCycleToActNum;

    //收款账户Id
    private String toAccountId;

    /**
     * 贷款期限
     *  01：1月
     *  02：2月
     *  03：3月
     *  04：4月
     *  05：5月
     *  06：6月
     *  07：7月
     *  08：8月
     *  09：9月
     *  10：10月
     *  11：11月
     *  12：12月
     *
     */
    private String loanPeriod;

    /**
     * 还款方式
     * 1 一次性还本付息
     * 2 按月还息到期一次性还本
     * 3 按月等额本息
     * 4 按月等额本金
     * 1-3个月，上送1：一次性还本付息 ；用款期限4-12个月，上送2：按月付息到期还本
     *
     */
    private String payType;

    //贷款利率(固定值)
    private String loanRate;

    //还款日(1-31)
    private String issueRepayDate;

    //还款账户
    private String payAccount;

    /**
     * 下次还款日标示(0：第一个还款日还款（非整期后）
     * 1：一个整期后还款日还款
     * 3：按标准日结息)
     */
    private String nextRepayDate;


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

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public BigDecimal getLoanCycleAvaAmount() {
        return loanCycleAvaAmount;
    }

    public void setLoanCycleAvaAmount(BigDecimal loanCycleAvaAmount) {
        this.loanCycleAvaAmount = loanCycleAvaAmount;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLoanCycleToActNum() {
        return loanCycleToActNum;
    }

    public void setLoanCycleToActNum(String loanCycleToActNum) {
        this.loanCycleToActNum = loanCycleToActNum;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
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

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public String getIssueRepayDate() {
        return issueRepayDate;
    }

    public void setIssueRepayDate(String issueRepayDate) {
        this.issueRepayDate = issueRepayDate;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(String nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }


    @Override
    public String toString() {
        return "LOANCycleLoanEApplyVerifyReq{" +
                "conversationId='" + conversationId + '\'' +
                ", _combinId='" + _combinId + '\'' +
                ", quoteType='" + quoteType + '\'' +
                ", loanType='" + loanType + '\'' +
                ", quoteNo='" + quoteNo + '\'' +
                ", loanCycleAvaAmount=" + loanCycleAvaAmount +
                ", currencyCode='" + currencyCode + '\'' +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                ", loanCycleToActNum='" + loanCycleToActNum + '\'' +
                ", toAccountId='" + toAccountId + '\'' +
                ", loanPeriod='" + loanPeriod + '\'' +
                ", payType='" + payType + '\'' +
                ", loanRate='" + loanRate + '\'' +
                ", issueRepayDate='" + issueRepayDate + '\'' +
                ", payAccount='" + payAccount + '\'' +
                ", nextRepayDate='" + nextRepayDate + '\'' +
                '}';
    }
}
