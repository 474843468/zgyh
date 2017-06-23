package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.model;

/**
 * Created by taoyongzhen on 2016/12/20.
 */

public class PsnFundScheduleBuyModel {
    //
    private String fundPointendDate;
    private String endSum;
    private String fundPointendAmount;

    private String fundCode;
    private String feetype;
    private int sellAmount;
    private String fundSellFlag;
    private String token;
    private String eachAmount;
    private String dealCode;
    private String affirmFlag;

    public String getAffirmFlag() {
        return affirmFlag;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public int getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(int sellAmount) {
        this.sellAmount = sellAmount;
    }

    public String getFundSellFlag() {
        return fundSellFlag;
    }

    public void setFundSellFlag(String fundSellFlag) {
        this.fundSellFlag = fundSellFlag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEachAmount() {
        return eachAmount;
    }

    public void setEachAmount(String eachAmount) {
        this.eachAmount = eachAmount;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }


    public String getFundPointendAmount() {
        return fundPointendAmount;
    }

    public void setFundPointendAmount(String fundPointendAmount) {
        this.fundPointendAmount = fundPointendAmount;
    }

    public String getEndSum() {
        return endSum;
    }

    public void setEndSum(String endSum) {
        this.endSum = endSum;
    }

    public String getFundPointendDate() {
        return fundPointendDate;
    }

    public void setFundPointendDate(String fundPointendDate) {
        this.fundPointendDate = fundPointendDate;
    }
    //
    private String transactionId;
    private String fundSeq;
    private String dtFlag;
    private String subDate;
    private String endFlag;
    private String tranState;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getDtFlag() {
        return dtFlag;
    }

    public void setDtFlag(String dtFlag) {
        this.dtFlag = dtFlag;
    }

    public String getSubDate() {
        return subDate;
    }

    public void setSubDate(String subDate) {
        this.subDate = subDate;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }
}
