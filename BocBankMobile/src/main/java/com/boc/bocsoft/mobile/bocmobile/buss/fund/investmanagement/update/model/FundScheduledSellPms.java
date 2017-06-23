package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model;

/**
 * Created by huixiaobo on 2016/12/15.
 * 039定赎修改接口上送参数
 */
public class FundScheduledSellPms {
    /**基金代码*/
    private String fundCode;
    /**原定期定额申请日期*/
    private String oldFundPaymentDate;
    /**赎回份额*/
    private String fundSellAmount;
    /***/
    private String fundSellFlag;
    /**防重机制token*/
    private String token;
    /**赎回日期*/
    private String subDate;
    /**结束条件*/
    private String endFlag;
    /**指定结束日期*/
    private String fundPointendDate;
    /**指定结束累计次数*/
    private String endSum;
    /**指定结束累计金额*/
    private String fundPointendAmount;
    /**原定期定额序号*/
    private String fundSeq;
    /**定赎周期*/
    private String dsFlag;
    /**会话ID*/
    private String conversationId;

    public String getFundSellFlag() {
        return fundSellFlag;
    }

    public void setFundSellFlag(String fundSellFlag) {
        this.fundSellFlag = fundSellFlag;
    }

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

    public String getOldFundPaymentDate() {
        return oldFundPaymentDate;
    }

    public void setOldFundPaymentDate(String oldFundPaymentDate) {
        this.oldFundPaymentDate = oldFundPaymentDate;
    }

    public String getFundSellAmount() {
        return fundSellAmount;
    }

    public void setFundSellAmount(String fundSellAmount) {
        this.fundSellAmount = fundSellAmount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getDsFlag() {
        return dsFlag;
    }

    public void setDsFlag(String dsFlag) {
        this.dsFlag = dsFlag;
    }

    @Override
    public String toString() {
        return "PsnFundScheduledSellModifyParams{" +
                "fundCode='" + fundCode + '\'' +
                ", oldFundPaymentDate='" + oldFundPaymentDate + '\'' +
                ", fundSellAmount='" + fundSellAmount + '\'' +
                ", token='" + token + '\'' +
                ", subDate='" + subDate + '\'' +
                ", endFlag='" + endFlag + '\'' +
                ", fundPointendDate='" + fundPointendDate + '\'' +
                ", endSum='" + endSum + '\'' +
                ", fundPointendAmount='" + fundPointendAmount + '\'' +
                ", fundSeq='" + fundSeq + '\'' +
                ", dsFlag='" + dsFlag + '\'' +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
