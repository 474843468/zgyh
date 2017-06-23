package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model;

/**
 * Created by huixiaobo on 2016/12/15.
 * 038定投修改确认上送参数
 */
public class FundScheduledBuyPms {
    /**基金代码*/
    private String fundCode;
    /**原定期定额序号*/
    private String transSeq;
    /**原定期定额申请日期*/
    private String oldFundPaymentDate;
    /**每次扣款金额*/
    private String applyAmount;
    /**扣款日期*/
    private String subDate;
    /**结束条件*/
    private String endFlag;
    /**指定结束日期*/
    private String fundPointendDate;
    /**指定结束累计次数*/
    private String endSum;
    /**指定结束累计金额*/
    private String fundPointendAmount;
    /**防重机制token*/
    private String token;
    /**定投周期*/
    private String transCycle;
    /**会话ID*/
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getTransSeq() {
        return transSeq;
    }

    public void setTransSeq(String transSeq) {
        this.transSeq = transSeq;
    }

    public String getOldFundPaymentDate() {
        return oldFundPaymentDate;
    }

    public void setOldFundPaymentDate(String oldFundPaymentDate) {
        this.oldFundPaymentDate = oldFundPaymentDate;
    }

    public String getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(String applyAmount) {
        this.applyAmount = applyAmount;
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

    public String getFundPointendDate() {
        return fundPointendDate;
    }

    public void setFundPointendDate(String fundPointendDate) {
        this.fundPointendDate = fundPointendDate;
    }

    public String getEndSum() {
        return endSum;
    }

    public void setEndSum(String endSum) {
        this.endSum = endSum;
    }

    public String getFundPointendAmount() {
        return fundPointendAmount;
    }

    public void setFundPointendAmount(String fundPointendAmount) {
        this.fundPointendAmount = fundPointendAmount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTransCycle() {
        return transCycle;
    }

    public void setTransCycle(String transCycle) {
        this.transCycle = transCycle;
    }

    @Override
    public String toString() {
        return "PsnFundScheduledBuyModifyParams{" +
                "fundCode='" + fundCode + '\'' +
                ", transSeq='" + transSeq + '\'' +
                ", oldFundPaymentDate='" + oldFundPaymentDate + '\'' +
                ", applyAmount='" + applyAmount + '\'' +
                ", subDate='" + subDate + '\'' +
                ", endFlag='" + endFlag + '\'' +
                ", fundPointendDate='" + fundPointendDate + '\'' +
                ", endSum='" + endSum + '\'' +
                ", fundPointendAmount='" + fundPointendAmount + '\'' +
                ", token='" + token + '\'' +
                ", transCycle='" + transCycle + '\'' +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
