package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.model;

import java.math.BigDecimal;

/**
 * Created by hty7062 on 2016/11/26.
 * 保证金转入、转出
 */
public class XpadVFGBailTransferViewModel {

    /**
     * token : 2oaowiuu
     * fundTransferDir : I
     * cashRemit : 0
     * amount : 100
     * currencyCode : 001
     * transactionId : 691725542
     * stockBalance : 600000000
     */

    private String token;
    private String fundTransferDir;
    private String cashRemit;
    private String amount;
    private String currencyCode;
    private String transactionId;
    private BigDecimal stockBalance;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFundTransferDir() {
        return fundTransferDir;
    }

    public void setFundTransferDir(String fundTransferDir) {
        this.fundTransferDir = fundTransferDir;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getStockBalance() {
        return stockBalance;
    }

    public void setStockBalance(BigDecimal stockBalance) {
        this.stockBalance = stockBalance;
    }

}
