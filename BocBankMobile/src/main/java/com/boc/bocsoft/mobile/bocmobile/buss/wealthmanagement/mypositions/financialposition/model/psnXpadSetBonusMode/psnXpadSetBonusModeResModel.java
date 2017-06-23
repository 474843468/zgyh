package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadSetBonusMode;

import java.math.BigDecimal;

/**
 * I42-4.14 014修改分红方式交易 PsnXpadSetBonusMode 响应model
 * Created by yx on 2016/11/14.
 */
public class psnXpadSetBonusModeResModel {

    /**
     * prodCode : RJYL-606-LJF082501
     * tranSeq : null
     * trfAmount : null
     * prodName : RJYL-606-LJF082501
     * trfPrice : 0.0
     * paymentDate : 2020/05/13
     * transactionId : 1556133330
     */
    /**
     * 产品代码
     */
    private String prodCode;
    /**
     * 交易流水号
     */
    private String tranSeq;
    /**
     * 交易份额
     */
    private BigDecimal trfAmount;
    /**
     * 产品名称
     */
    private String prodName;
    /**
     * 交易价格
     */
    private BigDecimal trfPrice;
    /**
     * 交易日期
     */
    private String paymentDate;
    /**
     * 网银交易流水号
     */
    private Long transactionId;

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }


    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }


    public String getProdCode() {
        return prodCode;
    }

    public String getTranSeq() {
        return tranSeq;
    }


    public String getProdName() {
        return prodName;
    }


    public String getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(BigDecimal trfAmount) {
        this.trfAmount = trfAmount;
    }

    public BigDecimal getTrfPrice() {
        return trfPrice;
    }

    public void setTrfPrice(BigDecimal trfPrice) {
        this.trfPrice = trfPrice;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}
