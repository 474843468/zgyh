package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQuery;

import java.io.Serializable;

/**
 * 4.72 072业绩基准产品预计年收益率查询    请求Model
 * PsnXpadExpectYieldQuery
 * Created by zn on 2016/11/10.
 */
public class PsnXpadExpectYieldQueryReqModel implements Serializable {
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
