package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductredeemverify;

import java.io.Serializable;
/**
 * I42-4.33 033持有产品赎回预交易  PsnXpadHoldProductRedeemVerify  请求Model
 * Created by cff on 2016/9/7.
 */
public class PsnXpadHoldProductRedeemVerifyReqModel implements Serializable {
    /**
     * 产品代码
     */
    private String prodCode;
    /**
     *  钞汇标识
     * 00:人民币钞汇
     * 01：钞
     * 02：汇
     */
    private String xpadCashRemit;
    /**
     *  赎回份额
     * 全部赎回时上送“0”
     */
    private String redeemQuantity;
    /**
     *  防重标识
     */
    private String token;
    /**
     * 账号缓存标识
     */
    private String accountKey;
    /** 产品性质
     * 0：结构性理财产品（默认）
     * 1：类基金产品
     * 可不上送，不送时默认为“0:结构性理财”
     */
    private String productKind;
    /**
     *  指定赎回日期
     * 默认：
     * 立即赎回，即在第一个赎回开放期首日进行赎回
     * 其他：
     * 指定日期赎回
     */
    private String tranSeq;
    /**
     * 交易流水号
     * 份额明细赎回必输
     * 最长30位字符
     */
    private String redeemDate;
    /**
     *  回话ID
     */
    private String conversationId;

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getXpadCashRemit() {
        return xpadCashRemit;
    }

    public void setXpadCashRemit(String xpadCashRemit) {
        this.xpadCashRemit = xpadCashRemit;
    }

    public String getRedeemQuantity() {
        return redeemQuantity;
    }

    public void setRedeemQuantity(String redeemQuantity) {
        this.redeemQuantity = redeemQuantity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
