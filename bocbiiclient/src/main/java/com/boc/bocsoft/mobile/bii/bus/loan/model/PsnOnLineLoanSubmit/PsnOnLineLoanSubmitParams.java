package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanSubmit;

/**
 * Created by XieDu on 2016/7/27.
 */
public class PsnOnLineLoanSubmitParams {

    private String cityCode;
    private String productName;
    private String productCode;
    private String appName;
    private String appAge;
    private String appSex;
    private String appPhone;
    private String appEmail;
    private String houseTradePrice;
    private String tuitionTradePrice;
    private String carTradePrice;
    private String loanAmount;
    private String loanTerm;
    private String currency;
    private String houseAge;

    /**
     * 企业名称
     */
    private String entName;
    /**
     * 办公地址
     */
    private String officeAddress;

    /**
     * 主营业务
     */
    private String mainBusiness;

    /**
     * 负责人姓名
     */
    private String principalName;

    /**
     * 是否能提供抵押担保
     */
    private String guaTypeFlag;
    /**
     * 担保方式
     * 1:房产抵押
     * 2:有价权利质押
     * 3:其他
     */
    private String guaWay;
    /**
     * 担保类别
     * 1:住房
     * 2:商铺
     * 3:土地
     * 4:其他固定资产
     * 若 guaTypeFlag上送1，担保类别必填
     */
    private String guaType;
    /**
     * 网点编号
     */
    private String deptID;
    private String token;
    private String conversationId;

    public String getCityCode() { return cityCode;}

    public void setCityCode(String cityCode) { this.cityCode = cityCode;}

    public String getProductName() { return productName;}

    public void setProductName(String productName) { this.productName = productName;}

    public String getProductCode() { return productCode;}

    public void setProductCode(String productCode) { this.productCode = productCode;}

    public String getAppSex() { return appSex;}

    public void setAppSex(String appSex) { this.appSex = appSex;}

    public String getCurrency() { return currency;}

    public void setCurrency(String currency) { this.currency = currency;}

    public String getDeptID() { return deptID;}

    public void setDeptID(String deptID) { this.deptID = deptID;}

    public String getAppAge() { return appAge;}

    public void setAppAge(String appAge) { this.appAge = appAge;}

    public String getAppPhone() { return appPhone;}

    public void setAppPhone(String appPhone) { this.appPhone = appPhone;}

    public String getHouseTradePrice() { return houseTradePrice;}

    public void setHouseTradePrice(String houseTradePrice) {
        this.houseTradePrice = houseTradePrice;
    }

    public String getHouseAge() { return houseAge;}

    public void setHouseAge(String houseAge) { this.houseAge = houseAge;}

    public String getEntName() { return entName;}

    public void setEntName(String entName) { this.entName = entName;}

    public String getToken() { return token;}

    public void setToken(String token) { this.token = token;}

    public String getConversationId() { return conversationId;}

    public void setConversationId(String conversationId) { this.conversationId = conversationId;}

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppEmail() {
        return appEmail;
    }

    public void setAppEmail(String appEmail) {
        this.appEmail = appEmail;
    }

    public String getTuitionTradePrice() {
        return tuitionTradePrice;
    }

    public void setTuitionTradePrice(String tuitionTradePrice) {
        this.tuitionTradePrice = tuitionTradePrice;
    }

    public String getCarTradePrice() {
        return carTradePrice;
    }

    public void setCarTradePrice(String carTradePrice) {
        this.carTradePrice = carTradePrice;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getGuaTypeFlag() {
        return guaTypeFlag;
    }

    public void setGuaTypeFlag(String guaTypeFlag) {
        this.guaTypeFlag = guaTypeFlag;
    }

    public String getGuaWay() {
        return guaWay;
    }

    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }

    public String getGuaType() {
        return guaType;
    }

    public void setGuaType(String guaType) {
        this.guaType = guaType;
    }
}
