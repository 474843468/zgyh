package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model;

/**
 * Created by huixiaobo on 2016/11/30.
 * 定投撤单返回 model
 */
public class InvestModel {
    /**网银交易序号*/
    private String transationId;
    /**基金交易流水号*/
    private String fundSeq;

    public String getTransationId() {
        return transationId;
    }

    public void setTransationId(String transationId) {
        this.transationId = transationId;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    @Override
    public String toString() {
        return "PsnFundDdAbortResult{" +
                "transationId='" + transationId + '\'' +
                ", fundSeq='" + fundSeq + '\'' +
                '}';
    }
}
