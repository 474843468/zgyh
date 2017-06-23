package com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querysingelquotation;

import com.boc.bocsoft.mobile.wfss.buss.common.WFSSBaseParamsModel;
import com.boc.bocsoft.mobile.wfss.common.globle.WFSSGlobalConst;

/**
 * 2.2 外汇、贵金属单笔行情查询
 * Created by gwluo on 2016/10/24.
 */

public class WFSSQuerySingelQuotationParams extends WFSSBaseParamsModel {
    //货币对代码	6	Y	sourceCurrencyCode+targetCurrencyCode
    private String ccygrp;
    //牌价类型	1	Y	F-外汇牌价 G-贵金属牌价
    private String cardType;
    //牌价种类	1	Y	R-实盘 M-虚盘
    private String cardClass;

    public void setCcygrp(String ccygrp) {
        this.ccygrp = ccygrp;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    @Override
    public String getModuleName() {
        return WFSSGlobalConst.WFSS_FOREX;
    }

    @Override
    public String getMethodName() {
        return "querySingelQuotation";
    }
}
