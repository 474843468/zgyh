package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPassPre;

/**
 * 设置支付密码预交易
 * Created by wangf on 2016/8/30.
 */
public class QRPaySetPassPreParams {

    //会话id
    private String conversationId;
    //客户选择的安全验证工具id
    private String _combinId;

    //新支付密码
    private String password;
    //新支付密码
    private String password_RC;
    //再次输入的新支付密码
    private String passwordConform;
    //再次输入的新支付密码
    private String passwordConform_RC;
    
    private String activ;
    private String state;


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_RC() {
        return password_RC;
    }

    public void setPassword_RC(String password_RC) {
        this.password_RC = password_RC;
    }

    public String getPasswordConform() {
        return passwordConform;
    }

    public void setPasswordConform(String passwordConform) {
        this.passwordConform = passwordConform;
    }

    public String getPasswordConform_RC() {
        return passwordConform_RC;
    }

    public void setPasswordConform_RC(String passwordConform_RC) {
        this.passwordConform_RC = passwordConform_RC;
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
