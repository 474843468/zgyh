package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnGetAllExchangeRatesOutlay;

import java.math.BigDecimal;

/**
 * result：双向宝-买入卖出价数据：PsnGetAllExchangeRatesOutlay -响应model
 * {I43-4.18 018 PsnGetAllExchangeRatesOutlay登录前贵金属、外汇、双向宝行情查询}
 * Created by yx on 2016年12月15日 09:24:01
 */
public class PsnGetAllExchangeRatesOutlayResult {

    /**
     * updateDate : 2021/04/01 14:38:02
     * flag : 1
     * targetCurrency : {"code":"001","i18nId":"CNY","fraction":2}
     * sourceCurrency : {"code":"035","i18nId":"GLD","fraction":0}
     * sourceCurrencyCode : 035
     * type : M
     * targetCurrencyCode : 001
     * vfgType : G
     * sellRate : 243.64
     * midRate : null
     * rate : 0
     * buyNoteRate : 0
     * spotRate : null
     * ibkNum : 40142
     * baseAmt : null
     * state : T
     * sellNoteRate : 0
     * quoteDate : 1617259078000
     * buyRate : 243
     * createDate : 1617259082308
     */

    /**
     * 更新时间
     */
    private String updateDate;
    /**
     * X-FUND涨跌标志	String	0未变,1涨,2跌
     */
    private String flag;
    /**
     * targetCurrency数组开始
     */
    private TargetCurrencyEntity targetCurrency;
    /**
     * sourceCurrency数组开始
     */
    private SourceCurrencyEntity sourceCurrency;
    /**
     * 兑换币别
     */
    private String sourceCurrencyCode;
    /**
     * X-FUND牌价类型	String	F/G/M
     */
    private String type;
    /**
     * 目标货币(人民币)
     */
    private String targetCurrencyCode;
    /**
     * 双向宝类型	String	F:虚盘外汇，G:虚盘贵金属
     * （当type为M-虚盘时有意义。）
     */
    private String vfgType;
    /**
     * 卖出牌价(现汇)	BigDecimal	页面显示该项
     */
    private BigDecimal sellRate;
    /**
     *
     */
    private String midRate;
    /**
     * X-FUND汇率	BigDecimal
     */
    private BigDecimal rate;
    /**
     * 现钞买入价	BigDecimal
     */
    private BigDecimal buyNoteRate;
    /**
     * 中间价	BigDecimal
     */
    private BigDecimal spotRate;
    /**
     * X-FUND联行号	String
     */
    private String ibkNum;
    /**
     * 计价单位	Integer
     */
    private String baseAmt;
    /**
     * 货币对状态	String	T可交易；S停止交易
     */
    private String state;
    /**
     * 现钞卖出价	BigDecimal
     */
    private BigDecimal sellNoteRate;
    /**
     *
     */
    private long quoteDate;
    /**
     * 买入牌价(现汇)	BigDecimal	页面显示该项
     */
    private BigDecimal buyRate;
    /**
     * 添加时间	Timestamp
     */
    private long createDate;

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setTargetCurrency(TargetCurrencyEntity targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public void setSourceCurrency(SourceCurrencyEntity sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public void setSourceCurrencyCode(String sourceCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public void setVfgType(String vfgType) {
        this.vfgType = vfgType;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public void setMidRate(String midRate) {
        this.midRate = midRate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public void setBuyNoteRate(BigDecimal buyNoteRate) {
        this.buyNoteRate = buyNoteRate;
    }

    public void setSpotRate(BigDecimal spotRate) {
        this.spotRate = spotRate;
    }

    public void setIbkNum(String ibkNum) {
        this.ibkNum = ibkNum;
    }

    public void setBaseAmt(String baseAmt) {
        this.baseAmt = baseAmt;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSellNoteRate(BigDecimal sellNoteRate) {
        this.sellNoteRate = sellNoteRate;
    }

    public void setQuoteDate(long quoteDate) {
        this.quoteDate = quoteDate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getFlag() {
        return flag;
    }

    public TargetCurrencyEntity getTargetCurrency() {
        return targetCurrency;
    }

    public SourceCurrencyEntity getSourceCurrency() {
        return sourceCurrency;
    }

    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public String getType() {
        return type;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public String getVfgType() {
        return vfgType;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public String getMidRate() {
        return midRate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getBuyNoteRate() {
        return buyNoteRate;
    }

    public BigDecimal getSpotRate() {
        return spotRate;
    }

    public String getIbkNum() {
        return ibkNum;
    }

    public String getBaseAmt() {
        return baseAmt;
    }

    public String getState() {
        return state;
    }

    public BigDecimal getSellNoteRate() {
        return sellNoteRate;
    }

    public long getQuoteDate() {
        return quoteDate;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public static class TargetCurrencyEntity {
        /**
         * code : 001
         * i18nId : CNY
         * fraction : 2
         */
        /**
         * 货币码	String
         */
        private String code;
        /**
         * 国际化标识	String
         */
        private String i18nId;
        /**
         * 辅币位数	Integer
         */
        private int fraction;

        public void setCode(String code) {
            this.code = code;
        }

        public void setI18nId(String i18nId) {
            this.i18nId = i18nId;
        }

        public void setFraction(int fraction) {
            this.fraction = fraction;
        }

        public String getCode() {
            return code;
        }

        public String getI18nId() {
            return i18nId;
        }

        public int getFraction() {
            return fraction;
        }
    }

    public static class SourceCurrencyEntity {
        /**
         * code : 035
         * i18nId : GLD
         * fraction : 0
         */
        /**
         * 货币码	String
         */
        private String code;
        /**
         * 国际化标识	String
         */
        private String i18nId;
        /**
         * 辅币位数	Integer
         */
        private int fraction;

        public void setCode(String code) {
            this.code = code;
        }

        public void setI18nId(String i18nId) {
            this.i18nId = i18nId;
        }

        public void setFraction(int fraction) {
            this.fraction = fraction;
        }

        public String getCode() {
            return code;
        }

        public String getI18nId() {
            return i18nId;
        }

        public int getFraction() {
            return fraction;
        }
    }
}
