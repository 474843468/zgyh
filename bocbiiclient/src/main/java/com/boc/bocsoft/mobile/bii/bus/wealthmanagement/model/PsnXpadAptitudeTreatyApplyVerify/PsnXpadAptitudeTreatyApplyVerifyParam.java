package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyVerify;

/**
 * 智能协议申请预交易--请求
 * Created by wangtong on 2016/10/28.
 */
public class PsnXpadAptitudeTreatyApplyVerifyParam {
    private String conversationId;// 会话ID
    private String accountId;//	帐号ID
    private String agrCode;// 产品协议编号
    private String amountType;// 投资金额模式
    private String amount;// 单期购买金额/单次购买金额
    private String minAmount;//	账户保留余额
    private String maxAmount;//	最大购买金额
    private String unit;// 赎回份额
    private String isControl;//	是否不限期;
    private String totalPeriod;// 购买期数/赎回期数
    private String charCode;// 钞汇类型
    private String token;//	防重机制，通过PSNGetTokenId接口获取

    public String getAccountId() {
        return accountId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
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
