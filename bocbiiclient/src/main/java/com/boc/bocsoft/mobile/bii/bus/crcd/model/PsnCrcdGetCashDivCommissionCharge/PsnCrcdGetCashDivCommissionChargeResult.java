package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdGetCashDivCommissionCharge;

/**
 * Created by cry7096 on 2016/11/22.
 */
public class PsnCrcdGetCashDivCommissionChargeResult {

    /**
     * divAmount : 分期金额
     * divCharge : 分期手续费
     * firstPayAmount : 分期后每期应还金额-首期
     * perPayAmount : 分期后每期应还金额-后每期
     * divPeriod : 分期期数
     * chargeMode : 分期手续费收取方式
     * divRate : 分期手续费率
     * firstCharge : 首期手续费金额
     * perCharge : 后期每期手续费金额
     */

    private String divAmount;
    private String divCharge;
    private String firstPayAmount;
    private String perPayAmount;
    private String divPeriod;
    private String chargeMode;
    private String divRate;
    private String firstCharge;
    private String perCharge;

    public String getDivAmount() {
        return divAmount;
    }

    public void setDivAmount(String divAmount) {
        this.divAmount = divAmount;
    }

    public String getDivCharge() {
        return divCharge;
    }

    public void setDivCharge(String divCharge) {
        this.divCharge = divCharge;
    }

    public String getFirstPayAmount() {
        return firstPayAmount;
    }

    public void setFirstPayAmount(String firstPayAmount) {
        this.firstPayAmount = firstPayAmount;
    }

    public String getPerPayAmount() {
        return perPayAmount;
    }

    public void setPerPayAmount(String perPayAmount) {
        this.perPayAmount = perPayAmount;
    }

    public String getDivPeriod() {
        return divPeriod;
    }

    public void setDivPeriod(String divPeriod) {
        this.divPeriod = divPeriod;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getDivRate() {
        return divRate;
    }

    public void setDivRate(String divRate) {
        this.divRate = divRate;
    }

    public String getFirstCharge() {
        return firstCharge;
    }

    public void setFirstCharge(String firstCharge) {
        this.firstCharge = firstCharge;
    }

    public String getPerCharge() {
        return perCharge;
    }

    public void setPerCharge(String perCharge) {
        this.perCharge = perCharge;
    }

}
