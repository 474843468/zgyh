package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyResult;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * Created by wangtong on 2016/10/10.
 */
public class PsnXpadProductBuyResultParam extends PublicParams{

    private String productCode;
    private String buyPrice;
    private String xpadCashRemit;
    private String productName;
    private String amount;
    private String isAutoser;
    private String currency;
    private String dealCode;
    private String accountId;
    private String productKind;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getXpadCashRemit() {
        return xpadCashRemit;
    }

    public void setXpadCashRemit(String xpadCashRemit) {
        this.xpadCashRemit = xpadCashRemit;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIsAutoser() {
        return isAutoser;
    }

    public void setIsAutoser(String isAutoser) {
        this.isAutoser = isAutoser;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

}
