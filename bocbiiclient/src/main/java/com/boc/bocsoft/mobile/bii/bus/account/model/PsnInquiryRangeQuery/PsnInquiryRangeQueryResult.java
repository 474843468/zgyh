package com.boc.bocsoft.mobile.bii.bus.account.model.PsnInquiryRangeQuery;

/**
 * 交易明细 -- 查询最大跨度和最长时间范围
 * Created by wangf on 2016/7/15.
 */
public class PsnInquiryRangeQueryResult {

    /**
     * maxMonths : 6
     * maxYears : 2
     */

    //单次查询最大跨度（月）
    private int maxMonths;
    //可查询的最长时间（年）
    private int maxYears;

    public int getMaxMonths() {
        return maxMonths;
    }

    public void setMaxMonths(int maxMonths) {
        this.maxMonths = maxMonths;
    }

    public int getMaxYears() {
        return maxYears;
    }

    public void setMaxYears(int maxYears) {
        this.maxYears = maxYears;
    }
}
