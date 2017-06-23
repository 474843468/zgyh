package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

/**
 * 由002接口 PsnFundRiskEvaluationQueryResult 查询风险评估等级
 * 上送参数：空
 * 返回参数：riskLevel，evaluationDate
 * Created by lyf7084 on 2016/12/13.
 */
public class RiskEvaluationQueryResModel {
    /**
     * 是否做过风险评估
     */
    private boolean isEvaluated;

    /**
     * 风险等级
     */
    private String riskLevel;

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }

    public void setEvaluated(boolean evaluated) {
        isEvaluated = evaluated;
    }


}
