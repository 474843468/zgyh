package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss;

/**
 * Created by taoyongzhen on 2016/11/30.
 */

public class PsnQueryFloatProfitAndLossResult {

    /**
     * fundCode : 163805
     * fundName : 中银策略
     * curceny : 001
     * startcost : 0.00
     * endCost : 2997.00
     * hsAmount : 0.00
     * trAmount : 3000.00
     * jyAmount : 3.00
     * middleFloat : -3.00
     * endFloat : -3.00
     * resultFloat : 0.00
     * cashFlag : CAS
     */

    private String fundCode;
    private String fundName;
    private String curceny;
    private String startcost;
    private String endCost;
    private String hsAmount;
    private String trAmount;
    private String jyAmount;
    private String middleFloat;
    private String endFloat;
    private String resultFloat;
    private String cashFlag;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getCurceny() {
        return curceny;
    }

    public void setCurceny(String curceny) {
        this.curceny = curceny;
    }

    public String getStartcost() {
        return startcost;
    }

    public void setStartcost(String startcost) {
        this.startcost = startcost;
    }

    public String getEndCost() {
        return endCost;
    }

    public void setEndCost(String endCost) {
        this.endCost = endCost;
    }

    public String getHsAmount() {
        return hsAmount;
    }

    public void setHsAmount(String hsAmount) {
        this.hsAmount = hsAmount;
    }

    public String getTrAmount() {
        return trAmount;
    }

    public void setTrAmount(String trAmount) {
        this.trAmount = trAmount;
    }

    public String getJyAmount() {
        return jyAmount;
    }

    public void setJyAmount(String jyAmount) {
        this.jyAmount = jyAmount;
    }

    public String getMiddleFloat() {
        return middleFloat;
    }

    public void setMiddleFloat(String middleFloat) {
        this.middleFloat = middleFloat;
    }

    public String getEndFloat() {
        return endFloat;
    }

    public void setEndFloat(String endFloat) {
        this.endFloat = endFloat;
    }

    public String getResultFloat() {
        return resultFloat;
    }

    public void setResultFloat(String resultFloat) {
        this.resultFloat = resultFloat;
    }

    public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }
}
