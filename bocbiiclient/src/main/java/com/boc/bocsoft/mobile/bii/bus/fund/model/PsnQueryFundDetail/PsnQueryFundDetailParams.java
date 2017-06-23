package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail;

/**
 * 基金信息查询（查询基金行情）上送参数
 * Created by Administrator on 2016/8/4 0004.
 */
public class PsnQueryFundDetailParams {


    /**
     * fundInfo : 05
     * company : 50050000
     * currencyCode :
     * fundType :
     * riskGrade :
     */

    // 基金代码或名称
    private String fundInfo;
    // 基金公司代码
    private String company;
    // 交易币种
    private String currencyCode;
    // 产品种类
    private String fundKind;
    // 产品类型
    private String fundType;
    // 风险等级
    private String riskGrade;
    // 基金状态
    private String fundState;
    // 排序方式
    private String sortFlag;
    // 排序字段
    private String sortField;
    // 页面显示记录条数
    private String pageSize;

    // 当前页起始索引
    private String currentIndex;

    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFundInfo() {
        return fundInfo;
    }

    public void setFundInfo(String fundInfo) {
        this.fundInfo = fundInfo;
    }

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

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
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

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }
}
