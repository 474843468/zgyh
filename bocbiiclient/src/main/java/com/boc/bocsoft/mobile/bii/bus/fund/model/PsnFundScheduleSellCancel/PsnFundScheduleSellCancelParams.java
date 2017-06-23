package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduleSellCancel;

import java.math.BigDecimal;

/**
 * Created by huixiaobo on 2016/11/18.
 * 040定期定额赎回撤销—上送参数
 */
public class PsnFundScheduleSellCancelParams {
    /**基金代码*/
    private String fundCode;
    /**原定期定额申请日期*/
    private String oldApplyDate;
    /**防重机制token*/
    private String token;
    /**原定期定额序号*/
    private String transSeq;
    /**定赎金额*/
    private BigDecimal eachAmount;
    /**会话ID*/
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

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

    public String getOldApplyDate() {
        return oldApplyDate;
    }

    public void setOldApplyDate(String oldApplyDate) {
        this.oldApplyDate = oldApplyDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTransSeq() {
        return transSeq;
    }

    public void setTransSeq(String transSeq) {
        this.transSeq = transSeq;
    }

    @Override
    public String toString() {
        return "PsnFundScheduleSellCancelParams{" +
                "fundCode='" + fundCode + '\'' +
                ", oldApplyDate='" + oldApplyDate + '\'' +
                ", token='" + token + '\'' +
                ", transSeq='" + transSeq + '\'' +
                ", eachAmount='" + eachAmount + '\'' +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
