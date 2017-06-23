package com.boc.bocsoft.mobile.bii.bus.login.model.LoginWP7;

/**
 * Created by feibin on 2016/5/19.
 */
public class LoginWP7Params {

    /**
     * loginName : hhf
     * password : 1u3ouii12i3ui123=
     * validationChar : ’1234’
     * segment : 1
     */

    private String loginName;
    private String password;
    private String password_RC;
    private String validationChar;
    private String segment;
    private String wp7LoginType;
    private String conversationId;
    private String activ;
    private String state;

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

    public String getPassword_RC() {
        return password_RC;
    }

    public void setPassword_RC(String password_RC) {
        this.password_RC = password_RC;
    }


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public String getWp7LoginType() {
        return wp7LoginType;
    }

    public void setWp7LoginType(String wp7LoginType) {
        this.wp7LoginType = wp7LoginType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidationChar() {
        return validationChar;
    }

    public void setValidationChar(String validationChar) {
        this.validationChar = validationChar;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }
}
