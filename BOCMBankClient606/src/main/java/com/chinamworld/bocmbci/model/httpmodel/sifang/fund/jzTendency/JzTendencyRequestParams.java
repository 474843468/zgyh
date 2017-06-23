package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.jzTendency;

import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.BaseSFFundRequestModel;

/**
 * Created by Administrator on 2016/10/29 0027.
 * 净值走势-趋势图、历史净值上送字段
 */
public class JzTendencyRequestParams extends BaseSFFundRequestModel {
    @Override
    public Object getExtendParam() {
        return "jzTendency";
    }

    private String fundId;//	基金Id
    private String fundBakCode;
    private String fundCycle;//周期	m-一个月 q-三个月 h-半年 y-一年



    public JzTendencyRequestParams(String fundBakCode/*fundId*/, String fundCycle){
        //this.fundId = fundId;
        this.fundBakCode = fundBakCode;
        this.fundCycle = fundCycle;

    }

}
