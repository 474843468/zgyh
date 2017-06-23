package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQuery;

/**
 * 4.72 072业绩基准产品预计年收益率查询 PsnXpadExpectYieldQuery
 * Created by zn on 2016/11/10.
 */
public class PsnXpadExpectYieldQueryParams {

    /**
     * 产品代码	String
     */
    private String productCode;
    /**
     * 查询日期	String
     */
    private String queryDate;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }
}
