package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationResult;

/**
 * 风险评估提交
 * Created by guokai on 2016/9/6.
 */
public class PsnInvtEvaluationResultParams {
    private String address;//	地址
    private String phone;//	电话
    private String riskMobile;//	手机
    private String postCode;//	邮编
    private String riskEMail;//	EMAIL
    private String gender;//	性别性别(2-男，1-女)
    private String riskBirthday;//	生日
    private String revenue;//	收入
    private String custNationality;//	客户国别
    private String education;//	教育程度
    private String occupation;//	行业
    private String riskScore;//	风险评估分数
    private String hasInvestExperience;//	是否有投资经验
    private String token;//	动态口令
    private String conversationId;//	会话Id
    private String riskAnswer;//	"ABCDEABCDE"

    public String getRiskAnswer() {
        return riskAnswer;
    }

    public void setRiskAnswer(String riskAnswer) {
        this.riskAnswer = riskAnswer;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustNationality() {
        return custNationality;
    }

    public void setCustNationality(String custNationality) {
        this.custNationality = custNationality;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getRiskBirthday() {
        return riskBirthday;
    }

    public void setRiskBirthday(String riskBirthday) {
        this.riskBirthday = riskBirthday;
    }

    public String getRiskEMail() {
        return riskEMail;
    }

    public void setRiskEMail(String riskEMail) {
        this.riskEMail = riskEMail;
    }

    public String getRiskMobile() {
        return riskMobile;
    }

    public void setRiskMobile(String riskMobile) {
        this.riskMobile = riskMobile;
    }

    public String getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(String riskScore) {
        this.riskScore = riskScore;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
