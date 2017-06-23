package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAppointCancel;

/**
 * 036 基金指定日期交易撤单-返回参数
 * Created by wy7105 on 2016/11/24.
 */
public class PsnFundAppointCancelResult {
    /**
     * fundSeq : 4324234328989
     * transactionId : E112121212
     */
    private String fundSeq; //基金交易流水号
    private String transactionId; //网银交易序号

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
