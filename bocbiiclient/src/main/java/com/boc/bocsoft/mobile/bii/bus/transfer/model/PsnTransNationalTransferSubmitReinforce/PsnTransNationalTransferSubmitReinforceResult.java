package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmitReinforce;

/**
 * Created by WM on 2016/6/16.
 */
public class PsnTransNationalTransferSubmitReinforceResult {

    /**
     * currency : 001
     * transactionId : 2414182
     * amount : 3355
     * batSeq : 41032689
     * commissionCharge : null
     * furInfo : null
     * postage : null
     * fromAccountNum : 100023440239
     * fromAccountNickname : 活一本
     * fromAccountType : 188
     * fromIbkNum : 40740
     * status : A
     */

    private String currency;
    private int transactionId;
    private int amount;
    private int batSeq;
    private Object commissionCharge;
    private Object furInfo;
    private Object postage;
    private String fromAccountNum;
    private String fromAccountNickname;
    private String fromAccountType;
    private String fromIbkNum;
    private String status;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(int batSeq) {
        this.batSeq = batSeq;
    }

    public Object getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(Object commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public Object getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(Object furInfo) {
        this.furInfo = furInfo;
    }

    public Object getPostage() {
        return postage;
    }

    public void setPostage(Object postage) {
        this.postage = postage;
    }

    public String getFromAccountNum() {
        return fromAccountNum;
    }

    public void setFromAccountNum(String fromAccountNum) {
        this.fromAccountNum = fromAccountNum;
    }

    public String getFromAccountNickname() {
        return fromAccountNickname;
    }

    public void setFromAccountNickname(String fromAccountNickname) {
        this.fromAccountNickname = fromAccountNickname;
    }

    public String getFromAccountType() {
        return fromAccountType;
    }

    public void setFromAccountType(String fromAccountType) {
        this.fromAccountType = fromAccountType;
    }

    public String getFromIbkNum() {
        return fromIbkNum;
    }

    public void setFromIbkNum(String fromIbkNum) {
        this.fromIbkNum = fromIbkNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
