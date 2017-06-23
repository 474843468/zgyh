package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadGuarantyBuyResult;

/**
 * Created by wangtong on 2016/9/22.
 */
public class PsnXpadGuarantyBuyResult {

    private ResultBean result;

    private Object error;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public static class ResultBean {
        private Object currency;
        private String cashRemit;
        private Object GuarantyBuyAmout;
        private int buyPrice;
        private String tranSeq;
        private Object productName;
        private String tranDate;
        private String productCode;
        private String productBeginDate;
        private String productEndDate;
        private int productValue;

        public Object getCurrency() {
            return currency;
        }

        public void setCurrency(Object currency) {
            this.currency = currency;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public Object getGuarantyBuyAmout() {
            return GuarantyBuyAmout;
        }

        public void setGuarantyBuyAmout(Object GuarantyBuyAmout) {
            this.GuarantyBuyAmout = GuarantyBuyAmout;
        }

        public int getBuyPrice() {
            return buyPrice;
        }

        public void setBuyPrice(int buyPrice) {
            this.buyPrice = buyPrice;
        }

        public String getTranSeq() {
            return tranSeq;
        }

        public void setTranSeq(String tranSeq) {
            this.tranSeq = tranSeq;
        }

        public Object getProductName() {
            return productName;
        }

        public void setProductName(Object productName) {
            this.productName = productName;
        }

        public String getTranDate() {
            return tranDate;
        }

        public void setTranDate(String tranDate) {
            this.tranDate = tranDate;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductBeginDate() {
            return productBeginDate;
        }

        public void setProductBeginDate(String productBeginDate) {
            this.productBeginDate = productBeginDate;
        }

        public String getProductEndDate() {
            return productEndDate;
        }

        public void setProductEndDate(String productEndDate) {
            this.productEndDate = productEndDate;
        }

        public int getProductValue() {
            return productValue;
        }

        public void setProductValue(int productValue) {
            this.productValue = productValue;
        }
    }
}
