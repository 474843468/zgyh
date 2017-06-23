package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class PsnFundRiskEvaluationQueryResult {

    /**
     * 是否做过风险评估
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
