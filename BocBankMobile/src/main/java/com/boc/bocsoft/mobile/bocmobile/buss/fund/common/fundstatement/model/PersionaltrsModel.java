package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model;

import java.io.Serializable;

/**
 * Created by huixiaobo on 2016/11/23.
 * 基金交易流水列表数据
 */
public class PersionaltrsModel implements Serializable{

    private String applyCount;
    private String applyDate;
    private String applyType;
    private String cashFlag;
    private String cashRemit;
    private String currency;
    private String endCount;
    private String endDate;
    private String endFlag;
    private String endSum;
    private String feeType;
    private String fundAccountNo;
    private String fundCode;
    private String fundConfirmAmount;
    private String fundConfirmCount;
    private String fundName;
    private String fundSeq;
    private String fundTranType;
    private String fundapplyCount;
    private String paymentDate;
    private String sellFlag;
    private String status;
    private String transCount;
    private String transCycle;
    private String transDate;
    private String trsStatus;

    public String getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(String applyCount) {
        this.applyCount = applyCount;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEndCount() {
        return endCount;
    }

    public void setEndCount(String endCount) {
        this.endCount = endCount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getEndSum() {
        return endSum;
    }

    public void setEndSum(String endSum) {
        this.endSum = endSum;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFundAccountNo() {
        return fundAccountNo;
    }

    public void setFundAccountNo(String fundAccountNo) {
        this.fundAccountNo = fundAccountNo;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundConfirmAmount() {
        return fundConfirmAmount;
    }

    public void setFundConfirmAmount(String fundConfirmAmount) {
        this.fundConfirmAmount = fundConfirmAmount;
    }

    public String getFundConfirmCount() {
        return fundConfirmCount;
    }

    public void setFundConfirmCount(String fundConfirmCount) {
        this.fundConfirmCount = fundConfirmCount;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getFundTranType() {
        return fundTranType;
    }

    public void setFundTranType(String fundTranType) {
        this.fundTranType = fundTranType;
    }

    public String getFundapplyCount() {
        return fundapplyCount;
    }

    public void setFundapplyCount(String fundapplyCount) {
        this.fundapplyCount = fundapplyCount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
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

    public String getTransCount() {
        return transCount;
    }

    public void setTransCount(String transCount) {
        this.transCount = transCount;
    }

    public String getTransCycle() {
        return transCycle;
    }

    public void setTransCycle(String transCycle) {
        this.transCycle = transCycle;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTrsStatus() {
        return trsStatus;
    }

    public void setTrsStatus(String trsStatus) {
        this.trsStatus = trsStatus;
    }

    @Override
    public String toString() {
        return "PsnPersionalTransDetailQueryResult{" +
                "applyCount='" + applyCount + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", applyType='" + applyType + '\'' +
                ", cashFlag='" + cashFlag + '\'' +
                ", cashRemit='" + cashRemit + '\'' +
                ", currency='" + currency + '\'' +
                ", endCount='" + endCount + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endFlag='" + endFlag + '\'' +
                ", endSum='" + endSum + '\'' +
                ", feeType='" + feeType + '\'' +
                ", fundAccountNo='" + fundAccountNo + '\'' +
                ", fundCode='" + fundCode + '\'' +
                ", fundConfirmAmount='" + fundConfirmAmount + '\'' +
                ", fundConfirmCount='" + fundConfirmCount + '\'' +
                ", fundName='" + fundName + '\'' +
                ", fundSeq='" + fundSeq + '\'' +
                ", fundTranType='" + fundTranType + '\'' +
                ", fundapplyCount='" + fundapplyCount + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", sellFlag='" + sellFlag + '\'' +
                ", status='" + status + '\'' +
                ", transCount='" + transCount + '\'' +
                ", transCycle='" + transCycle + '\'' +
                ", transDate='" + transDate + '\'' +
                ", trsStatus='" + trsStatus + '\'' +
                '}';
    }
}
