package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementModifyResult;

/**
 * 协议修改结果
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadAgreementModifyResultParams {
    private String contractSeq;//	协议序号
    private String totalPeriod;//	购买期数
    private String amountTypeCode;//	基础金额模式
    private String serialName;//	产品系列名称
    private String currencyCode;//	获取币种
    private String xpadCashRemit;//	钞汇标志
    private String addAmount;//	追加金额/返还份额
    private String contAmtMode;//	续约金额模式
    private String baseAmount;//	基础金额
    private String minAmount;//	最低预留金额
    private String maxAmount;//	最大扣款金额
    private String accountKey;//	账号缓存标识
    private String token;//	防重标识

    private String conversationId;//	回话id

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getContractSeq() {
        return contractSeq;
    }

    public void setContractSeq(String contractSeq) {
        this.contractSeq = contractSeq;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getAmountTypeCode() {
        return amountTypeCode;
    }

    public void setAmountTypeCode(String amountTypeCode) {
        this.amountTypeCode = amountTypeCode;
    }

    public String getSerialName() {
        return serialName;
    }

    public void setSerialName(String serialName) {
        this.serialName = serialName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getXpadCashRemit() {
        return xpadCashRemit;
    }

    public void setXpadCashRemit(String xpadCashRemit) {
        this.xpadCashRemit = xpadCashRemit;
    }

    public String getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(String addAmount) {
        this.addAmount = addAmount;
    }

    public String getContAmtMode() {
        return contAmtMode;
    }

    public void setContAmtMode(String contAmtMode) {
        this.contAmtMode = contAmtMode;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
