package com.chinamworld.bocmbci.model.httpmodel.sifang.queryktendency;


import com.chinamworld.bocmbci.model.httpmodel.sifang.BaseSFForexRequestModel;

/**
 * 外汇、贵金属K线图查询
 * Created by yuht on 2016/10/20.
 */
public class QuerykTendencyRequestParams extends BaseSFForexRequestModel {
    @Override
    public Object getExtendParam() {
        return "queryKTendency";
    }

    /** 货币对代码 */
    private String ccygrp;
    /** 趋势类型 */
    private String kType;
    /** 牌价类型 */
    private String cardType;
    /** 牌价种类 */
    private String cardClass;
    /** 时间区间 */
    private String timeZone;


    public QuerykTendencyRequestParams(String ccygrp, String kType, String cardType, String cardClass, String timeZone){
        this.ccygrp = ccygrp;
        this.kType = kType;
        this.cardType = cardType;
        this.cardClass = cardClass;
        this.timeZone = timeZone;
    }
}
