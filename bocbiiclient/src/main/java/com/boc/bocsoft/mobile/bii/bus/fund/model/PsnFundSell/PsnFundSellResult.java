package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSell;

/**
 * Created by zcy7065 on 2016/12/4.
 */
public class PsnFundSellResult {

    /**
     * isSignedFundContract : null
     * fundSeq : 100010001000 基金交易流水号
     * affirmFlag : null
     * isSignedFundEval : null
     * tranState : 交易状态 1：成功 9000：非工作时间
     * isMatchEval :
     * isSignedFundStipulation : null
     * consignSeq : null
     * transactionId : 20011062900001 交易流水号
     */
    private String isSignedFundContract;
    private String fundSeq;
    private String affirmFlag;
    private String isSignedFundEval;
    private String tranState;
    private String isMatchEval;
    private String isSignedFundStipulation;
    private String consignSeq;
    private String transactionId;

    public void setIsSignedFundContract(String isSignedFundContract) {
        this.isSignedFundContract = isSignedFundContract;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }

    public void setIsSignedFundEval(String isSignedFundEval) {
        this.isSignedFundEval = isSignedFundEval;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    public void setIsMatchEval(String isMatchEval) {
        this.isMatchEval = isMatchEval;
    }

    public void setIsSignedFundStipulation(String isSignedFundStipulation) {
        this.isSignedFundStipulation = isSignedFundStipulation;
    }

    public void setConsignSeq(String consignSeq) {
        this.consignSeq = consignSeq;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getIsSignedFundContract() {
        return isSignedFundContract;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public String getAffirmFlag() {
        return affirmFlag;
    }

    public String getIsSignedFundEval() {
        return isSignedFundEval;
    }

    public String getTranState() {
        return tranState;
    }

    public String getIsMatchEval() {
        return isMatchEval;
    }

    public String getIsSignedFundStipulation() {
        return isSignedFundStipulation;
    }

    public String getConsignSeq() {
        return consignSeq;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
