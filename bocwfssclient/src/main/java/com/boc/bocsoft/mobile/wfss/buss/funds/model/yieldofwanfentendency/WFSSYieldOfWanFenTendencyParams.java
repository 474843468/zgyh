package com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofwanfentendency;

/**
 * 3.10 基金详情接口10（万份收益率--趋势图、历史)
 * Created by gwluo on 2016/10/26.
 */

public class WFSSYieldOfWanFenTendencyParams {
    private String fundId;//	基金Id
    /*周期
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
