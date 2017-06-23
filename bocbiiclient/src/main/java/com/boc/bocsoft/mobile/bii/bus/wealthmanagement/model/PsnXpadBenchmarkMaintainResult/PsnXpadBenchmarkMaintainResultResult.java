package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadBenchmarkMaintainResult;

/**
 * Created by guokai on 2016/10/9.
 */
public class PsnXpadBenchmarkMaintainResultResult {

    /**
     * transactionId : 1555949690
     * custAgrCode : 218091510000382
     * serialName : p606-xcp-p606-y001e
     * accno : 1028****9185
     * totalperiod : 2
     * period : 0
     * surplusperiod : 2
     * startperiod : null
     * endperiod : null
     * operdt :
     */

    private String transactionId;
    private String custAgrCode;
    private String serialName;
    private String accno;
    private String totalperiod;
    private String period;
    private String surplusperiod;
    private Object startperiod;
    private Object endperiod;
    private String operdt;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustAgrCode() {
        return custAgrCode;
    }

    public void setCustAgrCode(String custAgrCode) {
        this.custAgrCode = custAgrCode;
    }

    public String getSerialName() {
        return serialName;
    }

    public void setSerialName(String serialName) {
        this.serialName = serialName;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getTotalperiod() {
        return totalperiod;
    }

    public void setTotalperiod(String totalperiod) {
        this.totalperiod = totalperiod;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSurplusperiod() {
        return surplusperiod;
    }

    public void setSurplusperiod(String surplusperiod) {
        this.surplusperiod = surplusperiod;
    }

    public Object getStartperiod() {
        return startperiod;
    }

    public void setStartperiod(Object startperiod) {
        this.startperiod = startperiod;
    }

    public Object getEndperiod() {
        return endperiod;
    }

    public void setEndperiod(Object endperiod) {
        this.endperiod = endperiod;
    }

    public String getOperdt() {
        return operdt;
    }

    public void setOperdt(String operdt) {
        this.operdt = operdt;
    }
}
