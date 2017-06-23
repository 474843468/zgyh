package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBuy;

/**
 * Created by zcy7065 on 2016/11/23.
 */
public class PsnFundNightBuyResult {

    /**
     * isSignedFundContract : 是否签署电子签名合同 0：未签署 1：签署
     * fundSeq :
     * affirmFlag : 客户是否确认风险标志
     * isSignedFundEval : 是否做过风险评估
     * tranState : 交易状态  1:成功  E146:未签署约定书  E135:未做风险评估  E142:未签署合同 E136:风险级别不匹配
     * isMatchEval : 风险评估结果是否匹配
     * isSignedFundStipulation : 是否签署电子签名约定书（纸质？）
     * consignSeq : 1
     * transactionId : 交易流水号
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
