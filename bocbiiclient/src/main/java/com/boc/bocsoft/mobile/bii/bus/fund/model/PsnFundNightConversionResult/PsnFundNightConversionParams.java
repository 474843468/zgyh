package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightConversionResult;

/**
 * Created by taoyonzhen on 2016/12/14.
 */

public class PsnFundNightConversionParams {


    /**
     * fromFundCode : 220020
     * toFundCode : 220020
     * feeType : 1
     * amount : 1
     * sellFlag : 0
     * token : 123456
     */

    private String fromFundCode;
    private String toFundCode;
    private String feeType;
    private String amount;
    private String sellFlag;
    private String token;
    private String conversationId;
    private String affirmFlag;
    public String getAffirmFlag() {
        return affirmFlag;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }


    String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFromFundCode() {
        return fromFundCode;
    }

    public void setFromFundCode(String fromFundCode) {
        this.fromFundCode = fromFundCode;
    }

    public String getToFundCode() {
        return toFundCode;
    }

    public void setToFundCode(String toFundCode) {
        this.toFundCode = toFundCode;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSellFlag() {
        return sellFlag;
    }

    public void setSellFlag(String sellFlag) {
        this.sellFlag = sellFlag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
