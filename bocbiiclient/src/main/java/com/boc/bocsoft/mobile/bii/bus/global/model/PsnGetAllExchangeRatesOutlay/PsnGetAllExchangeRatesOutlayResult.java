package com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay;

import java.math.BigDecimal;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

/**
 * 登录前贵金属、外汇、双向宝行情查询返回报文
 * Created by lxw on 2016/8/22 0022.
 */
public class PsnGetAllExchangeRatesOutlayResult  {


    /**
     * sourceCurrencyCode : 035
     * sourceCurrency : {"code":"035","fraction":0,"i18nId":"GLD"}
     * targetCurrencyCode : 001
     * targetCurrency : {"code":"001","fraction":2,"i18nId":"CNY"}
     * baseAmt : null
     * buyRate : 250.34
     * sellRate : 250.67
     * spotRate : null
     * ibkNum : 40142
     * type : G
     * vfgType : null
     * flag : 0
     * rate : 0
     * createDate : 1768821608000
     * state : T
     * buyNoteRate : 0
     * sellNoteRate : 0
     * updateDate : 2026/01/19 19:20:08
     * midRate : null
     * quoteDate : null
     */

    private String sourceCurrencyCode;
    /**
     * code : 035
     * fraction : 0
     * i18nId : GLD
     */

    private SourceCurrencyBean sourceCurrency;
    private String targetCurrencyCode;
    /**
     * code : 001
     * fraction : 2
     * i18nId : CNY
     */

    private TargetCurrencyBean targetCurrency;
    private int baseAmt;
    private BigDecimal buyRate;
    private BigDecimal sellRate;
    private BigDecimal spotRate;
    private String ibkNum;
    private String type;
    private String vfgType;
    private String flag;
    private int rate;
    private String createDate;
    private String state;
    private BigDecimal buyNoteRate;
    private BigDecimal sellNoteRate;
    private String updateDate;
    private BigDecimal midRate;
    //TODO
    private Instant quoteDate;


    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public void setSourceCurrencyCode(String sourceCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode;
    }

    public SourceCurrencyBean getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(SourceCurrencyBean sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public TargetCurrencyBean getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(TargetCurrencyBean targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public int getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(int baseAmt) {
        this.baseAmt = baseAmt;
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

    public BigDecimal getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(BigDecimal spotRate) {
        this.spotRate = spotRate;
    }

    public String getIbkNum() {
        return ibkNum;
    }

    public void setIbkNum(String ibkNum) {
        this.ibkNum = ibkNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVfgType() {
        return vfgType;
    }

    public void setVfgType(String vfgType) {
        this.vfgType = vfgType;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getBuyNoteRate() {
        return buyNoteRate;
    }

    public void setBuyNoteRate(BigDecimal buyNoteRate) {
        this.buyNoteRate = buyNoteRate;
    }

    public BigDecimal getSellNoteRate() {
        return sellNoteRate;
    }

    public void setSellNoteRate(BigDecimal sellNoteRate) {
        this.sellNoteRate = sellNoteRate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getMidRate() {
        return midRate;
    }

    public void setMidRate(BigDecimal midRate) {
        this.midRate = midRate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getQuoteDate() {
        return LocalDateTime.ofInstant(quoteDate, ZoneId.systemDefault());
    }

    public void setQuoteDate(Instant quoteDate) {
        this.quoteDate = quoteDate;
    }

    public static class SourceCurrencyBean {
        private String code;
        private int fraction;
        private String i18nId;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getFraction() {
            return fraction;
        }

        public void setFraction(int fraction) {
            this.fraction = fraction;
        }

        public String getI18nId() {
            return i18nId;
        }

        public void setI18nId(String i18nId) {
            this.i18nId = i18nId;
        }
    }

    public static class TargetCurrencyBean {
        private String code;
        private int fraction;
        private String i18nId;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getFraction() {
            return fraction;
        }

        public void setFraction(int fraction) {
            this.fraction = fraction;
        }

        public String getI18nId() {
            return i18nId;
        }

        public void setI18nId(String i18nId) {
            this.i18nId = i18nId;
        }
    }
}
