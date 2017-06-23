package com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit;

/**
 * Created by Administrator on 2016/6/27.
 */
public class PsnSvrRegisterDeviceSubmitParams {

    //动态口令
    private String Otp;
    //动态口令加密
    private String Otp_RC;
    //短信验证码
    private String Smc;
    //短信验证码加密
    private String Smc_RC;
    //CA认证生成的密文
    private String _signedData;
    //会话
    private String conversationId;
    //设备硬件信息
    private String deviceInfo;
    //设备硬件加密
    private String deviceInfo_RC;
    // 防重机制
    private String token;
    private String activ;
    private String state;

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

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String Smc) {
        this.Smc = Smc;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String Smc_RC) {
        this.Smc_RC = Smc_RC;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
