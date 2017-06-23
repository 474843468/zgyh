package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model;

/**
 * 查询最大跨度和最长时间范围 View层数据模型
 * Created by wangf on 2016/7/15.
 */
public class InquiryRangeQueryViewModel {


    /**
     * 查询最大跨度和最长时间范围 返回数据项
     */

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
