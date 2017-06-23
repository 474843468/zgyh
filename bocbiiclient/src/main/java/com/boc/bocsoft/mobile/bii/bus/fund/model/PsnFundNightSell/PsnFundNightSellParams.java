package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightSell;

/**
 * Created by zcy7065 on 2016/12/5.
 */
public class PsnFundNightSellParams {

    /**
     * assignedDate : 指定日期
     * executeType : 执行方式 0：立即 1：指定日期
     * fundCode : 000011
     * sellAmount : 赎回份额
     * feeType : 收费方式 1：前收 2：后收
     * fundSellFlag : 连续赎回  0：取消赎回 1：顺延赎回
     * token : ccarucjw
     */
    private String assignedDate;
    private String executeType;
    private String fundCode;
    private String sellAmount;
    private String feeType;
    private String fundSellFlag;
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

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setSellAmount(String sellAmount) {
        this.sellAmount = sellAmount;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public void setFundSellFlag(String fundSellFlag) {
        this.fundSellFlag = fundSellFlag;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public String getExecuteType() {
        return executeType;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getSellAmount() {
        return sellAmount;
    }

    public String getFeeType() {
        return feeType;
    }

    public String getFundSellFlag() {
        return fundSellFlag;
    }

    public String getToken() {
        return token;
    }
}
