package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyPauseResume;

/**
 * Created by huixiaobo on 2016/11/18.
 * 056定期定额申购暂停/开通—上送参数
 */
public class PsnFundScheduledBuyPauseResumeParams {
    /**定投申请日期*/
    private String scheduleBuyDate;
    /**定投序号*/
    private String scheduleBuyNum;
    /**暂停/开通标识*/
    private String fundTransFlag;
    /**基金代码*/
    private String fundCode;
    /**防重机制token*/
    private String token;
    /**会话ID*/
    private String conversationId;

    public String getScheduleBuyDate() {
        return scheduleBuyDate;
    }

    public void setScheduleBuyDate(String scheduleBuyDate) {
        this.scheduleBuyDate = scheduleBuyDate;
    }

    public String getScheduleBuyNum() {
        return scheduleBuyNum;
    }

    public void setScheduleBuyNum(String scheduleBuyNum) {
        this.scheduleBuyNum = scheduleBuyNum;
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
        return "PsnFundScheduledBuyPauseResumeParams{" +
                "scheduleBuyDate='" + scheduleBuyDate + '\'' +
                ", scheduleBuyNum='" + scheduleBuyNum + '\'' +
                ", fundTransFlag='" + fundTransFlag + '\'' +
                ", fundCode='" + fundCode + '\'' +
                ", token='" + token + '\'' +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
