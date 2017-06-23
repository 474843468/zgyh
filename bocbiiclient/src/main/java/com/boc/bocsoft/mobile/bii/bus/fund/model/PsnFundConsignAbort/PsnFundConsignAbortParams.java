package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConsignAbort;

/**
 * 021基金撤单-上送参数
 * Created by wy7105 on 2016/11/24.
 */
public class PsnFundConsignAbortParams {

    private String date; //交易日期
    private String fundAmount; //基金份额(金额)
    private String fundSeq; //基金交易序号
    private String fundCode; //基金代码
    private String nightFlag; //夜间交易标志
    private String originalTransCode; //基金原交易码
    private String token; //防重机制token
    private String conversationId; //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFundAmount(String fundAmount) {
        this.fundAmount = fundAmount;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setNightFlag(String nightFlag) {
        this.nightFlag = nightFlag;
    }

    public void setOriginalTransCode(String originalTransCode) {
        this.originalTransCode = originalTransCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDate() {
        return date;
    }

    public String getFundAmount() {
        return fundAmount;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getNightFlag() {
        return nightFlag;
    }

    public String getOriginalTransCode() {
        return originalTransCode;
    }

    public String getToken() {
        return token;
    }
}
