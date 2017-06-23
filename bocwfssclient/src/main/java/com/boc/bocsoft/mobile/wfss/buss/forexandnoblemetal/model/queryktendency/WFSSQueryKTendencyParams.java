package com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryktendency;

import com.boc.bocsoft.mobile.wfss.buss.common.WFSSBaseParamsModel;
import com.boc.bocsoft.mobile.wfss.common.globle.WFSSGlobalConst;

/**
 * 2.3 外汇、贵金属K线图查询 queryKTendency
 * Created by gwluo on 2016/10/25.
 */

public class WFSSQueryKTendencyParams extends WFSSBaseParamsModel {
    private String ccygrp;//	货币对代码	6	Y	sourceCurrencyCode+targetCurrencyCode
    /*趋势类型	2	Y	OK：一小时K线图
    FK：四小时K线图
    DK：日K线图
    WK：周K线图
    MK：月K线图*/
    private String kType;
    /*牌价类型	1	Y	F-外汇牌价
    G-贵金属牌价*/
    private String cardType;
    /*牌价种类 R-实盘  M-虚盘*/
    private String cardClass;
    /*时间区间 20 N 为空，返回全量数据
    不为空，返回该时区之后的K线数据（包含该时区）*/
    private String timeZone;

    public void setCcygrp(String ccygrp) {
        this.ccygrp = ccygrp;
    }

    public void setkType(String kType) {
        this.kType = kType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String getModuleName() {
        return WFSSGlobalConst.WFSS_FOREX;
    }

    @Override
    public String getMethodName() {
        return "queryKTendency";
    }
}
