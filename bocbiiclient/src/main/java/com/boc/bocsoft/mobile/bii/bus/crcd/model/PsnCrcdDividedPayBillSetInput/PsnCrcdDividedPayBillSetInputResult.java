package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput;

import java.math.BigDecimal;

/**
 * 办理账单分期输入
 * Created by wangf on 2016/11/22.
 */
public class PsnCrcdDividedPayBillSetInputResult {

    //可分期金额上限
    private BigDecimal upInstmtAmount;
    //可分期金额下限
    private BigDecimal lowInstmtAmount;

    public BigDecimal getLowInstmtAmount() {
        return lowInstmtAmount;
    }

    public void setLowInstmtAmount(BigDecimal lowInstmtAmount) {
        this.lowInstmtAmount = lowInstmtAmount;
    }

    public BigDecimal getUpInstmtAmount() {
        return upInstmtAmount;
    }

    public void setUpInstmtAmount(BigDecimal upInstmtAmount) {
        this.upInstmtAmount = upInstmtAmount;
    }
}
