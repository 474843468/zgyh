package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model;

import java.io.Serializable;

/**
 * PsnLOANChangeLoanRepayAccountVerify变更还款账户预交易，请求参数
 * Created by liuzc on 2016/8/25.
 */
public class ChangeAccountVerifyReq implements Serializable{

    private String conversationId; //会话ID
    private String loanType; //贷款品种
    private String loanActNum; //贷款账号
    private String oldPayAccountNum; //原还款账户
    private String oldPayCardNum; //原还款账户对应卡号
    private String newPayAccountNum; //新还款账户
    private String newPayAccountId; //新还款账户ID
    private String _combinId; //安全工具组合
    private String currencyCode; //贷款币种
    //钞汇标识,贷款币种为“人民币”时，前端上送”00”；贷款币种为非“人民币”时，前端上送”02”
    private String cashRemit;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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

    public String getOldPayAccountNum() {
        return oldPayAccountNum;
    }

    public void setOldPayAccountNum(String oldPayAccountNum) {
        this.oldPayAccountNum = oldPayAccountNum;
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

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
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
    @Override
    public String toString() {
        return "ChangeAccountVerifyReq{" +
                "conversationId='" + conversationId + '\'' +
                ", _combinId='" + _combinId + '\'' +
                ", oldPayCardNum='" + oldPayCardNum + '\'' +
                ", newPayAccountNum='" + newPayAccountNum + '\'' +
                ", newPayAccountId='" + newPayAccountId + '\'' +
                ", loanType='" + loanType + '\'' +
                ", loanActNum='" + loanActNum + '\'' +
                ", oldPayAccountNum='" + oldPayAccountNum + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", cashRemit='" + cashRemit + '\'' +
                '}';
    }
}
