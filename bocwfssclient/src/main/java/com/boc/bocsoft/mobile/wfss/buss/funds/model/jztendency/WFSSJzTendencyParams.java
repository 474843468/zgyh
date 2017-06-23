package com.boc.bocsoft.mobile.wfss.buss.funds.model.jztendency;

/**
 * 3.3 基金详情接口2（净值走势-趋势图、历史净值）
 * Created by gwluo on 2016/10/25.
 */

public class WFSSJzTendencyParams {
    private String fundId;//	基金Id
    /**
     * 周期
     * m-一个月
     * q-三个月
     * h-半年
     * y-一年
     */
    private String fundCycle;

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public void setFundCycle(String fundCycle) {
        this.fundCycle = fundCycle;
    }
}
