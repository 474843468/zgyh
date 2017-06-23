package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQueryOutlay;

import java.io.Serializable;

/**
 * 4.73 073登录前业绩基准产品预计年收益率查询PsnXpadExpectYieldQueryOutlay
 * Created by zn on 2016/11/23.
 */
public class PsnXpadExpectYieldQueryOutlayReqModel implements Serializable {
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
