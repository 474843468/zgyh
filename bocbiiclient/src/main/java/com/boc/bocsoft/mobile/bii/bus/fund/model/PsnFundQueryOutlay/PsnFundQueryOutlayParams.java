package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay;

/**
 * 登录前基金行情查询-上送参数
 * Created by lxw on 2016/7/29 0029.
 */
public class PsnFundQueryOutlayParams {

    /**
     * company :
     * currencyCode : 001
     * currentIndex : 0
     * fundKind : 00
     * fundState : 0
     * fundType : 00
     * id : 196071715
     * pageSize : 10
     * riskGrade :
     */

    private String fundInfo;
    private String company;
    private String currencyCode;
    private String currentIndex;
    private String fundKind;
    private String fundState;
    private String fundType;
    private String pageSize;
    private String riskGrade;
    private String sortFlag;

    public String getFundInfo() {
        return fundInfo;
    }

    public void setFundInfo(String fundInfo) {
        this.fundInfo = fundInfo;
    }

    public String getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    private String sortField;


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getFundKind() {
        return fundKind;
    }

    public void setFundKind(String fundKind) {
        this.fundKind = fundKind;
    }

    public String getFundState() {
        return fundState;
    }

    public void setFundState(String fundState) {
        this.fundState = fundState;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }
}
