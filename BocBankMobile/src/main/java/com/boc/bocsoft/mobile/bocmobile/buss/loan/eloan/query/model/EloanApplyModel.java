package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;

import java.io.Serializable;

/**
 * Created by louis.hui on 2016/7/6.
 * 激活申请传送参数
 */
public class EloanApplyModel implements Serializable{
    /**客户姓名*/
    private String customerName;
    /**证件类型*/
    private String identityType;
    /**证件号*/
    private String identityNumber;
    /**手机号*/
    private String mobile;
    /**授信额度*/
    private String quote;
    /**币种*/
    private String currency;

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getIdentityType() {
        return identityType;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getQuote() {
        return quote;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "EloanApplyModel{" +
                "customerName='" + customerName + '\'' +
                ", identityType='" + identityType + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                ", quote='" + quote + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
