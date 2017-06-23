package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBuy;

/**
 * Created by zcy7065 on 2016/11/23.
 */
public class PsnFundNightBuyParams {

    /**
     * assignedDate :
     * buyAmount : 买入金额
     * fundCode : 基金代码
     * affirmFlag : 是否确认风险不匹配 Y：是 N：否
     * feetype : 收费方式 1：前收 2：后收
     * token : 防重标识
     */
    private String assignedDate;
    private String buyAmount;
    private String fundCode;
    private String affirmFlag;
    private String feetype;
    private String token;
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getAffirmFlag() {
        return affirmFlag;
    }

    public String getFeetype() {
        return feetype;
    }

    public String getToken() {
        return token;
    }
}
