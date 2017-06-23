package com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.goldtraderatequery;

import java.math.BigDecimal;

/**
 * I46 账户贵金属 4.1 001 PsnGoldTradeRateQuery黄金行情查询
 * Created by gwluo on 2016/9/2.
 */
public class GoldTradeRateQueryResult {

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
