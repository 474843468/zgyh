package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransferSubmit;

import java.math.BigDecimal;

/**
 * Created by WM on 2016/6/24.
 */
public class PsnDirTransCrossBankTransferSubmitResult {


    /**
     * fromAccountNum : 102058194370
     * fromAccountNickname : 活期一本通
     * fromAccountType : 188
     * fromIbkNum : 43016
     * amount : 3456
     * batSeq : 2673879086
     * commissionCharge : 4.8
     * finalCommissionCharge : 4.8
     * currency : 001
     * furInfo : 请问
     * postage : 0.0
     * transactionId : 3867973382
     * status : L
     * workDayFlag : null
     * executeDate : null
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
    private String furInfo;
    private BigDecimal postage;
    private BigDecimal transactionId;
    private String status;
    private String workDayFlag;
    private String executeDate;

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

    public BigDecimal getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigDecimal transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkDayFlag() {
        return workDayFlag;
    }

    public void setWorkDayFlag(String workDayFlag) {
        this.workDayFlag = workDayFlag;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }
}
