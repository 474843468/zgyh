package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.model;

import java.math.BigDecimal;

/**
 * 牌价model
 * Created by gwluo on 2016/12/29.
 */

public class PriceModel {
    private String currency;//	币种名称	String
    private String buyRate;//	银行现汇买入价	BigDecimal
    private String buyNoteRate;//	银行现钞买入价	BigDecimal
    private String sellRate;//	银行现汇卖出价	BigDecimal
    private String sellNoteRate;//	银行现钞卖出价	BigDecimal
    private String updateTime;//	更新时间	String

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(String buyRate) {
        this.buyRate = buyRate;
    }

    public String getBuyNoteRate() {
        return buyNoteRate;
    }

    public void setBuyNoteRate(String buyNoteRate) {
        this.buyNoteRate = buyNoteRate;
    }

    public String getSellRate() {
        return sellRate;
    }

    public void setSellRate(String sellRate) {
        this.sellRate = sellRate;
    }

    public String getSellNoteRate() {
        return sellNoteRate;
    }

    public void setSellNoteRate(String sellNoteRate) {
        this.sellNoteRate = sellNoteRate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
