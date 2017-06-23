package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyPre;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * Created by wangtong on 2016/10/10.
 */
public class PsnXpadProductBuyPreParam extends PublicParams{

    private String productName;
    private String curCode;
    private String productCode;
    private String xpadCashRemit;
    private String martCode;
    private String buyPrice;
    private String isAutoser;
    private String accountId;
    private String productKind;
    private String redDate;
    private String investCycle;

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

    public String getRedDate() {
        return redDate;
    }

    public void setRedDate(String redDate) {
        this.redDate = redDate;
    }

    public String getInvestCycle() {
        return investCycle;
    }

    public void setInvestCycle(String investCycle) {
        this.investCycle = investCycle;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getXpadCashRemit() {
        return xpadCashRemit;
    }

    public void setXpadCashRemit(String xpadCashRemit) {
        this.xpadCashRemit = xpadCashRemit;
    }

    public String getMartCode() {
        return martCode;
    }

    public void setMartCode(String martCode) {
        this.martCode = martCode;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getIsAutoser() {
        return isAutoser;
    }

    public void setIsAutoser(String isAutoser) {
        this.isAutoser = isAutoser;
    }
}
