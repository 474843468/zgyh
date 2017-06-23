package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightConversionResult;

/**
 * Created by taoyongzhen on 2016/12/14.
 */

public class PsnFundNightConversionResult {


    /**
     * fundCode :
     * fundName :
     * canBuy : false
     * tranState : 1
     * transactionId
     fundSeq
     feeType
     isMatchEval
     affirmFlag

     */

    private String fundCode;
    private String fundName;
    private String canBuy;
    private String tranState;

    private String transactionId;
    private String fundSeq;
    private String feeType;
    private String isMatchEval;
    private String affirmFlag;

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

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getIsMatchEval() {
        return isMatchEval;
    }

    public void setIsMatchEval(String isMatchEval) {
        this.isMatchEval = isMatchEval;
    }

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

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getCanBuy() {
        return canBuy;
    }

    public void setCanBuy(String canBuy) {
        this.canBuy = canBuy;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }
}
