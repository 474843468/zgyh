package com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querymultiplequotation;

import com.boc.bocsoft.mobile.wfss.buss.common.WFSSBaseParamsModel;
import com.boc.bocsoft.mobile.wfss.common.globle.WFSSGlobalConst;

/**
 * 2.1外汇、贵金属多笔行情查询 queryMultipleQuotation
 * Created by gwluo on 2016/10/24.
 */

public class WFSSQueryMultipleQuotationParams extends WFSSBaseParamsModel {
    //牌价类型	F-外汇牌价 G-贵金属牌价
    private String cardType;
    /*牌价种类 R-实盘 M-虚盘*/
    private String cardClass;
    /*涨跌幅排序	UP - 升序DN - 降序为空按优先级排序*/
    private String pSort;

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public void setpSort(String pSort) {
        this.pSort = pSort;
    }

    @Override
    public String getModuleName() {
        return WFSSGlobalConst.WFSS_FOREX;
    }

    @Override
    public String getMethodName() {
        return "queryMultipleQuotation";
    }
}
