package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model;

/**
 * Created by huixiaobo  on 2016/12/1.
 * 057定赎暂停恢复上传参数
 */
public class RedeemParams {
    /**定赎申请日期*/
    private String scheduleSellDate;
    /**定赎序号*/
    private String scheduleSellNum;
    /**暂停/开通标识*/
    private String fundTransFlag;
    /**基金代码*/
    private String fundCode;
    /** 防重机制token*/
    private String token;
    /**会话ID*/
    private String conversationId;

    public String getScheduleSellDate() {
        return scheduleSellDate;
    }

    public void setScheduleSellDate(String scheduleSellDate) {
        this.scheduleSellDate = scheduleSellDate;
    }

    public String getScheduleSellNum() {
        return scheduleSellNum;
    }

    public void setScheduleSellNum(String scheduleSellNum) {
        this.scheduleSellNum = scheduleSellNum;
    }

    public String getFundTransFlag() {
        return fundTransFlag;
    }

    public void setFundTransFlag(String fundTransFlag) {
        this.fundTransFlag = fundTransFlag;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "PsnFundScheduledSellPauseResumeParams{" +
                "scheduleSellDate='" + scheduleSellDate + '\'' +
                ", scheduleSellNum='" + scheduleSellNum + '\'' +
                ", fundTransFlag='" + fundTransFlag + '\'' +
                ", fundCode='" + fundCode + '\'' +
                ", token='" + token + '\'' +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}

