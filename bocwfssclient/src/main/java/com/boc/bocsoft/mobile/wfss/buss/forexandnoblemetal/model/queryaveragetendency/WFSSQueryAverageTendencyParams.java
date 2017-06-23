package com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryaveragetendency;

import com.boc.bocsoft.mobile.wfss.buss.common.WFSSBaseParamsModel;
import com.boc.bocsoft.mobile.wfss.common.globle.WFSSGlobalConst;

/**
 * 2.4 外汇、贵金属趋势图查询
 * Created by gwluo on 2016/10/25.
 */
public class WFSSQueryAverageTendencyParams extends WFSSBaseParamsModel {
    /**
     * 货币对代码sourceCurrencyCode+targetCurrencyCode
     */
    private String ccygrp;

    @Override
    public String getMethodName() {
        return "queryAverageTendency";
    }

    /**
     * 牌价类型		F-外汇牌价
     * G-贵金属牌价
     */
    private String cardType;
    /*牌价种类 R-实盘 M-虚盘*/
    private String cardClass;
    /**
     * 趋势类型
     * O-一小时
     * F-四小时
     * D-日图
     * W-周图
     * M-月图
     */
    private String tendencyType;

    public void setCcygrp(String ccygrp) {
        this.ccygrp = ccygrp;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public void setTendencyType(String tendencyType) {
        this.tendencyType = tendencyType;
    }

    @Override
    public String getModuleName() {
        return WFSSGlobalConst.WFSS_FOREX;
    }
}
