package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model;

/**
 * Created by louis.hui on 2016/12/15.
 * 039定赎修改返回页面数据
 */
public class FundScheduledSellModel {

    /**基金交易流水号*/
    private String fundSeq;
    /**交易流水号*/
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
        return "PsnFundScheduledSellModifyResullt{" +
                "fundSeq='" + fundSeq + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", tranState='" + tranState + '\'' +
                '}';
    }
}
