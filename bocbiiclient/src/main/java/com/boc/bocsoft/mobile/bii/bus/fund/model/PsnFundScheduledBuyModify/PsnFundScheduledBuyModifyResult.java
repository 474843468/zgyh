package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyModify;

/**
 * Created by huixiaobo on 2016/11/18.
 * 038基金定期定额申购修改—返回参数
 */
public class PsnFundScheduledBuyModifyResult {
    /**基金交易流水号*/
    private String fundSeq;
    /**网银交易流水号*/
    private String transactionId;
    /**交易状态*/
    private String tranState;

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    @Override
    public String toString() {
        return "PsnFundScheduledBuyModifyResult{" +
                "fundSeq='" + fundSeq + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", tranState='" + tranState + '\'' +
                '}';
    }
}
