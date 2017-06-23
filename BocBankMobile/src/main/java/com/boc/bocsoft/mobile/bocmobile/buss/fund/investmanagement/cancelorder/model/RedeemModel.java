package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model;

/**
 * Created by huixiaobo on 2016/11/30.
 * 定赎返回参数model
 */
public class RedeemModel {

    private String dsFlag;
    private String eachAmount;
    private String endFlag;
    private String feeType;
    private int fundSeq;
    private String subDate;
    private String tranState;
    private long transactionId;

    public String getDsFlag() {
        return dsFlag;
    }

    public void setDsFlag(String dsFlag) {
        this.dsFlag = dsFlag;
    }

    public String getEachAmount() {
        return eachAmount;
    }

    public void setEachAmount(String eachAmount) {
        this.eachAmount = eachAmount;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(int fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getSubDate() {
        return subDate;
    }

    public void setSubDate(String subDate) {
        this.subDate = subDate;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "PsnFundScheduleSellCancelResult{" +
                "dsFlag='" + dsFlag + '\'' +
                ", eachAmount='" + eachAmount + '\'' +
                ", endFlag='" + endFlag + '\'' +
                ", feeType='" + feeType + '\'' +
                ", fundSeq=" + fundSeq +
                ", subDate='" + subDate + '\'' +
                ", tranState='" + tranState + '\'' +
                ", transactionId=" + transactionId +
                '}';
    }
}
