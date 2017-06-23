package com.boc.bocsoft.mobile.bocmobile.buss.login.model;

/**
 * Created by feib on 16/8/2.
 */
public class CardBalanceLoginViewModel {
    /**
     * 登录上送数据项
     */
    //卡号
    private String loginName;
    //借记卡取款密码
    private String atmPassword;
    //随机数(借记卡取款密码)
    private String atmPassword_RC;
    //信用卡查询密码
    private String phoneBankPassword;
    //随机数(信用卡查询密码)
    private String phoneBankPassword_RC;
    //图形验证码
    private String validationChar;
    //控件取值
    private String activ;
    //控件取值
    private String state;
    /**
     * 登录返回数据项
     */
    //卡号Sequence
    private String accountSeq;
    private String accountType;
    private String name;
    private String eBankingFlag;
    private String isHaveEleCashAcct;
    private String accountNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteBankingFlag() {
        return eBankingFlag;
    }

    public void seteBankingFlag(String eBankingFlag) {
        this.eBankingFlag = eBankingFlag;
    }

    public String isHaveEleCashAcct() {
        return isHaveEleCashAcct;
    }

    public void setHaveEleCashAcct(String haveEleCashAcct) {
        isHaveEleCashAcct = haveEleCashAcct;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }



    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAtmPassword() {
        return atmPassword;
    }

    public void setAtmPassword(String atmPassword) {
        this.atmPassword = atmPassword;
    }

    public String getAtmPassword_RC() {
        return atmPassword_RC;
    }

    public void setAtmPassword_RC(String atmPassword_RC) {
        this.atmPassword_RC = atmPassword_RC;
    }

    public String getPhoneBankPassword() {
        return phoneBankPassword;
    }

    public void setPhoneBankPassword(String phoneBankPassword) {
        this.phoneBankPassword = phoneBankPassword;
    }

    public String getPhoneBankPassword_RC() {
        return phoneBankPassword_RC;
    }

    public void setPhoneBankPassword_RC(String phoneBankPassword_RC) {
        this.phoneBankPassword_RC = phoneBankPassword_RC;
    }

    public String getValidationChar() {
        return validationChar;
    }

    public void setValidationChar(String validationChar) {
        this.validationChar = validationChar;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccountSeq() {
        return accountSeq;
    }

    public void setAccountSeq(String accountSeq) {
        this.accountSeq = accountSeq;
    }
}
