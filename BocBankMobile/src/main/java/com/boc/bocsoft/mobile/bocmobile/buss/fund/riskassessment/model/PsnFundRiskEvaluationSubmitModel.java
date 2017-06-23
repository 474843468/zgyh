package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class PsnFundRiskEvaluationSubmitModel {
    //上报参数
    private String riskScore;
    private String token;

    public String getRiskAnswer() {
        return riskAnswer;
    }

    public void setRiskAnswer(String riskAnswer) {
        this.riskAnswer = riskAnswer;
    }

    private String riskAnswer;

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

    //返回参数
    /**
     * evaluationDate : 2017/09/22
     * riskLevel : 1
     */
    //评价日期
    private String evaluationDate;
    //评价等级
    private String riskLevel;

    public String getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(String evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
