package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model;

/**
 * Created by yangle on 2016/12/24.
 * 提交交易安全工具相关model
 */
public class SubmitModel {
    // 动态口令
    private String Otp;
    private String Otp_RC;
    // 短信验证
    private String Smc;
    private String Smc_RC;
    // 设备信息-加密
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;
    // CA认证生成的密文
    private String _signedData;
    // 安全工具信息
    private String state;
    private String activ;

    private String token;

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

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
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

    public void reset() {
         Otp = null;
         Otp_RC = null;
         Smc = null;
         Smc_RC = null;
         deviceInfo = null;
         deviceInfo_RC = null;
         devicePrint = null;
         _signedData = null;
         state = null;
         activ = null;
         token = null;
    }

}
