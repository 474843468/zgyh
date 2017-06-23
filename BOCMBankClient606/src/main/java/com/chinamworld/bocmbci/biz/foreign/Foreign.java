package com.chinamworld.bocmbci.biz.foreign;

/**
 * 3.0 外汇
 * @author luqp 2016/10/14
 */
public class Foreign {
    public Foreign() {
    }
    /** 外汇、贵金属单笔行情查询 API-2.1*/
    public static final String QUERYMULTIPLEQUOTATION = "queryMultipleQuotation";
    /** 牌价类型 F-外汇牌价 G-贵金属牌价*/
    public static final String MultiplEcardType = "cardType";
    /** 牌价种类 R-实盘 M-虚盘*/
    public static final String MultiplEcardClass = "cardClass";
    /** 涨跌幅排序 UP - 升序 DN - 降序 为空按优先级排序*/
    public static final String MultiplPSort = "pSort";

    /** 外汇、贵金属单笔行情查询 API-2.2*/
    public static final String QUERYSINGELQUOTATION = "querySingelQuotation";
    /** 货币对代码*/
    public static final String SingelCcygrp = "ccygrp";
    /** 牌价类型 F-外汇牌价 G-贵金属牌价*/
    public static final String SingelCardType = "cardType";
    /** 牌价种类 R-实盘 M-虚盘*/
    public static final String SingelCardClass = "cardClass";

    /** 外汇、贵金属K线图查询 API-2.3*/
    public static final String QUERYKTENDENCY = "queryKTendency";
    /** 货币对代码*/
    public static final String KTccygrp = "ccygrp";
    /** 趋势类型 OK：一小时K线图,FK：四小时K线图,DK：日K线图,WK：周K线图,MK：月K线图*/
    public static final String KTkType = "kType";
    /** 牌价类型 F-外汇牌价 G-贵金属牌价*/
    public static final String KTcardType = "cardType";
    /** 牌价种类 R-实盘 M-虚盘*/
    public static final String KTcardClass = "cardClass";
    /** 时间区间 为空，返回全量数据 不为空，返回该时区之后的K线数据（包含该时区）*/
    public static final String KTtimeZone = "timeZone";

    /** 外汇、贵金属趋势图查询 API-2.4*/
    public static final String QUERYAVERAGETENDENCY = "queryAverageTendency";
    /** 货币对代码*/
    public static final String Averageccygrp = "ccygrp";
    /** 牌价类型 F-外汇牌价 G-贵金属牌价*/
    public static final String AveragecardType = "cardType";
    /** 牌价种类 R-实盘 M-虚盘*/
    public static final String AveragecardClass = "cardClass";
    /** 趋势类型 O-小时,F-四小时,D-日图,W-周图,M - 月图*/
    public static final String AveragetendencyType = "tendencyType";
    /** 今日涨跌幅*/
    public static final String CURRPERCENTDIFF = "currPercentDiff";
    /** 今日涨跌值*/
    public static final String CURRDIFF = "currDiff";
    /** 用户选货币对数据的位置*/
    public static final String SELECTPOSITION = "selectPosition";
    /** 详情页面中间值*/
    public static final String REFERPRICE = "referPrice";
    /** 详情页面中间值*/
    public static final String QRCODEALLTHEINFORMATION = "QRCodeAllTheInformation";

    /** 详情页面中间值*/
    public static final String TASK = "task";
}
