package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProfitCount;

/**
 * 收益试算-请求
 * Created by liuweidong on 2016/10/22.
 */
public class PsnXpadProfitCountParams {
    private String proId;// 产品代码
    private String opt;// 操作类型 0：查询产品信息1：产品收益试算（默认为1）
    private String exyield;// 预计年收益率（%）
    private String dayTerm;// 产品期限（天数）
    private String puramt;// 投资金额
    private String totalPeriod;// 购买期数 周期性产品输入，-1(即输入-000001)代表不限期，表示续到产品系列结束，否则必须输入大于零的整数
    private String commonExyield;
    private String commonDayTerm;
    private String commonPurAmt;

    public void setProId(String proId) {
        this.proId = proId;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public void setExyield(String exyield) {
        this.exyield = exyield;
    }

    public void setDayTerm(String dayTerm) {
        this.dayTerm = dayTerm;
    }

    public void setPuramt(String puramt) {
        this.puramt = puramt;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getProId() {
        return proId;
    }

    public String getOpt() {
        return opt;
    }

    public String getExyield() {
        return exyield;
    }

    public String getDayTerm() {
        return dayTerm;
    }

    public String getPuramt() {
        return puramt;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public String getCommonExyield() {
        return commonExyield;
    }

    public void setCommonExyield(String commonExyield) {
        this.commonExyield = commonExyield;
    }

    public String getCommonDayTerm() {
        return commonDayTerm;
    }

    public void setCommonDayTerm(String commonDayTerm) {
        this.commonDayTerm = commonDayTerm;
    }

    public String getCommonPurAmt() {
        return commonPurAmt;
    }

    public void setCommonPurAmt(String commonPurAmt) {
        this.commonPurAmt = commonPurAmt;
    }
}
