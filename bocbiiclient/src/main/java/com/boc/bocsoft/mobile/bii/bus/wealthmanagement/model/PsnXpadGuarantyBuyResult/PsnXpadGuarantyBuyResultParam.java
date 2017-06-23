package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadGuarantyBuyResult;

import java.util.List;

/**
 * Created by wangtong on 2016/9/22.
 */
public class PsnXpadGuarantyBuyResultParam {

    private String conversationId;
    private String productCode;
    private String productNum;
    private String productName;
    private String buyPrice;
    private String GuarantyBuyAmount;
    private String productCurrency;
    private String xpadCashRemit;
    private String productBeginDate;
    private String productEndDate;
    private String token;
    private String overFlag;
    private String buyAmount;
    private String accountId;
    private List<String> prodCode;
    private List<String> freezeUnit;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getGuarantyBuyAmount() {
        return GuarantyBuyAmount;
    }

    public void setGuarantyBuyAmount(String guarantyBuyAmount) {
        GuarantyBuyAmount = guarantyBuyAmount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getProductCurrency() {
        return productCurrency;
    }

    public void setProductCurrency(String productCurrency) {
        this.productCurrency = productCurrency;
    }

    public String getXpadCashRemit() {
        return xpadCashRemit;
    }

    public void setXpadCashRemit(String xpadCashRemit) {
        this.xpadCashRemit = xpadCashRemit;
    }

    public String getProductBeginDate() {
        return productBeginDate;
    }

    public void setProductBeginDate(String productBeginDate) {
        this.productBeginDate = productBeginDate;
    }

    public String getProductEndDate() {
        return productEndDate;
    }

    public void setProductEndDate(String productEndDate) {
        this.productEndDate = productEndDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOverFlag() {
        return overFlag;
    }

    public void setOverFlag(String overFlag) {
        this.overFlag = overFlag;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    public List<String> getProdCode() {
        return prodCode;
    }

    public void setProdCode(List<String> prodCode) {
        this.prodCode = prodCode;
    }

    public List<String> getFreezeUnit() {
        return freezeUnit;
    }

    public void setFreezeUnit(List<String> freezeUnit) {
        this.freezeUnit = freezeUnit;
    }
}
