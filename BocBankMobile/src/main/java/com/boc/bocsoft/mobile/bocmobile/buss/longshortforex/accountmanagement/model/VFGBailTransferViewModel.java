package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import java.math.BigDecimal;

/**
 * ViewModel：保证金存入/转出
 * Created by zhx on 2016/12/5
 */
public class VFGBailTransferViewModel {
    // 币种(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String currencyCode;
    // 钞汇(1:钞；2:汇；0:人民币)
    private String cashRemit;
    // 操作方式(I:银行资金账户转证券保证金账户,O:证券保证金账户转银行资金账户)
    private String fundTransferDir;
    // 金额
    private String amount;
    // 防重标志
    private String token;

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFundTransferDir(String fundTransferDir) {
        this.fundTransferDir = fundTransferDir;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAmount() {
        return amount;
    }

    public String getFundTransferDir() {
        return fundTransferDir;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getToken() {
        return token;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
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
