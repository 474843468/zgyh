package com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGoldTradeRateQuery;

import java.math.BigDecimal;

/**
 * 黄金行情查询 返回结果
 * Created by lxw on 2016/8/16 0016.
 */
public class PsnGoldTradeRateQueryResult {

    /**
     * createDate : 2007/12/08 14:56:56
     * sellRate : 10.85
     * buyRate : 10.85
     * sourceCurrencyCode : 034
     * targetCurrencyCode : 014
     * upDownFlag : 2
     */

    private String createDate;
    private BigDecimal sellRate;
    private BigDecimal buyRate;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private String upDownFlag;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
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

    public String getUpDownFlag() {
        return upDownFlag;
    }

    public void setUpDownFlag(String upDownFlag) {
        this.upDownFlag = upDownFlag;
    }
}
