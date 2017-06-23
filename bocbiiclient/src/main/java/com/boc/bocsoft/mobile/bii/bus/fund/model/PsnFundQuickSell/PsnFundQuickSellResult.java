package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQuickSell;

import java.math.BigDecimal;

/**
 * Created by zcy7065 on 2016/12/13.
 */
public class PsnFundQuickSellResult {

    private String fundCode;//基金代码
    private String fundSeq;//基金交易流水号
    private BigDecimal sellAmount;//赎回份额
    private String fundName;//基金名称
    private String transactionId;//交易流水号

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public void setSellAmount(BigDecimal sellAmount) {
        this.sellAmount = sellAmount;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public BigDecimal getSellAmount() {
        return sellAmount;
    }

    public String getFundName() {
        return fundName;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
