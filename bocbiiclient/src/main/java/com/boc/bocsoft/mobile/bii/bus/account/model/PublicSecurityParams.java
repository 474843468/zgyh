package com.boc.bocsoft.mobile.bii.bus.account.model;

/**
 * @author wangyang
 *         16/8/25 23:29
 *         安全认证参数
 */
public class PublicSecurityParams extends PublicParams {

    /**
     * 手机校验码-加密
     */
    private String Smc;
    /**
     * 手机校验码-加密
     */
    private String Smc_RC;
    /**
     * 动态口令-加密
     */
    private String Otp;
    /**
     * 动态口令-加密
     */
    private String Otp_RC;
    /**
     * CA密文
     */
    private String _signedData;
    /**
     * 控件取值
     */
    private String activ = "303101003";
    /**
     * 控件取值
     */
    private String state = "41943040";
    /**
     * 设备信息-加密
     */
    private String deviceInfo;
    /**
     * 设备信息-加密
     */
    private String deviceInfo_RC;
    /**
     * 设置指纹
     */
    private String devicePrint;

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
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

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }
}
