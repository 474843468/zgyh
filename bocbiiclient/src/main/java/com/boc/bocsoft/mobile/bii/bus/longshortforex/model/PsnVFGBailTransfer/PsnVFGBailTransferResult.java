package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailTransfer;

import java.math.BigDecimal;

/**
 * Params：保证金存入/转出
 * Created by zhx on 2016/12/5
 */
public class PsnVFGBailTransferResult {

    /**
     * stockBalance : 600000000
     * transactionId : 691725542
     */
    // 网银交易序号
    private String transactionId;
    // 保证金余额
    private BigDecimal stockBalance;

    public void setStockBalance(BigDecimal stockBalance) {
        this.stockBalance = stockBalance;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getStockBalance() {
        return stockBalance;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
