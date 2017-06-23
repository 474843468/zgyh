package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeService;

/**
 * 开通小额免密服务提交交易
 * Created by wangf on 2016/8/30.
 */
public class QRPayOpenPassFreeServiceParams {

    //会话id
    private String conversationId;
    //支付卡流水
    private String actSeq;
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
    //防重令牌
    private String token;

    //手机硬件绑定
    private String deviceInfo;
    private String deviceInfo_RC;

    private String activ;
    private String state;


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
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
