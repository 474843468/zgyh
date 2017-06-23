package com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGetAllExchangeRatesOutlay;

import java.util.List;

/**
 * 4.18 018 PsnGetAllExchangeRatesOutlay登录前贵金属、外汇、双向宝行情查询
 * Created by Administrator on 2016/8/22 0022.
 */
public class PsnGetAllExchangeRatesOutlayResult {
    private List<ListBean> listBeen;

    public List<ListBean> getListBeen() {
        return listBeen;
    }

    public void setListBeen(List<ListBean> listBeen) {
        this.listBeen = listBeen;
    }

    public static class ListBean {
        private String state;//货币对状态	String	T可交易；S停止交易
        private String type;//X-FUND牌价类型	String	F/G/M
        private String vfgType;//双向宝类型	String	F:虚盘外汇，G:虚盘贵金属（当type为M-虚盘时有意义。）
        private String createDate;//添加时间	Timestamp
        private String sellRate;//卖出牌价(现汇)	BigDecimal	页面显示该项
        private String rate;//X-FUND汇率	BigDecimal
        private String buyRate;//买入牌价(现汇)	BigDecimal 页面显示该项
        private String sourceCurrencyCode;//兑换币别	String
        private String flag;//X-FUND涨跌标志	String	0未变,1涨,2跌
        private String ibkNum;//X-FUND联行号	String
        private List<SourceCurrency> sourceCurrency;//兑换币别（首货币）	LIST
        private List<TargetCurrency> targetCurrency;//目标货币(尾货币)	LIST

    }

    public static class SourceCurrency {
        private String code;//货币码	String
        private String fraction;//辅币位数	Integer
        private String i18nId;//国际化标识	String

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFraction() {
            return fraction;
        }

        public void setFraction(String fraction) {
            this.fraction = fraction;
        }

        public String getI18nId() {
            return i18nId;
        }

        public void setI18nId(String i18nId) {
            this.i18nId = i18nId;
        }
    }

    public static class TargetCurrency {
        private String code;//货币码	String
        private String fraction;//辅币位数	Integer
        private String i18nId;//国际化标识	String
        private String updateDate;//更新时间	String
        private String targetCurrencyCode;//目标货币(人民币)	String
        private String spotRate;//中间价	BigDecimal
        private String baseAmt;//计价单位	Integer
        private String buyNoteRate;//现钞买入价	BigDecimal
        private String sellNoteRate;//现钞卖出价	BigDecimal

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFraction() {
            return fraction;
        }

        public void setFraction(String fraction) {
            this.fraction = fraction;
        }

        public String getI18nId() {
            return i18nId;
        }

        public void setI18nId(String i18nId) {
            this.i18nId = i18nId;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getTargetCurrencyCode() {
            return targetCurrencyCode;
        }

        public void setTargetCurrencyCode(String targetCurrencyCode) {
            this.targetCurrencyCode = targetCurrencyCode;
        }

        public String getSpotRate() {
            return spotRate;
        }

        public void setSpotRate(String spotRate) {
            this.spotRate = spotRate;
        }

        public String getBaseAmt() {
            return baseAmt;
        }

        public void setBaseAmt(String baseAmt) {
            this.baseAmt = baseAmt;
        }

        public String getBuyNoteRate() {
            return buyNoteRate;
        }

        public void setBuyNoteRate(String buyNoteRate) {
            this.buyNoteRate = buyNoteRate;
        }

        public String getSellNoteRate() {
            return sellNoteRate;
        }

        public void setSellNoteRate(String sellNoteRate) {
            this.sellNoteRate = sellNoteRate;
        }
    }
}
