package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConsignAbort;

/**
 * 021基金撤单-返回参数
 * Created by wy7105 on 2016/11/24.
 */
public class PsnFundConsignAbortResult {
    /**
     * fundSeq : 123456
     * error : null
     * transactionId : 12345678
     */
    private String fundSeq; //基金交易流水号
    private String transactionId; //网银交易序号
    //private String error;

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

//    public void setError(String error) {
//        this.error = error;
//    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFundSeq() {
        return fundSeq;
    }

//    public String getError() {
//        return error;
//    }

    public String getTransactionId() {
        return transactionId;
    }
}
