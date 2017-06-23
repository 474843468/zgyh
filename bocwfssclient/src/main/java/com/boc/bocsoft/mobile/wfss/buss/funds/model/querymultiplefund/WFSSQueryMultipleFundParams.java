package com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund;

/**
 * 3.1 基金列表查询
 * Created by gwluo on 2016/10/25.
 */

public class WFSSQueryMultipleFundParams {
    /**
     * 基金类型	全部、自选、股票型、债券其型、货币型、混合型、QDII型、保本型、指数型、理财型、ETF、资管计划产品、依托产品、其他
     */
    private String fundType;
    /*基金状态	全部、正常开放、可认购、暂停交易、暂停申购、暂停赎回*/
    private String fundStatus;
    /*交易币种	全部、人民币元、英镑、港币、美元、日元、欧元*/
    private String transCurrency;
    /*风险级别	全部、低风险、中低风险、中风险、中高风险、高风险*/
    private String levelOfRisk;
    /*基金公司代码*/
    private String fundCompanyCode;
    /*排序字段	 dwjz单位净值*/
    private String sortFile;
    private String change_of_day;//日涨跌幅
    private String change_of_month;//月涨跌幅
    private String change_of_quarter;//季涨跌幅
    private String change_of_half_year;//半年涨跌幅
    private String change_of_year;//年涨跌幅
    private String this_year_priceChange;//今年以来涨跌幅
    private String tield_of_week;//七日年化收益率
    private String tield_of_ten_thousand;//万份收益

    private String sortType;//	排序方式	1	N	asc-升序
    private String desc;//-降序
    private String pageNo;//	页码		N	默认为1
    private String pageSize;//	每页大小			默认10

    //基金代码列表, 该字段不为空时上送多个基金代码用&连接，为空时查询全部基金列表
    private String multipleFundBakCode;

    public String getMultipleFundBakCode() {
        return multipleFundBakCode;
    }

    public void setMultipleFundBakCode(String multipleFundBakCode) {
        this.multipleFundBakCode = multipleFundBakCode;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundStatus() {
        return fundStatus;
    }

    public void setFundStatus(String fundStatus) {
        this.fundStatus = fundStatus;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    public String getLevelOfRisk() {
        return levelOfRisk;
    }

    public void setLevelOfRisk(String levelOfRisk) {
        this.levelOfRisk = levelOfRisk;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }

    public String getSortFile() {
        return sortFile;
    }

    public void setSortFile(String sortFile) {
        this.sortFile = sortFile;
    }

    public String getChange_of_day() {
        return change_of_day;
    }

    public void setChange_of_day(String change_of_day) {
        this.change_of_day = change_of_day;
    }

    public String getChange_of_month() {
        return change_of_month;
    }

    public void setChange_of_month(String change_of_month) {
        this.change_of_month = change_of_month;
    }

    public String getChange_of_quarter() {
        return change_of_quarter;
    }

    public void setChange_of_quarter(String change_of_quarter) {
        this.change_of_quarter = change_of_quarter;
    }

    public String getChange_of_half_year() {
        return change_of_half_year;
    }

    public void setChange_of_half_year(String change_of_half_year) {
        this.change_of_half_year = change_of_half_year;
    }

    public String getChange_of_year() {
        return change_of_year;
    }

    public void setChange_of_year(String change_of_year) {
        this.change_of_year = change_of_year;
    }

    public String getThis_year_priceChange() {
        return this_year_priceChange;
    }

    public void setThis_year_priceChange(String this_year_priceChange) {
        this.this_year_priceChange = this_year_priceChange;
    }

    public String getTield_of_week() {
        return tield_of_week;
    }

    public void setTield_of_week(String tield_of_week) {
        this.tield_of_week = tield_of_week;
    }

    public String getTield_of_ten_thousand() {
        return tield_of_ten_thousand;
    }

    public void setTield_of_ten_thousand(String tield_of_ten_thousand) {
        this.tield_of_ten_thousand = tield_of_ten_thousand;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
