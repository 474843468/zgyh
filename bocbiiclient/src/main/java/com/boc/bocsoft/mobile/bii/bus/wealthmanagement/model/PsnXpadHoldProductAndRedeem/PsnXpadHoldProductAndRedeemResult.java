package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem;

import java.math.BigDecimal;

/**
 * I42-4.13 013持有产品赎回  PsnXpadHoldProductAndRedeem   响应Model
 * Created by cff on 2016/9/7.
 */
public class PsnXpadHoldProductAndRedeemResult {
    //币种
    private String currencyCode;
    //网银交易流水号
    private long transactionId;
    //付款日期
    private String paymentDate;
    //报文、文档字段不一致，待确定
    private String tranSeq;
    //交易份额
    private BigDecimal trfAmount;
    //产品代码
    private String prodCode;
    //产品名称
    private String prodName;
    //赎回金额
    private int redeemAmount;
    //交易价格
    private int trfPrice;
    //成交类型   0:正常1:挂单
    private String tranflag;
    //交易日期	String	交易实际将要执行的日期
    private String transDate;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(int redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public int getTrfPrice() {
        return trfPrice;
    }

    public void setTrfPrice(int trfPrice) {
        this.trfPrice = trfPrice;
    }

    public String getTranflag() {
        return tranflag;
    }

    public void setTranflag(String tranflag) {
        this.tranflag = tranflag;
    }
    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public BigDecimal getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(BigDecimal trfAmount) {
        this.trfAmount = trfAmount;
    }
}
