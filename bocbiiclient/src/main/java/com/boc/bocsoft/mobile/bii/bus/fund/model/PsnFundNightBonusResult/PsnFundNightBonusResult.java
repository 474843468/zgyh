package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBonusResult;

/**
 * Created by taoyongzhen on 2016/12/8.
 */

public class PsnFundNightBonusResult {
    private String affirmFlag;

    private boolean canBuy;

    private String consignSeq;

    private String feeType;

    private String fundCode;

    private String fundName;
    private String fundSeq;

    private String isMatchEval;

    private String isSignedFundEval;

    private String tranState;

    private long transactionId;

    public String getAffirmFlag() {
        return affirmFlag;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }

    public boolean isCanBuy() {
        return canBuy;
    }

    public void setCanBuy(boolean canBuy) {
        this.canBuy = canBuy;
    }

    public String getConsignSeq() {
        return consignSeq;
    }

    public void setConsignSeq(String consignSeq) {
        this.consignSeq = consignSeq;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
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

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getIsMatchEval() {
        return isMatchEval;
    }

    public void setIsMatchEval(String isMatchEval) {
        this.isMatchEval = isMatchEval;
    }

    public String getIsSignedFundEval() {
        return isSignedFundEval;
    }

    public void setIsSignedFundEval(String isSignedFundEval) {
        this.isSignedFundEval = isSignedFundEval;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }
}
