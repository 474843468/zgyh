package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOffFee;

import java.math.BigDecimal;

/**
 * 作者：xwg on 16/12/8 17:14
 */
public class PsnCrcdForeignPayOffFeeResult {

    /**
     *  拟收费用
     */
    private BigDecimal preCommissionCharge;
    /**
     *  应收费用
     */
    private BigDecimal needCommissionCharge;
    /**
     *   是否签约汇款笔数套餐
     *   1:签约汇款笔数套餐
     0：未签约汇款笔数套餐
     */
    private String remitSetMealFlag;
    /**
    *  试费查询是否成功标识位		1：查询成功0：查询失败
    */
    private String getChargeFlag;

    public String getGetChargeFlag() {
        return getChargeFlag;
    }

    public void setGetChargeFlag(String getChargeFlag) {
        this.getChargeFlag = getChargeFlag;
    }

    public BigDecimal getNeedCommissionCharge() {
        return needCommissionCharge;
    }

    public void setNeedCommissionCharge(BigDecimal needCommissionCharge) {
        this.needCommissionCharge = needCommissionCharge;
    }

    public BigDecimal getPreCommissionCharge() {
        return preCommissionCharge;
    }

    public void setPreCommissionCharge(BigDecimal preCommissionCharge) {
        this.preCommissionCharge = preCommissionCharge;
    }

    public String getRemitSetMealFlag() {
        return remitSetMealFlag;
    }

    public void setRemitSetMealFlag(String remitSetMealFlag) {
        this.remitSetMealFlag = remitSetMealFlag;
    }
}
