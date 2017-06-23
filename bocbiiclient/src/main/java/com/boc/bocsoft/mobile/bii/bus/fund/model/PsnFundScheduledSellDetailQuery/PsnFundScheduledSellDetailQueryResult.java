package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellDetailQuery;

/**
 * Created by huixiaobo on 2016/11/18.
 * 055定赎申请明细查询—返回参数
 */
public class PsnFundScheduledSellDetailQueryResult {
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
    /**定赎份额*/
    private String sellCount;
    /**定赎周期*/
    private String sellCycle;
    /**定赎日期*/
    private String sellDate;
    /**结束条件*/
    private String endFlag;
    /**指定结束日期*/
    private String endDate;
    /**指定结束累计次数*/
    private String endSum;
    /**指定结束累计份额*/
    private String endCount;
    /**连续赎回标志*/
    private String sellFlag;
    /**状态*/
    private String status;
    /**累计扣款成功次数*/
    private String successCount;
    /**累计扣款失败次数*/
    private String failCount;
    /**累计扣款份额*/
    private String paymentCount;

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

    public String getSellCount() {
        return sellCount;
    }

    public void setSellCount(String sellCount) {
        this.sellCount = sellCount;
    }

    public String getSellCycle() {
        return sellCycle;
    }

    public void setSellCycle(String sellCycle) {
        this.sellCycle = sellCycle;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndSum() {
        return endSum;
    }

    public void setEndSum(String endSum) {
        this.endSum = endSum;
    }

    public String getEndCount() {
        return endCount;
    }

    public void setEndCount(String endCount) {
        this.endCount = endCount;
    }

    public String getSellFlag() {
        return sellFlag;
    }

    public void setSellFlag(String sellFlag) {
        this.sellFlag = sellFlag;
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

    public String getPaymentCount() {
        return paymentCount;
    }

    public void setPaymentCount(String paymentCount) {
        this.paymentCount = paymentCount;
    }

    @Override
    public String toString() {
        return "PsnFundScheduledSellDetailQueryResult{" +
                "fundCode='" + fundCode + '\'' +
                ", fundName='" + fundName + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", cashFlag='" + cashFlag + '\'' +
                ", feeType='" + feeType + '\'' +
                ", sellCount='" + sellCount + '\'' +
                ", sellCycle='" + sellCycle + '\'' +
                ", sellDate='" + sellDate + '\'' +
                ", endFlag='" + endFlag + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endSum='" + endSum + '\'' +
                ", endCount='" + endCount + '\'' +
                ", sellFlag='" + sellFlag + '\'' +
                ", status='" + status + '\'' +
                ", successCount='" + successCount + '\'' +
                ", failCount='" + failCount + '\'' +
                ", paymentCount='" + paymentCount + '\'' +
                '}';
    }
}
