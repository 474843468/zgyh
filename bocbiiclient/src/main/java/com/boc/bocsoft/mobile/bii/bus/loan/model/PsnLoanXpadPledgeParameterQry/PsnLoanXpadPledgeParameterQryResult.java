package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeParameterQry;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PsnLoanXpadPledgeParameterQryResult {

    /**
     * 贷款品种
     * FIN-LOAN 理财产品融资便利
     */
    private String loanType;
    /**
     * 贷款期限上限
     */
    private String loanPeriodMax;
    /**
     * 贷款期限下限
     */
    private String loanPeriodMin;
    /**
     * 单笔限额上限
     */
    private String singleQuotaMax;
    /**
     * 单笔限额下限
     */
    private String singleQuotaMin;
    /**
     * 当日剩余可用金额
     */
    private String availableAmtToday;
    /**
     * 浮动比
     */
    private String floatingRate;
    /**
     * 浮动值
     */
    private String floatingValue;

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanPeriodMax() {
        return loanPeriodMax;
    }

    public void setLoanPeriodMax(String loanPeriodMax) {
        this.loanPeriodMax = loanPeriodMax;
    }

    public String getLoanPeriodMin() {
        return loanPeriodMin;
    }

    public void setLoanPeriodMin(String loanPeriodMin) {
        this.loanPeriodMin = loanPeriodMin;
    }

    public String getSingleQuotaMax() {
        return singleQuotaMax;
    }

    public void setSingleQuotaMax(String singleQuotaMax) {
        this.singleQuotaMax = singleQuotaMax;
    }

    public String getSingleQuotaMin() {
        return singleQuotaMin;
    }

    public void setSingleQuotaMin(String singleQuotaMin) {
        this.singleQuotaMin = singleQuotaMin;
    }

    public String getAvailableAmtToday() {
        return availableAmtToday;
    }

    public void setAvailableAmtToday(String availableAmtToday) {
        this.availableAmtToday = availableAmtToday;
    }

    public String getFloatingRate() {
        return floatingRate;
    }

    public void setFloatingRate(String floatingRate) {
        this.floatingRate = floatingRate;
    }

    public String getFloatingValue() {
        return floatingValue;
    }

    public void setFloatingValue(String floatingValue) {
        this.floatingValue = floatingValue;
    }
}
