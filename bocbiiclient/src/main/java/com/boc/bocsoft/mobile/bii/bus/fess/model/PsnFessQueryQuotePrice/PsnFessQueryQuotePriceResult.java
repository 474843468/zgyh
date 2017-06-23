package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice;

import java.util.List;

/**
 * I49 4.9 009PsnFessQueryQuotePrice查询结汇购汇牌价列表
 * Created by gwluo on 2016/11/16.
 */

public class PsnFessQueryQuotePriceResult {

    /**
     * quotePriceList	牌价列表
     */
    private List<QuotePrice> quotePriceList;

    public List<QuotePrice> getQuotePriceList() {
        return quotePriceList;
    }

    public void setQuotePriceList(List<QuotePrice> quotePriceList) {
        this.quotePriceList = quotePriceList;
    }

    public class QuotePrice {
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
}
