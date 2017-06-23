package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeRateQry;

/**
 * 作者：XieDu
 * 创建时间：2016/10/25 11:24
 * 描述：
 */
public class PsnLoanXpadPledgeRateQryParams {

    /**
     * 浮动比
     * "利率增量选项”字段为2时，此字段有效
     */
    private String floatingRate;
    /**
     * 浮动值
     * “利率增量选项”字段为1时，此字段有效
     */
    private String floatingValue;
    /**
     * 利率增量选项。
     * 有效取值：1-浮动值；2-浮动比。
     */
    private String rateIncrOpt;
    /**
     * 贷款期限	单位为“月”
     */
    private String loanPeriod;

    public String getFloatingValue() { return floatingValue;}

    public void setFloatingValue(String floatingValue) { this.floatingValue = floatingValue;}

    public String getRateIncrOpt() { return rateIncrOpt;}

    public void setRateIncrOpt(String rateIncrOpt) { this.rateIncrOpt = rateIncrOpt;}

    public String getLoanPeriod() { return loanPeriod;}

    public void setLoanPeriod(String loanPeriod) { this.loanPeriod = loanPeriod;}

    public String getFloatingRate() {
        return floatingRate;
    }

    public void setFloatingRate(String floatingRate) {
        this.floatingRate = floatingRate;
    }
}
