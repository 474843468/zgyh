package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDdAbort;

/**
 * Created by huixiaobo on 2016/11/18.
 * 014定期定额申购撤销—返回参数
 */
public class PsnFundDdAbortResult {
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
