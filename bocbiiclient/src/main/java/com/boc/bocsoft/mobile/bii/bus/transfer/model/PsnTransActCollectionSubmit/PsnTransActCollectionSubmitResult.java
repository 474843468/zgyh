package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionSubmit;

import java.math.BigDecimal;

/**
 * 主动收款提交
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActCollectionSubmitResult {
    /**
     * 币种
     */
    private String currency;
    /**
     * 收款账户类型
     */
    private String accountType;
    /**
     * 收款账户联行号
     */
    private String accountIbkNum;
    /**
     * 转账金额
     */
    private BigDecimal amount;
    /**
     * 收款账户号
     */
    private String accountNum;
    /**
     * 指令序号
     */
    private long notifyId;
    /**
     * 付款人客户号
     */
    private int payerCustId;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(long notifyId) {
        this.notifyId = notifyId;
    }

    public int getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(int payerCustId) {
        this.payerCustId = payerCustId;
    }
}
