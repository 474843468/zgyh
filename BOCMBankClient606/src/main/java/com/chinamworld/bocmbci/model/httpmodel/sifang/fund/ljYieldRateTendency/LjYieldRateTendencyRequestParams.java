package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.ljYieldRateTendency;

import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.BaseSFFundRequestModel;

/**
 * Created by Administrator on 2016/10/29 0029.
 */
public class LjYieldRateTendencyRequestParams extends BaseSFFundRequestModel {
    @Override
    public Object getExtendParam() {
        return "ljYieldRateTendency";
    }
    private String fundId;//	基金Id
    private String fundBakCode;
    private String fundCycle;//周期	m-一个月 q-三个月 h-半年 y-一年



    public LjYieldRateTendencyRequestParams(String fundBakCode, String fundCycle){
        this.fundBakCode = fundBakCode;
        this.fundCycle = fundCycle;

    }
}
