package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignSubmit;

/**
 * Params：双向宝签约提交交易
 * Created by zhx on 2016/11/21
 */
public class PsnVFGSignSubmitParams {

    /**
     * bailNo : 123
     * settleCurrency : 001
     * accountNumber : 4563510800034881051
     * accountId : 101418297
     * Smc : 111111
     * token : 0di22fxz
     * Otp : 111111
     * bailName : test
     */
    // 借记卡账户标识
    private String accountId;
    // 借记卡卡号
    private String accountNumber;
    // 保证金产品序号
    private String bailNo;
    // 保证金产品名称
    private String bailName;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String settleCurrency;
    // 手机交易码
    private String Smc;
    // 动态口令
    private String Otp;
    // 防重机制，通过PSNGetTokenId接口获取
    private String token;
    // CA认证生成的密文
    private String _signedData;
    private String conversationId;

    private String Smc_RC;
    private String Otp_RC;
    private String state;
    private String activ;
    /**
     * 设备信息-加密
     */
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBailNo() {
        return bailNo;
    }

    public void setBailNo(String bailNo) {
        this.bailNo = bailNo;
    }

    public String getBailName() {
        return bailName;
    }

    public void setBailName(String bailName) {
        this.bailName = bailName;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
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
}
