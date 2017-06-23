package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResult;

/**
 * 信用卡挂失及补卡结果请求
 * <p/>
 * Created by liuweidong on 2016/6/13.
 */
public class PsnCrcdReportLossResultParams {

    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 账户标识
     */
    private String accountId;
    /**
     * 挂失类型：0-挂失；1-挂失及补卡
     */
    private String selectLossType;
    /**
     * 防重机制
     */
    private String token;
    /**
     * 动态口令
     */
    private String Otp;
    private String Otp_RC;
    /**
     * 手机交易码
     */
    private String Smc;
    private String Smc_RC;
    /**
     * CA认证生成的密文
     */
    private String _signedData;
    /**
     * 设备指纹 设备指纹字符串由客户浏览器运行的“rsa.js” Javascript脚本生成
     */
    private String devicePrint;
    /**
     * 手机硬件绑定
     */
    private String deviceInfo;
    private String deviceInfo_RC;
    private String state;
    private String activ;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSelectLossType() {
        return selectLossType;
    }

    public void setSelectLossType(String selectLossType) {
        this.selectLossType = selectLossType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public void setOtp_RC(String Otp_RC) {
        this.Otp_RC = Otp_RC;
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

    public void setSmc_RC(String Smc_RC) {
        this.Smc_RC = Smc_RC;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
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

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }
}
