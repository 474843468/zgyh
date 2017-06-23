package com.chinamworld.bocmbci.model.httpmodel.sifang.queryAverageTendency;


import com.chinamworld.bocmbci.model.httpmodel.sifang.BaseSFForexRequestModel;

/**
 * Created by yuht on 2016/10/25.
 */
public class QueryAverageTendencyRequestParams extends BaseSFForexRequestModel {
    @Override
    public Object getExtendParam() {
        return "queryAverageTendency";
    }

    private String ccygrp;
    private String cardType;
    private String cardClass;
    private String tendencyType;

    public QueryAverageTendencyRequestParams(String ccygrp, String cardType, String cardClass, String tendencyType){
        this.ccygrp = ccygrp;
        this.cardType = cardType;
        this.cardClass = cardClass;
        this.tendencyType = tendencyType;
    }
}
