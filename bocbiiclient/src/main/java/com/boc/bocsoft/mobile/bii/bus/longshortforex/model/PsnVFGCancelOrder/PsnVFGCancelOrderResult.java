package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelOrder;

/**
 * Created by zc7067 on 2016/11/17.
 *
 * @des 双向宝-委托撤单  014
 */
public class PsnVFGCancelOrderResult {
    /**
     *  "transactionId": 691772041,
     *  "exchangeTransDate": "2020/11/01 02:11:22"
     */
    //网银交易序号
    private String transactionId;
    //撤单时间
    private String exchangeTransDate;

    public void setExchangeTransDate(String exchangeTransDate) {
        this.exchangeTransDate = exchangeTransDate;
    }

    public String getExchangeTransDate() {
        return exchangeTransDate;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
