package com.boc.bocsoft.mobile.bocmobile.buss.login.model;

/**
 * Created by feib on 16/7/14.
 * 登录重置密码View Model
 */
public class ModifyPasswordViewModel {
    //卡号后六位
    private String cardNoSixLast;
    //安全组合编号
    private String combinId;
    //会话ID
    private String conversationId;
    //登录名
    private String loginName;
    //新密码
    private String newPass;
    //确认密码
    private String newPass2;
    private String newPass2_RC;
    private String newPass_RC;
    //原密码
    private String oldPass;
    private String oldPass_RC;
    private String state;
    private String activ;

    public String getCardNoSixLast() {
        return cardNoSixLast;
    }

    public void setCardNoSixLast(String cardNoSixLast) {
        this.cardNoSixLast = cardNoSixLast;
    }

    public String getCombinId() {
        return combinId;
    }

    public void setCombinId(String combinId) {
        this.combinId = combinId;
    }

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

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getNewPass2() {
        return newPass2;
    }

    public void setNewPass2(String newPass2) {
        this.newPass2 = newPass2;
    }

    public String getNewPass2_RC() {
        return newPass2_RC;
    }

    public void setNewPass2_RC(String newPass2_RC) {
        this.newPass2_RC = newPass2_RC;
    }

    public String getNewPass_RC() {
        return newPass_RC;
    }

    public void setNewPass_RC(String newPass_RC) {
        this.newPass_RC = newPass_RC;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getOldPass_RC() {
        return oldPass_RC;
    }

    public void setOldPass_RC(String oldPass_RC) {
        this.oldPass_RC = oldPass_RC;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }
}
