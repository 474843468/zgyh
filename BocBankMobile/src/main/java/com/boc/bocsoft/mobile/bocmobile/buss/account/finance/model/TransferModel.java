package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model;

import java.io.Serializable;

/**
 * @author wangyang
 *         16/6/27 16:17
 *         充值信息Model
 */
public class TransferModel implements Serializable{

    /**
     * 转出账户
     */
    private String bankAccountId;
    /**
     * 转出账户
     */
    private String bankAccountNumber;
    /**
     * 转入账户
     */
    private String financeICAccountId;
    /**
     * 转入账户
     */
    private String financeICNumber;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 金额
     */
    private String amount;
    /**
     * 是否给自己充值
     */
    private boolean isSelf;

    public String getBankAccountId() {
        return bankAccountId;
    }

    public String getFinanceICNumber() {
        return financeICNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public String getFinanceICAccountId() {
        return financeICAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setFinanceICAccountId(String financeICAccountId) {
        this.financeICAccountId = financeICAccountId;
    }

    public void setFinanceICNumber(String financeICNumber) {
        this.financeICNumber = financeICNumber;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
