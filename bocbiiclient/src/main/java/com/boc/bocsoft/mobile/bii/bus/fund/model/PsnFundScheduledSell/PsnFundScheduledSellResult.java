package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSell;

/**
 * Created by taoyongzhen on 2016/12/19.
 */

public class PsnFundScheduledSellResult {


    /**
     * transactionId : 12
     * fundSeq : 200020002000
     * dtFlag : 0
     * subDate : 28
     * endFlag : 0
     * tranState : 1
     */

    private String transactionId;
    private String fundSeq;
    private String dtFlag;
    private String subDate;
    private String endFlag;
    private String tranState;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getDtFlag() {
        return dtFlag;
    }

    public void setDtFlag(String dtFlag) {
        this.dtFlag = dtFlag;
    }

    public String getSubDate() {
        return subDate;
    }

    public void setSubDate(String subDate) {
        this.subDate = subDate;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }
}
