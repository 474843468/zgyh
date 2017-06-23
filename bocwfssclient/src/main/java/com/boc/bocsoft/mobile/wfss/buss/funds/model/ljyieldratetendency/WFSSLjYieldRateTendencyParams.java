package com.boc.bocsoft.mobile.wfss.buss.funds.model.ljyieldratetendency;

/**
 * 3.4 基金详情接口3（基金累计收益率--趋势图、历史累计收益率）
 * Created by gwluo on 2016/10/25.
 */

public class WFSSLjYieldRateTendencyParams {
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
