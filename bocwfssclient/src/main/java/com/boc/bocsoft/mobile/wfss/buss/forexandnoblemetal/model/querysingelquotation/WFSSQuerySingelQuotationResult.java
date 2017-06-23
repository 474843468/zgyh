package com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querysingelquotation;

import java.math.BigDecimal;

/**
 * 2.2 外汇、贵金属单笔行情查询
 * Created by gwluo on 2016/10/24.
 */

public class WFSSQuerySingelQuotationResult {
    private String ccygrpNm;//	货币对名称
    private BigDecimal buyRate;//	买入价
    private BigDecimal sellRate;//卖出价
    private String priceTime;//	牌价有效时间		yyyyMMddHHmmss
    private String currPercentDiff;//	今日涨跌幅		小数点后有效位数为3位
    private String currDiff;//	今日涨跌值
    private String tranCode;//	可交易标识		T-可交易  S-停止交易
    private String sortPriority;//	货币对优先级
    private String sourceCurrencyCode;//	首货币代码
    private String targetCurrencyCode;//	尾货币代码
    private String openPrice;//开盘价
    private String maxPrice;//最高价
    private String minPrice;//最低价

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getCcygrpNm() {
        return ccygrpNm;
    }

    public void setCcygrpNm(String ccygrpNm) {
        this.ccygrpNm = ccygrpNm;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public String getPriceTime() {
        return priceTime;
    }

    public void setPriceTime(String priceTime) {
        this.priceTime = priceTime;
    }

    public String getCurrPercentDiff() {
        return currPercentDiff;
    }

    public void setCurrPercentDiff(String currPercentDiff) {
        this.currPercentDiff = currPercentDiff;
    }

    public String getCurrDiff() {
        return currDiff;
    }

    public void setCurrDiff(String currDiff) {
        this.currDiff = currDiff;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(String sortPriority) {
        this.sortPriority = sortPriority;
    }

    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public void setSourceCurrencyCode(String sourceCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }
}
