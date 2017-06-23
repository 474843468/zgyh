package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model;

import java.math.BigDecimal;

/**
 * Created by huixiaobo on 2016/11/30.
 * 定投撤单上传参数model
 */
public class InvestCldParamsModel {
    /**基金代码*/
    private String fundCode;
    /**防重机制token*/
    private String token;
    /**原定期定额申请日期*/
    private String oldApplyDate;
    /**原定期定额序号*/
    private String transSeq;
    /**撤销金额*/
    private BigDecimal eachAmount;
    /**会话ID*/
    private String conversationId;

    public BigDecimal getEachAmount() {
        return eachAmount;
    }

    public void setEachAmount(BigDecimal eachAmount) {
        this.eachAmount = eachAmount;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOldApplyDate() {
        return oldApplyDate;
    }

    public void setOldApplyDate(String oldApplyDate) {
        this.oldApplyDate = oldApplyDate;
    }

    public String getTransSeq() {
        return transSeq;
    }

    public void setTransSeq(String transSeq) {
        this.transSeq = transSeq;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "PsnFundDdAbortParams{" +
                "fundCode='" + fundCode + '\'' +
                ", token='" + token + '\'' +
                ", oldApplyDate='" + oldApplyDate + '\'' +
                ", transSeq='" + transSeq + '\'' +
                ", eachAmount=" + eachAmount +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
