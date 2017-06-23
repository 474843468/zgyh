package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model;

import java.io.Serializable;

/**
 * PsnLOANChangeLoanERepayAccountVerify(变更中E贷)还款账户预交易，请求参数
 * Created by xintong on 2016/6/24.
 */
public class ChangeAccountVerifyReq implements Serializable{

    //创建交易会话后的id
    private String conversationId;
    //安全工具组合
    private String _combinId;
    //额度编号
    private String quoteNo;
    //原还款账户对应卡号
    private String oldPayCardNum;
    //新还款账户(只支持借记卡)
    private String newPayAccountNum;
    //新还款账户ID
    private String newPayAccountId;
    //贷款品种
    private String loanType;
    //币种(同PsnLOANQuoteDetailEQuery接口中的返回字段)
    private String currency;


    //贷款账号
    private String loanActNum;
    //原还款账户
    private String oldPayAccountNum;
    //贷款币种(同PsnLOANQuoteDetailEQuery接口中的返回字段)
    private String currencyCode;
    //钞汇标识  贷款币种为“人民币”时，前端上送”00”；贷款币种为非“人民币”时，前端上送”02”
    private String cashRemit;

    public String getLoanActNum() {
        return loanActNum;
    }

    public void setLoanActNum(String loanActNum) {
        this.loanActNum = loanActNum;
    }

    public String getOldPayAccountNum() {
        return oldPayAccountNum;
    }

    public void setOldPayAccountNum(String oldPayAccountNum) {
        this.oldPayAccountNum = oldPayAccountNum;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public String getOldPayCardNum() {
        return oldPayCardNum;
    }

    public void setOldPayCardNum(String oldPayCardNum) {
        this.oldPayCardNum = oldPayCardNum;
    }

    public String getNewPayAccountNum() {
        return newPayAccountNum;
    }

    public void setNewPayAccountNum(String newPayAccountNum) {
        this.newPayAccountNum = newPayAccountNum;
    }

    public String getNewPayAccountId() {
        return newPayAccountId;
    }

    public void setNewPayAccountId(String newPayAccountId) {
        this.newPayAccountId = newPayAccountId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "ChangeAccountVerifyReq{" +
                "conversationId='" + conversationId + '\'' +
                ", _combinId='" + _combinId + '\'' +
                ", quoteNo='" + quoteNo + '\'' +
                ", oldPayCardNum='" + oldPayCardNum + '\'' +
                ", newPayAccountNum='" + newPayAccountNum + '\'' +
                ", newPayAccountId='" + newPayAccountId + '\'' +
                ", loanType='" + loanType + '\'' +
                ", currency='" + currency + '\'' +
                ", loanActNum='" + loanActNum + '\'' +
                ", oldPayAccountNum='" + oldPayAccountNum + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", cashRemit='" + cashRemit + '\'' +
                '}';
    }
}
