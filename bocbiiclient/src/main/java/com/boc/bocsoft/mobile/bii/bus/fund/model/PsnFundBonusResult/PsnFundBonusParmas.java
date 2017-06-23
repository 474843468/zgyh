package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBonusResult;

/**
 * Created by taoyongzhen on 2016/12/8.
 */

public class PsnFundBonusParmas {


    /**
     * fundCode : 220020
     * fundBonusType : 1
     * token : 123456
     */
    //基金编码
    private String fundCode;
    //分红方式0: 默认 1: 现金 2: 红利再投资
    private String fundBonusType;

    private String token;

    private String affirmFlag;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundBonusType() {
        return fundBonusType;
    }

    public void setFundBonusType(String fundBonusType) {
        this.fundBonusType = fundBonusType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAffirmFlag() {
        return affirmFlag;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }
}
