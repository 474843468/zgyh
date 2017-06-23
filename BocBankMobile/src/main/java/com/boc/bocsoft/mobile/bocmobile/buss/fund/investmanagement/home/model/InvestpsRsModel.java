package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model;

/**
 * Created by huixiaobo on 2016/12/1.
 *056接口返回参数
 */
public class InvestpsRsModel {

    /**基金交易流水号*/
    private String fundSeq;
    /**网银交易流水号*/
    private String transactionId;

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

    @Override
    public String toString() {
        return "PsnFundScheduledBuyPauseResumeResult{" +
                "fundSeq='" + fundSeq + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
