package com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetLogin;

/**
 * Created by feib on 16/8/2.
 * 使用卡号登录账户版网银
 */
public class PsnAccBocnetLoginParams {


    /**
     *  loginName : 5698556685425656
     *  phoneBankPassword  :
     * phoneBankPassword_RC :
     *  validationChar : abcd
     *  activ :
     *  state :
     */
    //会话
    private String conversationId;
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


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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
}
