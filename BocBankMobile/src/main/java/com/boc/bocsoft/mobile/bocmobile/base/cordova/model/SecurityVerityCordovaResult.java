package com.boc.bocsoft.mobile.bocmobile.base.cordova.model;

/**
 * 作者：XieDu
 * 创建时间：2016/12/13 9:56
 * 描述：
 */
public class SecurityVerityCordovaResult {

    /**
     * 控件版本号
     */
    private String activ;
    /**
     *
     */
    private String state;
    private String Sms_RC;
    private String Sms;
    private String Otp_RC;
    private String Otp;
    private String deviceInfo_RC;
    private String deviceInfo;
    private String devicePrint;
    private String _signedData;

    public String getActiv() { return activ;}

    public void setActiv(String activ) { this.activ = activ;}

    public String getState() { return state;}

    public void setState(String state) { this.state = state;}

    public String getSms_RC() { return Sms_RC;}

    public void setSms_RC(String Sms_RC) { this.Sms_RC = Sms_RC;}

    public String getSms() { return Sms;}

    public void setSms(String Sms) { this.Sms = Sms;}

    public String getOtp_RC() { return Otp_RC;}

    public void setOtp_RC(String Otp_RC) { this.Otp_RC = Otp_RC;}

    public String getOtp() { return Otp;}

    public void setOtp(String Otp) { this.Otp = Otp;}

    public String getDevicePrint() { return devicePrint;}

    public void setDevicePrint(String devicePrint) { this.devicePrint = devicePrint;}

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }
}
