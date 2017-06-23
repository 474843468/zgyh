package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model;

/**
 * Created by huixiaobo on 2016/12/15.
 * 038定投修改确认返回参数
 */
public class FundScheduelBuyModel {
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
