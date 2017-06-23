package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 贷款申请提交信息
 * Created by XieDu on 2016/7/28.
 */
public class OnLineLoanSubmitModel implements Parcelable {
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
    /**
     * 网点名称
     */
    private String deptName;
    /**
     * 网点地址
     */
    private String deptAddr;

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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityCode);
        dest.writeString(this.productName);
        dest.writeString(this.productCode);
        dest.writeString(this.appName);
        dest.writeString(this.appAge);
        dest.writeString(this.appSex);
        dest.writeString(this.appPhone);
        dest.writeString(this.appEmail);
        dest.writeString(this.houseTradePrice);
        dest.writeString(this.tuitionTradePrice);
        dest.writeString(this.carTradePrice);
        dest.writeString(this.loanAmount);
        dest.writeString(this.loanTerm);
        dest.writeString(this.currency);
        dest.writeString(this.houseAge);
        dest.writeString(this.entName);
        dest.writeString(this.officeAddress);
        dest.writeString(this.mainBusiness);
        dest.writeString(this.principalName);
        dest.writeString(this.guaTypeFlag);
        dest.writeString(this.guaWay);
        dest.writeString(this.guaType);
        dest.writeString(this.deptID);
        dest.writeString(this.deptName);
        dest.writeString(this.deptAddr);
    }

    public OnLineLoanSubmitModel() {}

    protected OnLineLoanSubmitModel(Parcel in) {
        this.cityCode = in.readString();
        this.productName = in.readString();
        this.productCode = in.readString();
        this.appName = in.readString();
        this.appAge = in.readString();
        this.appSex = in.readString();
        this.appPhone = in.readString();
        this.appEmail = in.readString();
        this.houseTradePrice = in.readString();
        this.tuitionTradePrice = in.readString();
        this.carTradePrice = in.readString();
        this.loanAmount = in.readString();
        this.loanTerm = in.readString();
        this.currency = in.readString();
        this.houseAge = in.readString();
        this.entName = in.readString();
        this.officeAddress = in.readString();
        this.mainBusiness = in.readString();
        this.principalName = in.readString();
        this.guaTypeFlag = in.readString();
        this.guaWay = in.readString();
        this.guaType = in.readString();
        this.deptID = in.readString();
        this.deptName = in.readString();
        this.deptAddr = in.readString();
    }

    public static final Creator<OnLineLoanSubmitModel> CREATOR =
            new Creator<OnLineLoanSubmitModel>() {
                @Override
                public OnLineLoanSubmitModel createFromParcel(
                        Parcel source) {return new OnLineLoanSubmitModel(source);}

                @Override
                public OnLineLoanSubmitModel[] newArray(
                        int size) {return new OnLineLoanSubmitModel[size];}
            };
}
