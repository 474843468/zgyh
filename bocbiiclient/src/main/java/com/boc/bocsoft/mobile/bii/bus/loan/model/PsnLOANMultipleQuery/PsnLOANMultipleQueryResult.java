package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANMultipleQuery;

import java.math.BigDecimal;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PsnLOANMultipleQueryResult {

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;
    /**
     * 浮动比
     */
    private Object floatingRate;
    /**
     * 浮动值
     */
    private String floatingValue;
    /**
     * 质押率(人民币)
     */
    private String pledgeRate_ZH;
    /**
     * 质押率(美元)
     */
    private String pledgeRate_US;
    /**
     * 质押率(其他)
     */
    private String pledgeRate_OT;
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

    public BigDecimal getExchangeRate() { return exchangeRate;}

    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate;}

    public Object getFloatingRate() { return floatingRate;}

    public void setFloatingRate(Object floatingRate) { this.floatingRate = floatingRate;}

    public String getFloatingValue() { return floatingValue;}

    public void setFloatingValue(String floatingValue) { this.floatingValue = floatingValue;}

    public String getPledgeRate_ZH() { return pledgeRate_ZH;}

    public void setPledgeRate_ZH(String pledgeRate_ZH) { this.pledgeRate_ZH = pledgeRate_ZH;}

    public String getPledgeRate_US() { return pledgeRate_US;}

    public void setPledgeRate_US(String pledgeRate_US) { this.pledgeRate_US = pledgeRate_US;}

    public String getPledgeRate_OT() { return pledgeRate_OT;}

    public void setPledgeRate_OT(String pledgeRate_OT) { this.pledgeRate_OT = pledgeRate_OT;}

    public String getLoanPeriodMax() { return loanPeriodMax;}

    public void setLoanPeriodMax(String loanPeriodMax) { this.loanPeriodMax = loanPeriodMax;}

    public String getLoanPeriodMin() { return loanPeriodMin;}

    public void setLoanPeriodMin(String loanPeriodMin) { this.loanPeriodMin = loanPeriodMin;}

    public String getSingleQuotaMax() { return singleQuotaMax;}

    public void setSingleQuotaMax(String singleQuotaMax) { this.singleQuotaMax = singleQuotaMax;}

    public String getSingleQuotaMin() { return singleQuotaMin;}

    public void setSingleQuotaMin(String singleQuotaMin) { this.singleQuotaMin = singleQuotaMin;}
}
