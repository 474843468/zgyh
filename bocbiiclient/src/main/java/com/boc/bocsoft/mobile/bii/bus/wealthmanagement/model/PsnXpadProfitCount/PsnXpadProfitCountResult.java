package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProfitCount;

import java.math.BigDecimal;

/**
 * 收益试算-响应
 * Created by liuweidong on 2016/10/22.
 */
public class PsnXpadProfitCountResult {
    private double commonExyield;
    private String issueKind;
    private String proid;
    private String pronam;
    private String procur;
    private String intsdate;
    private String edate;
    private String ispre;
    private String dayterm;
    private String totalperiod;
    private String couponbasis;
    private double exyield;
    private double puramt;
    private BigDecimal expprofit;// 试算收益

    public double getCommonExyield() {
        return commonExyield;
    }

    public void setCommonExyield(double commonExyield) {
        this.commonExyield = commonExyield;
    }

    public String getIssueKind() {
        return issueKind;
    }

    public void setIssueKind(String issueKind) {
        this.issueKind = issueKind;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getPronam() {
        return pronam;
    }

    public void setPronam(String pronam) {
        this.pronam = pronam;
    }

    public String getProcur() {
        return procur;
    }

    public void setProcur(String procur) {
        this.procur = procur;
    }

    public String getIntsdate() {
        return intsdate;
    }

    public void setIntsdate(String intsdate) {
        this.intsdate = intsdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getIspre() {
        return ispre;
    }

    public void setIspre(String ispre) {
        this.ispre = ispre;
    }

    public String getDayterm() {
        return dayterm;
    }

    public void setDayterm(String dayterm) {
        this.dayterm = dayterm;
    }

    public String getTotalperiod() {
        return totalperiod;
    }

    public void setTotalperiod(String totalperiod) {
        this.totalperiod = totalperiod;
    }

    public String getCouponbasis() {
        return couponbasis;
    }

    public void setCouponbasis(String couponbasis) {
        this.couponbasis = couponbasis;
    }

    public double getExyield() {
        return exyield;
    }

    public void setExyield(double exyield) {
        this.exyield = exyield;
    }

    public double getPuramt() {
        return puramt;
    }

    public void setPuramt(double puramt) {
        this.puramt = puramt;
    }

    public BigDecimal getExpprofit() {
        return expprofit;
    }

    public void setExpprofit(BigDecimal expprofit) {
        this.expprofit = expprofit;
    }
}
