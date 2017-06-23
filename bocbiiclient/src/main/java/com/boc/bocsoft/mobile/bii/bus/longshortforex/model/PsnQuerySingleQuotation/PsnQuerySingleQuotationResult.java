package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnQuerySingleQuotation;

import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/12/19 13:46
 *         外汇、贵金属单笔行情查询Result
 */
public class PsnQuerySingleQuotationResult {

    /** 货币对名称 */
    private String ccygrpNm;
    /** 买入价 */
    private BigDecimal buyRate;
    /** 卖出价 */
    private BigDecimal sellRate;
    /** 牌价有效时间 */
    private LocalDateTime priceTime;
    /** 今日涨跌幅 小数点后3位 */
    private BigDecimal currPercentDiff;
    /** 今日涨跌值 */
    private BigDecimal currDiff;
    /** 可交易标识  T-可交易,S-停止交易 */
    private String tranCode;
    /** 货币对优先级 */
    private String sortPriority;
    /** 首货币代码 */
    private String sourceCurrencyCode;
    /** 尾货币代码 */
    private String targetCurrencyCode;
    /** 中间价 小数位比买入/卖出价多一位 */
    private BigDecimal referPrice;
    /** 开盘价格 */
    private BigDecimal openPrice;
    /** 最高值 */
    private BigDecimal maxPrice;
    /** 最低值 */
    private BigDecimal minPrice;

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

    public LocalDateTime getPriceTime() {
        return priceTime;
    }

    public void setPriceTime(LocalDateTime priceTime) {
        this.priceTime = priceTime;
    }

    public BigDecimal getCurrPercentDiff() {
        return currPercentDiff;
    }

    public void setCurrPercentDiff(BigDecimal currPercentDiff) {
        this.currPercentDiff = currPercentDiff;
    }

    public BigDecimal getCurrDiff() {
        return currDiff;
    }

    public void setCurrDiff(BigDecimal currDiff) {
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

    public BigDecimal getReferPrice() {
        return referPrice;
    }

    public void setReferPrice(BigDecimal referPrice) {
        this.referPrice = referPrice;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
}
