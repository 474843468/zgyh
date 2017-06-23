package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductredeemverify;

import java.io.Serializable;

/**
 * I42-4.33 033持有产品赎回预交易PsnXpadHoldProductRedeemVerify {响应model}
 *
 * @author yx
 * @date 2016-9-9 09:18:54
 */
public class PsnXpadHoldProductRedeemVerifyResModel implements Serializable {
    /**
     * 产品代码
     */
    private String prodCode;
    /**
     * 产品名称
     */
    private String prodName;
    /**
     * 币种
     */
    private String currencyCode;
    /**
     * 交易日期
     */
    private String paymentDate;
    /**
     * 赎回价格
     */
    private String sellPrice;
    /**
     * 赎回份额
     */
    private String redeemQuantity;
    /**
     * 赎回金额
     */
    private String redeemAmount;
    /**
     * 成交类型
     * 0:正常1:挂单
     */
    private String tranflag;
    /**
     * 交易流水号
     */
    private String tranSeq;
    /**
     * 指定赎回日期/确认赎回日期
     * 立即赎回时表示根据用户选择而确定的实际赎回日期；
     * 指定日期赎回时表示用户选择的日期
     */
    private String redeemDate;
    /**
     * 00:人民币钞汇
     * 01：钞
     * 02：汇
     */
    private String cashRemit;

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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getRedeemQuantity() {
        return redeemQuantity;
    }

    public void setRedeemQuantity(String redeemQuantity) {
        this.redeemQuantity = redeemQuantity;
    }

    public String getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(String redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public String getTranflag() {
        return tranflag;
    }

    public void setTranflag(String tranflag) {
        this.tranflag = tranflag;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public String getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        this.redeemDate = redeemDate;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }
}
