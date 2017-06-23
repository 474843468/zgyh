package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransfer;

import java.math.BigDecimal;

/**
 * Created by WM on 2016/6/24.
 */
public class PsnEbpsRealTimePaymentTransferResult {

    /**
     * batSequence : 2673779698
     * finalCommissionCharge : 1.92
     * transactionId : 3867927002
     */

    private BigDecimal batSequence;
    private BigDecimal finalCommissionCharge;
    private BigDecimal transactionId;

    public BigDecimal getBatSequence() {
        return batSequence;
    }

    public void setBatSequence(BigDecimal batSequence) {
        this.batSequence = batSequence;
    }

    public BigDecimal getFinalCommissionCharge() {
        return finalCommissionCharge;
    }

    public void setFinalCommissionCharge(BigDecimal finalCommissionCharge) {
        this.finalCommissionCharge = finalCommissionCharge;
    }

    public BigDecimal getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigDecimal transactionId) {
        this.transactionId = transactionId;
    }
}
