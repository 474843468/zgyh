package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnPersionalTransDetailQuery;

/**
 * Created by huixiaobo on 2016/11/19.
 * 059基金对账单交易明细查询-上送参数
 */
public class PsnPersionalTransDetailQueryParams {
    /**截止日期*/
    private String endDate;
    /**开始日期*/
    private String startDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
