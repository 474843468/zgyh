package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking;

import java.math.BigDecimal;

/**
 * Created by WM on 2016/6/16.
 */
public class PsnTransNationalChangeBookingResult {

    /**
     * currency : 001
     * transactionId : 2414182
     * amount : 3355
     * batSeq : 41032689
     * commissionCharge : null
     * furInfo : null
     * postage : null
     * status : A
     */

    private String currency;
    private BigDecimal transactionId;
    private String amount;
    private BigDecimal batSeq;
    private BigDecimal commissionCharge;
    private String furInfo;
    private BigDecimal postage;
    private String status;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigDecimal transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    public String getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
