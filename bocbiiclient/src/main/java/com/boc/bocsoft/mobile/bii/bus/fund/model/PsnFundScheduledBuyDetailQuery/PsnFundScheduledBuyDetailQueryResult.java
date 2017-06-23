package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyDetailQuery;

/**
 * Created by huixiaobo on 2016/11/17.
 * 054定投申请明细查询-返回参数
 */
public class PsnFundScheduledBuyDetailQueryResult {
    /**基金代码*/
    private String fundCode;
    /**基金名称*/
    private String fundName;
    /**货币码*/
    private String currencyCode;
    /**钞汇标识*/
    private String cashFlag;
    /**收费方式*/
    private String feeType;
    /**定投金额*/
    private String applyAmount;
    /**定投周期*/
    private String applyCycle;
    /**扣款日期*/
    private String paymentDate;
    /**结束条件*/
    private String endFlag;
//    /**指定结束日期*/
//    private String endDate;
    /**指定结束累计次数*/
    private String endSum;
    /**指定结束累计金额*/
    private String endAmt;
    /**状态*/
    private String status;
    /**累计扣款成功次数*/
    private String successCount;
    /**累计扣款失败次数*/
    private String failCount;
    /**累计扣款金额*/
    private String paymentAmount;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(String applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getApplyCycle() {
        return applyCycle;
    }

    public void setApplyCycle(String applyCycle) {
        this.applyCycle = applyCycle;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

//    public String getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(String endDate) {
//        this.endDate = endDate;
//    }

    public String getEndSum() {
        return endSum;
    }

    public void setEndSum(String endSum) {
        this.endSum = endSum;
    }

    public String getEndAmt() {
        return endAmt;
    }

    public void setEndAmt(String endAmt) {
        this.endAmt = endAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(String successCount) {
        this.successCount = successCount;
    }

    public String getFailCount() {
        return failCount;
    }

    public void setFailCount(String failCount) {
        this.failCount = failCount;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public String toString() {
        return "PsnFundScheduledBuyDetailQueryResult{" +
                "fundCode='" + fundCode + '\'' +
                ", fundName='" + fundName + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", cashFlag='" + cashFlag + '\'' +
                ", feeType='" + feeType + '\'' +
                ", applyAmount='" + applyAmount + '\'' +
                ", applyCycle='" + applyCycle + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", endFlag='" + endFlag + '\'' +
                ", endSum='" + endSum + '\'' +
                ", endAmt='" + endAmt + '\'' +
                ", status='" + status + '\'' +
                ", successCount='" + successCount + '\'' +
                ", failCount='" + failCount + '\'' +
                ", paymentAmount='" + paymentAmount + '\'' +
                '}';
    }
}
