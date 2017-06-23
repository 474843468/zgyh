package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge;

import java.math.BigDecimal;

/**
 * Created by WM on 2016/7/1.
 */
public class PsnTransGetNationalTransferCommissionChargeResult {

    /**
     * needCommissionCharge : 23
     * preCommissionCharge : 32
     * remitSetMealFlag : 0
     * getChargeFlag : 0
     */

    private BigDecimal needCommissionCharge;
    private BigDecimal preCommissionCharge;
    private String remitSetMealFlag;
    private String getChargeFlag;

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

    public String getGetChargeFlag() {
        return getChargeFlag;
    }

    public void setGetChargeFlag(String getChargeFlag) {
        this.getChargeFlag = getChargeFlag;
    }
}
