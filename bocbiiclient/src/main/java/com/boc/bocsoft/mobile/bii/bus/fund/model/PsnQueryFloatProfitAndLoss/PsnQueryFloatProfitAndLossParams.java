package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss;

/**
 * Created by taoyongzhen on 2016/11/30.
 */

public class PsnQueryFloatProfitAndLossParams {

    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 截止日期
     */
    private String endDate;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

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
