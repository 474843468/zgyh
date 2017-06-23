package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationSubmit;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class PsnFundRiskEvaluationSubmitResult {

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
