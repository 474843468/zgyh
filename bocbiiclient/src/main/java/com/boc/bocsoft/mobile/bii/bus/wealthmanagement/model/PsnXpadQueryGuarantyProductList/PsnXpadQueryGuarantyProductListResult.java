package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductList;

import java.util.List;

/**
 * Created by wangtong on 2016/9/13.
 */
public class PsnXpadQueryGuarantyProductListResult {

    private String accountNumber;

    private List<ListBean> list;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private double cashShare;
        private String productCode;
        private String productName;
        private double remitShare;
        private String accountNumber;

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public double getCashShare() {
            return cashShare;
        }

        public void setCashShare(double cashShare) {
            this.cashShare = cashShare;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getRemitShare() {
            return remitShare;
        }

        public void setRemitShare(double remitShare) {
            this.remitShare = remitShare;
        }
    }
}
