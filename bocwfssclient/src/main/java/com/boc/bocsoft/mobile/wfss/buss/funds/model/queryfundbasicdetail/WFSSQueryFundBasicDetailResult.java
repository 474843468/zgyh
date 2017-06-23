package com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail;

/**
 * 3.2 基金详情接口（基本信息）
 * Created by gwluo on 2016/10/25.
 */

public class WFSSQueryFundBasicDetailResult {
    private String fundId;//	基金Id
    private String fundBakCode;//	基金公共代码
    private String fundName;//	基金名称
    private String dBuyStart;//	起购金额
    private String transCurrency;//	交易币种
    private String fundCompanyCode;//	基金公司代码
    private String fundCompany;//	基金公司名称
    private String collectFeeWay;//	收费方式
    private String productFoundDate;//	产品成立日期
    private String dwjz;//	单位净值
    private String jzTime;//	单位净值时间
    private String levelOfRisk;//	风险级别
    private String gradeOrg;//	评级机构
    private String gradeLevel;//	评级级别
    private String preferential;//	优惠
    private String currPercentDiff;//	日涨跌幅
    private String yieldOfWeek;//	七日年化收益率
    private String yieldOfTenThousand;//	万分收益
    private String applyBuyFeeLow;//	起购金额
    private String productType;//	产品种类
    private String fundScale;//基金规模

    public String getFundScale() {
        return fundScale;
    }

    public void setFundScale(String fundScale) {
        this.fundScale = fundScale;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getFundBakCode() {
        return fundBakCode;
    }

    public void setFundBakCode(String fundBakCode) {
        this.fundBakCode = fundBakCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getdBuyStart() {
        return dBuyStart;
    }

    public void setdBuyStart(String dBuyStart) {
        this.dBuyStart = dBuyStart;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }

    public String getFundCompany() {
        return fundCompany;
    }

    public void setFundCompany(String fundCompany) {
        this.fundCompany = fundCompany;
    }

    public String getCollectFeeWay() {
        return collectFeeWay;
    }

    public void setCollectFeeWay(String collectFeeWay) {
        this.collectFeeWay = collectFeeWay;
    }

    public String getProductFoundDate() {
        return productFoundDate;
    }

    public void setProductFoundDate(String productFoundDate) {
        this.productFoundDate = productFoundDate;
    }

    public String getDwjz() {
        return dwjz;
    }

    public void setDwjz(String dwjz) {
        this.dwjz = dwjz;
    }

    public String getJzTime() {
        return jzTime;
    }

    public void setJzTime(String jzTime) {
        this.jzTime = jzTime;
    }

    public String getLevelOfRisk() {
        return levelOfRisk;
    }

    public void setLevelOfRisk(String levelOfRisk) {
        this.levelOfRisk = levelOfRisk;
    }

    public String getGradeOrg() {
        return gradeOrg;
    }

    public void setGradeOrg(String gradeOrg) {
        this.gradeOrg = gradeOrg;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getPreferential() {
        return preferential;
    }

    public void setPreferential(String preferential) {
        this.preferential = preferential;
    }

    public String getCurrPercentDiff() {
        return currPercentDiff;
    }

    public void setCurrPercentDiff(String currPercentDiff) {
        this.currPercentDiff = currPercentDiff;
    }

    public String getYieldOfWeek() {
        return yieldOfWeek;
    }

    public void setYieldOfWeek(String yieldOfWeek) {
        this.yieldOfWeek = yieldOfWeek;
    }

    public String getYieldOfTenThousand() {
        return yieldOfTenThousand;
    }

    public void setYieldOfTenThousand(String yieldOfTenThousand) {
        this.yieldOfTenThousand = yieldOfTenThousand;
    }

    public String getApplyBuyFeeLow() {
        return applyBuyFeeLow;
    }

    public void setApplyBuyFeeLow(String applyBuyFeeLow) {
        this.applyBuyFeeLow = applyBuyFeeLow;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
