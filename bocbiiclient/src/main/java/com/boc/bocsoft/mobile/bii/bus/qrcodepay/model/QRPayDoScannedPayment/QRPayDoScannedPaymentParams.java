package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoScannedPayment;

/**
 * 反扫支付
 * 客户输入密码后，前端调用此接口验证密码，验证通过后通知后台继续交易
 * Created by wangf on 2016/8/30.
 */
public class QRPayDoScannedPaymentParams {

    //防重令牌
    private String token;
    //支付密码
    private String password;
    //支付密码
    private String password_RC;
    //支付密码类型  简单密码（六位数字）：01 复杂密码（字母数字组合）：02
    private String passType;
    private String activ;
    private String state;
    //会话id
    private String conversationId;


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
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
