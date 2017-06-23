package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PnsXpadInvestAgreementModifyVerify;

/**
 * 智能投资协议修改预交易
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadInvestAgreementModifyVerifyResult {
    private String accNo;//	银行帐号
    private String agrCode;//	产品协议
    private String agrName;//	协议名称
    private String periodPur;//	购买周期
    private String firstDatePur;//	下次购买日
    private String periodRed;//	赎回周期
    private String firstDateRed;//	下次赎回日
    private String amount;//	单期购买金额/单次赎回金额
    private String minAmount;//	账户保留余额
    private String maxAmount;//	最大购买金额
    private String periodAgr;//	协议周期
    private String isControl;//	是否不限期
    private String totalPeriod;//	签约期数
    private String endDate;//	协议到期日
    private String lastDays;//	协议总持续天数
    private String willPurCount;//	预计剩余购买次数
    private String willRedCount;//	预计剩余赎回次数
    private String eachpbalDays;//	每期持有天数
    private String oneTmredAmt;//	单次赎回金额
    private String failMax;//	失败次数上限
    private String isNeedPur;//	购买选项
    private String isNeedRed;//	赎回选项


    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getAgrName() {
        return agrName;
    }

    public void setAgrName(String agrName) {
        this.agrName = agrName;
    }

    public String getPeriodPur() {
        return periodPur;
    }

    public void setPeriodPur(String periodPur) {
        this.periodPur = periodPur;
    }

    public String getFirstDatePur() {
        return firstDatePur;
    }

    public void setFirstDatePur(String firstDatePur) {
        this.firstDatePur = firstDatePur;
    }

    public String getPeriodRed() {
        return periodRed;
    }

    public void setPeriodRed(String periodRed) {
        this.periodRed = periodRed;
    }

    public String getFirstDateRed() {
        return firstDateRed;
    }

    public void setFirstDateRed(String firstDateRed) {
        this.firstDateRed = firstDateRed;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getPeriodAgr() {
        return periodAgr;
    }

    public void setPeriodAgr(String periodAgr) {
        this.periodAgr = periodAgr;
    }

    public String getIsControl() {
        return isControl;
    }

    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLastDays() {
        return lastDays;
    }

    public void setLastDays(String lastDays) {
        this.lastDays = lastDays;
    }

    public String getWillPurCount() {
        return willPurCount;
    }

    public void setWillPurCount(String willPurCount) {
        this.willPurCount = willPurCount;
    }

    public String getWillRedCount() {
        return willRedCount;
    }

    public void setWillRedCount(String willRedCount) {
        this.willRedCount = willRedCount;
    }

    public String getEachpbalDays() {
        return eachpbalDays;
    }

    public void setEachpbalDays(String eachpbalDays) {
        this.eachpbalDays = eachpbalDays;
    }

    public String getOneTmredAmt() {
        return oneTmredAmt;
    }

    public void setOneTmredAmt(String oneTmredAmt) {
        this.oneTmredAmt = oneTmredAmt;
    }

    public String getFailMax() {
        return failMax;
    }

    public void setFailMax(String failMax) {
        this.failMax = failMax;
    }

    public String getIsNeedPur() {
        return isNeedPur;
    }

    public void setIsNeedPur(String isNeedPur) {
        this.isNeedPur = isNeedPur;
    }

    public String getIsNeedRed() {
        return isNeedRed;
    }

    public void setIsNeedRed(String isNeedRed) {
        this.isNeedRed = isNeedRed;
    }
}
