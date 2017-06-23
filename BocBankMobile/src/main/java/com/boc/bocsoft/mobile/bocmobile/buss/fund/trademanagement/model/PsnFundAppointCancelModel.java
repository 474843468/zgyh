package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model;

/**
 * Created by wy7105 on 2016/11/25.
 * 指定日期撤单model
 */
public class PsnFundAppointCancelModel {

    //请求上报数据

    private String assignedDate; //指定日期
    private String fundSeq; //基金交易序号
    private String fundCode; //基金代码
    private String originalTransCode; //基金原交易码
    private String token; //防重机制token
    private String conversationId; //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setOriginalTransCode(String originalTransCode) {
        this.originalTransCode = originalTransCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getOriginalTransCode() {
        return originalTransCode;
    }

    public String getToken() {
        return token;
    }


    //请求返回数据
    private String transactionId; //网银交易序号


    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public String getTransactionId() {
        return transactionId;
    }
}
