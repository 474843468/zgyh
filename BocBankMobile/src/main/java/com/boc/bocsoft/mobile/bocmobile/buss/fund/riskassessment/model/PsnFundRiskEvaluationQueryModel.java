package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class PsnFundRiskEvaluationQueryModel {

    /**
     * 是否评估过
     */
    private boolean isEvaluated;
    /**
     * 风险等级
     */
    private String riskLevel;
    /**
     * 评估日期
     */
    private String evaluationDate;

    public String getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(String evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }

    public void setIsEvaluated(boolean isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
