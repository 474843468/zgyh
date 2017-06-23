package com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevanceRes;

/**
 * @author wangyang
 *         16/6/17 14:49
 *         账户充值-未关联进网银-提交交易
 */
public class PsnICTransferNoRelevanceResResult {

    /** 交易状态 */
    private String status;
    /** 币种 */
    private String currency;
    /** 网银交易序号 */
    private long transactionId;
    /** 转账金额 */
    private double amount;
    /** 转账批次号 */
    private int batSeq;
    /** 手续费 */
    private double commissionCharge;
    /** 电汇费 */
    private double postage;
    /** 银行账户别名 */
    private String bankAccountNickname;
    /** 银行账户账号 */
    private String bankAccountNum;
    /** 银行账户类型 */
    private String bankAccountType;
    /** IC卡账户别名 */
    private String icCardNickname;
    /** IC卡账户账号 */
    private String icCardNum;
    /** IC卡账户类型 */
    private String icCardType;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(int batSeq) {
        this.batSeq = batSeq;
    }

    public double getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(double commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public String getBankAccountNickname() {
        return bankAccountNickname;
    }

    public void setBankAccountNickname(String bankAccountNickname) {
        this.bankAccountNickname = bankAccountNickname;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getIcCardNickname() {
        return icCardNickname;
    }

    public void setIcCardNickname(String icCardNickname) {
        this.icCardNickname = icCardNickname;
    }

    public String getIcCardNum() {
        return icCardNum;
    }

    public void setIcCardNum(String icCardNum) {
        this.icCardNum = icCardNum;
    }

    public String getIcCardType() {
        return icCardType;
    }

    public void setIcCardType(String icCardType) {
        this.icCardType = icCardType;
    }
}
