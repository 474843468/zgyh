package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChange;

/**
 * Created by wangtong on 2016/8/11.
 */
public class PsnSsmAccountChangeParam {

    private String conversationId;
    private String accountId;
    private String accountNumber;
    private String accAlias;
    private String accountNickName;
    private String feeAccountNum;
    private String ssmfeeAccountId;
    private String token;
    private String activ;
    private String state;
    private String Smc;
    private String Smc_RC;
    private String Otp;
    private String Otp_RC;
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;
    private String _signedData;

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccAlias() {
        return accAlias;
    }

    public void setAccAlias(String accAlias) {
        this.accAlias = accAlias;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
    }

    public String getFeeAccountNum() {
        return feeAccountNum;
    }

    public void setFeeAccountNum(String feeAccountNum) {
        this.feeAccountNum = feeAccountNum;
    }

    public String getSsmfeeAccountId() {
        return ssmfeeAccountId;
    }

    public void setSsmfeeAccountId(String ssmfeeAccountId) {
        this.ssmfeeAccountId = ssmfeeAccountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
