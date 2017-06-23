package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossFee;

import java.math.BigDecimal;

/**
 * 信用卡挂失及补卡手续费查询响应
 *
 * Created by liuweidong on 2016/6/13.
 */
public class PsnCrcdReportLossFeeResult {

    /**
     * 挂失手续费
     */
    private BigDecimal lossFee;
    /**
     * 挂失手续费币种
     */
    private String lossFeeCurrency;
    /**
     * 补卡手续费
     */
    private BigDecimal reportFee;
    /**
     * 试费查询是否成功标识位 1：查询成功0：查询失败
     */
    private String getChargeFlag;
    /**
     * 补卡手续费币种
     */
    private String reportFeeCurrency;

    public BigDecimal getLossFee() {
        return lossFee;
    }

    public void setLossFee(BigDecimal lossFee) {
        this.lossFee = lossFee;
    }

    public String getLossFeeCurrency() {
        return lossFeeCurrency;
    }

    public void setLossFeeCurrency(String lossFeeCurrency) {
        this.lossFeeCurrency = lossFeeCurrency;
    }

    public BigDecimal getReportFee() {
        return reportFee;
    }

    public void setReportFee(BigDecimal reportFee) {
        this.reportFee = reportFee;
    }

    public String getGetChargeFlag() {
        return getChargeFlag;
    }

    public void setGetChargeFlag(String getChargeFlag) {
        this.getChargeFlag = getChargeFlag;
    }

    public String getReportFeeCurrency() {
        return reportFeeCurrency;
    }

    public void setReportFeeCurrency(String reportFeeCurrency) {
        this.reportFeeCurrency = reportFeeCurrency;
    }
}
