package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPaySetPass;

/**
 * 设置支付密码
 * Created by wangf on 2016/8/30.
 */
public class QRPaySetPassParams {

    //会话id
    private String conversationId;
    //短信交易码加密串
    private String Smc;
    //短信交易码加密串
    private String Smc_RC;
    //动态口令加密串
    private String Otp;
    //动态口令加密串
    private String Otp_RC;
    //USB KEY或音频KEY加密串
    private String _signedData;
  //手机硬件绑定
    private String deviceInfo;
    private String deviceInfo_RC;
    //防重令牌
    private String token;
    //新支付密码
    private String password;
    //新支付密码
    private String password_RC;
    //再次输入的新支付密码
    private String passwordConform;
    //再次输入的新支付密码
    private String passwordConform_RC;
    //支付密码类型  简单密码（六位数字）：01 复杂密码（字母数字组合）：02
    private String passType;

    private String activ;
    private String state;


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
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
    
    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }
}
