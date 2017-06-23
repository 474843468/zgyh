package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyDetailQuery;

/**
 * Created by huixiaobo on 2016/11/17.
 * 054定投申请明细查询-上送参数
 */
public class PsnFundScheduledBuyDetailQueryParams {

    /**定投申请日期*/
    private String fundScheduleDate;
    /**定投序号*/
    private String scheduleBuyNum;

    public String getFundScheduleDate() {
        return fundScheduleDate;
    }

    public void setFundScheduleDate(String fundScheduleDate) {
        this.fundScheduleDate = fundScheduleDate;
    }

    public String getScheduleBuyNum() {
        return scheduleBuyNum;
    }

    public void setScheduleBuyNum(String scheduleBuyNum) {
        this.scheduleBuyNum = scheduleBuyNum;
    }

    @Override
    public String toString() {
        return "PsnFundScheduledBuyDetailQueryParams{" +
                "fundScheduleDate='" + fundScheduleDate + '\'' +
                ", scheduleBuyNum='" + scheduleBuyNum + '\'' +
                '}';
    }
}
