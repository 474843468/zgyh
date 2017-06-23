package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanDetailQry;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查询单笔贷款记录详情返回结果
 * Created by liuzc on 2016/8/16.
 */
public class PsnOnLineLoanDetailQryResult {
    private String productName; //产品名称
    private String appName; //姓名
    private String appAge; //年龄
    private String appSex;	//性别: 1:男、2:女
    private String appPhone;	//联系电话
    private String appEmail;	//Email地址
    private String houseTradePrice;	//房屋交易价
    private String tuitionTradePrice;	//学费生活费总额

    private String carTradePrice; //净车价
    private String loanAmount; //贷款金额
    private String loanTerm; //贷款期限
    private String currency;	//币种
    private String houseAge;	//所购住房房龄
    private String guaWay;	//担保方式
    private String deptName;	//网点名称
    private String deptAddr;	//网点地址

    private String loanStatus; //贷款状态
    private String entName; //企业名称
    private String officeAddress; //办公地址
    private String mainBusiness;	//主营业务
    private String principalName;	//负责人姓名
    private String guaTypeFlag;	//是否能提供抵押担保
    private String guaType;	//担保类别
    private String refuseReason;	//拒绝原因

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppAge() {
        return appAge;
    }

    public void setAppAge(String appAge) {
        this.appAge = appAge;
    }

    public String getAppSex() {
        return appSex;
    }

    public void setAppSex(String appSex) {
        this.appSex = appSex;
    }

    public String getAppPhone() {
        return appPhone;
    }

    public void setAppPhone(String appPhone) {
        this.appPhone = appPhone;
    }

    public String getAppEmail() {
        return appEmail;
    }

    public void setAppEmail(String appEmail) {
        this.appEmail = appEmail;
    }

    public String getHouseTradePrice() {
        return houseTradePrice;
    }

    public void setHouseTradePrice(String houseTradePrice) {
        this.houseTradePrice = houseTradePrice;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getHouseAge() {
        return houseAge;
    }

    public void setHouseAge(String houseAge) {
        this.houseAge = houseAge;
    }

    public String getGuaWay() {
        return guaWay;
    }

    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAddr() {
        return deptAddr;
    }

    public void setDeptAddr(String deptAddr) {
        this.deptAddr = deptAddr;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
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

    public String getGuaType() {
        return guaType;
    }

    public void setGuaType(String guaType) {
        this.guaType = guaType;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }
}
