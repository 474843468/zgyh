package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationSubmit;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class PsnFundRiskEvaluationSubmitParams {

    /**
     * riskScore : 20
     * token : 4saj9oup
     */

    private String riskScore;
    private String riskAnswer;
    private String conversationId;
    private String token;

    public String getRiskAnswer() {
        return riskAnswer;
    }

    public void setRiskAnswer(String riskAnswer) {
        this.riskAnswer = riskAnswer;
    }

    public String getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(String riskScore) {
        this.riskScore = riskScore;
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

}
