package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model;

import java.math.BigDecimal;

/**
 * Created by xintong on 2016/6/24.
 */
public class PrepayVerifyReq {

    //创建交易会话后的id
    private String conversationId;
    //贷款品种（必输）
    private String loanType;
    //贷款账号（必输）
    private String loanAccount;
    //币种（必输）
    private String currency;
    //提前还款金额（必输）
    private BigDecimal repayAmount;
    //转出账户ID（必输）
    private String fromAccountId;
    //转出账号
    private String accountNumber;
    /**
     * 安全因子（必输）
     * 通过PsnGetSecurityFactor接口查询
     */
    private String _combinId;



    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    @Override
    public String toString() {
        return "PrepayVerifyReq{" +
                "conversationId='" + conversationId + '\'' +
                ", loanType='" + loanType + '\'' +
                ", loanAccount='" + loanAccount + '\'' +
                ", currency='" + currency + '\'' +
                ", repayAmount=" + repayAmount +
                ", fromAccountId='" + fromAccountId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", _combinId='" + _combinId + '\'' +
                '}';
    }
}
