package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProductQry;

import java.util.List;

/**
 * Created by XieDu on 2016/7/19.
 */
public class PsnOnLineLoanProductResult {


    private List<PsnOnLineLoanProductBean> list;

    public List<PsnOnLineLoanProductBean> getList() { return list;}

    public void setList(List<PsnOnLineLoanProductBean> list) { this.list = list;}

    public static class PsnOnLineLoanProductBean {
        private String productCode;
        private String productName;

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
    }
}

