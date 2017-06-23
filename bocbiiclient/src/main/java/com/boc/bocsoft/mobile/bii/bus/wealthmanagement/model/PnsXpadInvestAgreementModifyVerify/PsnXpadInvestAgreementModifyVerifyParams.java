package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PnsXpadInvestAgreementModifyVerify;

/**
 * 智能投资协议修改预交易
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadInvestAgreementModifyVerifyParams {

    /**
     * agrCode : 500000025
     * amount : 1000.2
     * amountType : 0
     * charCode : 0
     * conversationId : d22b34a3-58cd-427b-a02c-aa5892e0dbd3
     * custAgrCode : 4000000001
     * isControl : 1
     * maxAmount : 5000000
     * minAmount : 100000.23
     * token : ccarucjw
     * totalPeriod : -1
     */
    private String accountKey;//	账号缓存标识
    private String agrCode;//	产品协议编号
    private String custAgrCode;//	客户协议编号
    private String amountType;//	投资金额模式
    private String amount;//	单期购买金额/单次购买金额
    private String minAmount;//	账户保留余额
    private String maxAmount;//	最大购买金额
    private String unit;//	赎回份额
    private String isControl;//	是否不限期
    private String totalPeriod;//	购买期数/赎回期数
    private String charCode;//	钞汇类型
    private String token;//	防重机制，
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

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getCustAgrCode() {
        return custAgrCode;
    }

    public void setCustAgrCode(String custAgrCode) {
        this.custAgrCode = custAgrCode;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIsControl() {
        return isControl;
    }

    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
