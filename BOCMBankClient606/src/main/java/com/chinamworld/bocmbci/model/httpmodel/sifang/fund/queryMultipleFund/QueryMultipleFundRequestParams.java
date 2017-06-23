package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund;

import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.BaseSFFundRequestModel;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 基金列表查询接口上送字段
 */
public class QueryMultipleFundRequestParams extends BaseSFFundRequestModel {
    @Override
    public Object getExtendParam() {
        return "queryMultipleFund";
    }

    private String fundType;//基金类型
    private String fundStatus;//基金状态
    private String transCurrency;//交易币种
    private String levelOfRisk;//风险级别
    private String fundCompanyCode;//基金公司代码
    private String sortFile;//排序字段
    private String sortType;//排序方式
    private String pageNo;//页码
    private String pageSize;//每页大小
    private String multipleFundBakCode;//基金代码列表

    public QueryMultipleFundRequestParams(String fundType,String fundStatus,String transCurrency,String levelOfRisk
                                          ,String fundCompanyCode,String sortFile, String sortType,String pageNo,
                                          String pageSize){
        this.fundType = fundType;
        this.fundStatus = fundStatus;
        this.transCurrency = transCurrency;
        this.levelOfRisk = levelOfRisk;
        this.fundCompanyCode = fundCompanyCode;
        this.sortFile = sortFile;
        this.sortType = sortType;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
 //       this.multipleFundBakCode = multipleFundBakCode;
    }

    public QueryMultipleFundRequestParams(String multipleFundBakCode){
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

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
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

    public String getMultipleFundBakCode() {
        return multipleFundBakCode;
    }

    public void setMultipleFundBakCode(String multipleFundBakCode) {
        this.multipleFundBakCode = multipleFundBakCode;
    }
}
