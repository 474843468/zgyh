package com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofweektendency;

/**
 * 3.6 基金详情接口5（七日年化收益率--趋势图、历史)
 * Created by gwluo on 2016/10/25.
 */

public class WFSSYieldOfWeekTendencyParams {
    private String fundId;//	基金Id
    /* 周期
    w-七日
    m-一个月
    t-两个月*/
    private String fundCycle;

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public void setFundCycle(String fundCycle) {
        this.fundCycle = fundCycle;
    }
}
