package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellPauseResume;

/**
 * Created by huixiaobo on 2016/11/18.
 * 057 定期定额赎回暂停/开通—返回参数
 */
public class PsnFundScheduledSellPauseResumeResult {
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
        return "PsnFundScheduledSellPauseResumeResult{" +
                "fundSeq='" + fundSeq + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
