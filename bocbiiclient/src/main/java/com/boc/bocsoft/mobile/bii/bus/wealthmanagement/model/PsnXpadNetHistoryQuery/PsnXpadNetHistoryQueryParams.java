package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQuery;

/**
 * 历史净值查询-请求
 * Created by liuweidong on 2016/10/22.
 */
public class PsnXpadNetHistoryQueryParams {

    private String productCode;
    private String period;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
