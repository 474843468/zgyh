package com.chinamworld.bocmbci.model.httpmodel.sifang.querymultiplequotation;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class QueryMultipleQuotationResult {

    public List<QueryMultipleQuotationItem> getItem() {
        return items;
    }

    public void setItem(List<QueryMultipleQuotationItem> item) {
        this.items = item;
    }

    private List<QueryMultipleQuotationItem> items;

    public String getRcdcnt() {
        return rcdcnt;
    }

    public void setRcdcnt(String rcdcnt) {
        this.rcdcnt = rcdcnt;
    }

    private String rcdcnt;

    public class QueryMultipleQuotationItem {

        public String sellPrice;
        public String currPercentDiff;
        public String currDiff;
        public String priceTime;
        public String tranCode;
        public String sortPriority;
        public String sourceCurrencyCode;
        public String targetCurrencyCode;
        public String ccygrpNm;
        public String buyPrice;

        public String getCcygrpNm() {
            return ccygrpNm;
        }

        public void setCcygrpNm(String ccygrpNm) {
            this.ccygrpNm = ccygrpNm;
        }

        public String getBuyPrice() {
            return buyPrice;
        }

        public void setBuyPrice(String buyPrice) {
            this.buyPrice = buyPrice;
        }

        public String getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
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

        public String getPriceTime() {
            return priceTime;
        }

        public void setPriceTime(String priceTime) {
            this.priceTime = priceTime;
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
}
