package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWanFenTendency;

import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.BaseSFFundRequestModel;

/**
 * Created by Administrator on 2016/10/29 0029.
 */
public class YieldOfWanFenTendencyRequestParams extends BaseSFFundRequestModel {
    @Override
    public Object getExtendParam() {
        return "yieldOfWanFenTendency";
    }
    private String fundId;//	基金Id
    private String fundBakCode;
    private String fundCycle;//周期	m-一个月 q-三个月 h-半年 y-一年



    public YieldOfWanFenTendencyRequestParams(String fundBakCode, String fundCycle){
        this.fundBakCode = fundBakCode;
        this.fundCycle = fundCycle;

    }
}
