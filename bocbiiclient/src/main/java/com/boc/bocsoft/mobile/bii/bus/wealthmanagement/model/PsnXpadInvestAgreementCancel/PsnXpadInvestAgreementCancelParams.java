package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementCancel;

/**
 * 投资协议终止
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadInvestAgreementCancelParams {
    private String accountKey;//	账号缓存标识
    private String custAgrCode;//	客户协议编号
    private String agrType;//	协议类型
    private String productName;//	产品名称
    private String totalPeriod;//	购买期数
    private String amountType;//	基础金额模式
    private String baseAmount;//	基础金额
    private String minAmount;//	最低预留金额
    private String maxAmount;//	最大扣款金额
    private String currencyCode;//	币种
    private String cashRemit;//	钞汇标志
    private String token;//	防重机制，通过PSNGetTokenId接口获取
    /** 会话Id */
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCustAgrCode() {
        return custAgrCode;
    }

    public void setCustAgrCode(String custAgrCode) {
        this.custAgrCode = custAgrCode;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }
}
