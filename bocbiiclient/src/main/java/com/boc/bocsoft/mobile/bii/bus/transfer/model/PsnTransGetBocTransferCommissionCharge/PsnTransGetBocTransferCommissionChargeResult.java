package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge;

import java.math.BigDecimal;

/**
 * Created by WM on 2016/6/12.
 */
public class PsnTransGetBocTransferCommissionChargeResult {

    /**
     * needCommissionCharge : 20
     * preCommissionCharge : 20
     * remitSetMealFlag :
     * getChargeFlag :
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
