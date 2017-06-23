package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery;

/**
 * 产品查询与购买请求
 * Created by liuweidong on 2016/9/13.
 */
public class PsnXpadProductListQueryParams {
    private String productCode;
    private String accountId;
    private String productRiskType;
    private String bancAccountNo;
    private String productCurCode;
    private String xpadStatus;
    private String prodTimeLimit;
    private String productKind;
    private String proRisk;
    private String issueType;
    private String dayTerm;
    private String isLockPeriod;
    private String sortType;
    private String sortFlag;
    private String pageSize;
    private String _refresh;
    private String currentIndex;
    private String conversationId;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProductRiskType() {
        return productRiskType;
    }

    public void setProductRiskType(String productRiskType) {
        this.productRiskType = productRiskType;
    }

    public String getBancAccountNo() {
        return bancAccountNo;
    }

    public void setBancAccountNo(String bancAccountNo) {
        this.bancAccountNo = bancAccountNo;
    }

    public String getProductCurCode() {
        return productCurCode;
    }

    public void setProductCurCode(String productCurCode) {
        this.productCurCode = productCurCode;
    }

    public String getXpadStatus() {
        return xpadStatus;
    }

    public void setXpadStatus(String xpadStatus) {
        this.xpadStatus = xpadStatus;
    }

    public String getProdTimeLimit() {
        return prodTimeLimit;
    }

    public void setProdTimeLimit(String prodTimeLimit) {
        this.prodTimeLimit = prodTimeLimit;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public String getProRisk() {
        return proRisk;
    }

    public void setProRisk(String proRisk) {
        this.proRisk = proRisk;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getDayTerm() {
        return dayTerm;
    }

    public void setDayTerm(String dayTerm) {
        this.dayTerm = dayTerm;
    }

    public String getIsLockPeriod() {
        return isLockPeriod;
    }

    public void setIsLockPeriod(String isLockPeriod) {
        this.isLockPeriod = isLockPeriod;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
