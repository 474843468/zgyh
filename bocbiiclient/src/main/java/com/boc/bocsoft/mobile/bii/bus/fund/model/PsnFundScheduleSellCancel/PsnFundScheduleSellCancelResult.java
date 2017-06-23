package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduleSellCancel;

/**
 * Created by huixiaobo on 2016/11/18.
 * 040定期定额赎回撤销—返回参数
 */
public class PsnFundScheduleSellCancelResult {
    /**
     * dsFlag : 0
     * eachAmount : 200.00
     * endFlag : 0
     * feeType : 1
     * fundSeq : 13105
     * subDate : 28
     * tranState : 0
     * transactionId : 10000003105
     */

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
//    /**交易号*/
//    private String transationId;
//    /**交易流水号*/
//    private String fundSeq;
//
//    public String getTransationId() {
//        return transationId;
//    }
//
//    public void setTransationId(String transationId) {
//        this.transationId = transationId;
//    }
//
//    public String getFundSeq() {
//        return fundSeq;
//    }
//
//    public void setFundSeq(String fundSeq) {
//        this.fundSeq = fundSeq;
//    }
//
//    @Override
//    public String toString() {
//        return "PsnFundScheduleSellCancelResult{" +
//                "transationId='" + transationId + '\'' +
//                ", fundSeq='" + fundSeq + '\'' +
//                '}';
//    }


}
