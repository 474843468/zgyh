package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model;

/**
 * @author wangyang
 *         16/6/27 20:00
 *         交易结果Model
 */
public class TransferResultModel {

    /**
     * 网银交易序号
     */
    private long transactionId;
    /**
     * 转出账号
     */
    private String bankAccountNum;
    /**
     * 转入账号
     */
    private String icCardNum;
    /**
     * 金额
     */
    private double amount;
    /**
     * 状态
     */
    private String status;
    /**
     * 是否给自己充值
     */
    private boolean isSelf;

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId + "";
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getIcCardNum() {
        return icCardNum;
    }

    public void setIcCardNum(String icCardNum) {
        this.icCardNum = icCardNum;
    }

    public String getAmount() {
        return amount + "";
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isSuccess(){
        return "A".equals(status);
    }
}
