package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit;

import java.math.BigDecimal;

/**
 * Created by WM on 2016/6/12.
 */

public  class PsnTransBocTransferSubmitResult {

    /**
     * fromAccountNum : 102058194370
     * fromAccountNickname : 活期一本通
     * fromAccountType : 188
     * fromIbkNum : 43016
     * amount : 14
     * batSeq : 2673779828
     * commissionCharge : 0
     * finalCommissionCharge : 0.0
     * currency : 001
     * status : A
     * needCount : null
     * postage : 0.0
     * transactionId : 3867927407
     */

    private String fromAccountNum;
    private String fromAccountNickname;
    private String fromAccountType;
    private String fromIbkNum;
    private BigDecimal amount;
    private BigDecimal batSeq;
    private BigDecimal commissionCharge;
    private BigDecimal finalCommissionCharge;
    private String currency;
    private String status;
    private BigDecimal needCount;
    private BigDecimal postage;
    private BigDecimal transactionId;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(BigDecimal batSeq) {
        this.batSeq = batSeq;
    }

    public BigDecimal getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(BigDecimal commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public BigDecimal getFinalCommissionCharge() {
        return finalCommissionCharge;
    }

    public void setFinalCommissionCharge(BigDecimal finalCommissionCharge) {
        this.finalCommissionCharge = finalCommissionCharge;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getNeedCount() {
        return needCount;
    }

    public void setNeedCount(BigDecimal needCount) {
        this.needCount = needCount;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public BigDecimal getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigDecimal transactionId) {
        this.transactionId = transactionId;
    }
}
