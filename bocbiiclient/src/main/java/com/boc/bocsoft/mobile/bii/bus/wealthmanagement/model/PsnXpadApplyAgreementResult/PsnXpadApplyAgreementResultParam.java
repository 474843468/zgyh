package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadApplyAgreementResult;

/**
 * Created by wangtong on 2016/10/27.
 */
public class PsnXpadApplyAgreementResultParam {

    //产品代码
    private String productCode;
    //投资方式
    private String investType;
    //钞汇标识
    private String xpadCashRemit;
    //总期数
    private String totalPeriod;
    //投资时间
    private String investTime;
    //产品币种
    private String curCode;
    //产品代码
    private String prodName;
    //定投类型
    private String timeInvestType;
    //定投金额
    private String redeemAmount;
    //定投频率
    private String timeInvestRate;
    //定投频率类型 定投频率类型须输入格式为字母,如'w'表示周, d:天, w:周, m:月, y:年；
    private String timeInvestRateFlag;
    //最低限额
    private String minAmount;
    //最高限额
    private String maxAmount;
    //账户标识;
    private String accountId;
    //防重标识
    private String token;
    //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType;
    }

    public String getXpadCashRemit() {
        return xpadCashRemit;
    }

    public void setXpadCashRemit(String xpadCashRemit) {
        this.xpadCashRemit = xpadCashRemit;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getTimeInvestType() {
        return timeInvestType;
    }

    public void setTimeInvestType(String timeInvestType) {
        this.timeInvestType = timeInvestType;
    }

    public String getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(String redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public String getTimeInvestRate() {
        return timeInvestRate;
    }

    public void setTimeInvestRate(String timeInvestRate) {
        this.timeInvestRate = timeInvestRate;
    }

    public String getTimeInvestRateFlag() {
        return timeInvestRateFlag;
    }

    public void setTimeInvestRateFlag(String timeInvestRateFlag) {
        this.timeInvestRateFlag = timeInvestRateFlag;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
