package com.boc.bocsoft.mobile.wfss.buss.funds.model.ranktendency;

/**
 * 3.5 基金详情接口4（排名变化--趋势图、历史排名变化)
 * Created by gwluo on 2016/10/25.
 */

public class WFSSRankTendencyParams {
    private String fundId;//基金Id
    /*周期
    m-一个月
    q-三个月
    h-半年
    y-一年*/
    private String fundCycle;

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public void setFundCycle(String fundCycle) {
        this.fundCycle = fundCycle;
    }
}
