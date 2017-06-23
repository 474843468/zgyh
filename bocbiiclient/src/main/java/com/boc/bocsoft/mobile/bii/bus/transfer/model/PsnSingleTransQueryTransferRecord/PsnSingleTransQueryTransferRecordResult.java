package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnSingleTransQueryTransferRecord;

/**
 * Created by WYme on 2016/9/26.
 */
public class PsnSingleTransQueryTransferRecordResult {

    /**
     * batSeq : 1082465292
     * status : L
     * paymentDate : 2018/08/31
     * payeeAccountNumber : 2232232323322232
     * payeeAccountName : 可乐
     * payerAccountNumber : 102458209689
     * feeCur : 001
     * amount : 121
     * channel : 1
     * transactionId : 1555930386
     * transferType : 16
     * returnCode : null
     * commissionCharge : 0
     * defaultTimeForRealTime : 5
     * waitTimeForRealTime : 90
     */

    private String batSeq;
    private String status;
    private String paymentDate;
    private String payeeAccountNumber;
    private String payeeAccountName;
    private String payerAccountNumber;
    private String feeCur;
    private String amount;
    private String channel;
    private String transactionId;
    private String transferType;
    private String returnCode;
    private String commissionCharge;
    private String defaultTimeForRealTime;
    private String waitTimeForRealTime;

    public String getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(String batSeq) {
        this.batSeq = batSeq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
    }

    public String getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(String payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
    }

    public String getFeeCur() {
        return feeCur;
    }

    public void setFeeCur(String feeCur) {
        this.feeCur = feeCur;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(String commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public String getDefaultTimeForRealTime() {
        return defaultTimeForRealTime;
    }

    public void setDefaultTimeForRealTime(String defaultTimeForRealTime) {
        this.defaultTimeForRealTime = defaultTimeForRealTime;
    }

    public String getWaitTimeForRealTime() {
        return waitTimeForRealTime;
    }

    public void setWaitTimeForRealTime(String waitTimeForRealTime) {
        this.waitTimeForRealTime = waitTimeForRealTime;
    }
}
