package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.model;

import java.io.Serializable;

/**
 * Created by guokai on 2016/9/17.
 */
public class RiskAssessModel implements Serializable {
    private String custName;//	操作员名称
    private String idType;//	证件类型
    private String idNum;//	证件号码
    private String riskLevel;//	客户风险等级
    private String address;//	地址
    private String phone;//	电话
    private String mobile;//	手机
    private String postCode;//	邮编
    private String eMail;//	EMAIL
    private String gender;//	性别性别(2-男，1-女)
    private String birthday;//	生日
    private String hasInvestExperience;//	是否有投资经验
    private String returnCode;//	处理返回码
    private String evalDate;//	风险评测日期
    private String validThruDate;//	风险评测有效期
    private String evalExpired;//	风险评测否到期
    private String custExist;//	是否存在此客户
    private String evaluatedBefore;//	是否做过风险评估
    private String channel;//	风险评估渠道

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCustExist() {
        return custExist;
    }

    public void setCustExist(String custExist) {
        this.custExist = custExist;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getEvalDate() {
        return evalDate;
    }

    public void setEvalDate(String evalDate) {
        this.evalDate = evalDate;
    }

    public String getEvalExpired() {
        return evalExpired;
    }

    public void setEvalExpired(String evalExpired) {
        this.evalExpired = evalExpired;
    }

    public String getEvaluatedBefore() {
        return evaluatedBefore;
    }

    public void setEvaluatedBefore(String evaluatedBefore) {
        this.evaluatedBefore = evaluatedBefore;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHasInvestExperience() {
        return hasInvestExperience;
    }

    public void setHasInvestExperience(String hasInvestExperience) {
        this.hasInvestExperience = hasInvestExperience;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getValidThruDate() {
        return validThruDate;
    }

    public void setValidThruDate(String validThruDate) {
        this.validThruDate = validThruDate;
    }
}
