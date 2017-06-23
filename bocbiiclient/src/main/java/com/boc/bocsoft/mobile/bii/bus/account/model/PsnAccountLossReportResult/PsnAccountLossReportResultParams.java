package com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportResult;

/**
 * 活期一本通提交交易请求
 * <p>
 * Created by liuweidong on 2016/6/7.
 */
public class PsnAccountLossReportResultParams {

    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 账户ID
     */
    private int accountId;
    /**
     * 账户卡账号
     */
    private String accountNumber;
    /**
     * 挂失期限 1：5天；2：长期
     */
    private String lossDays;
    /**
     * 客户姓名
     */
    private String Name;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;
    /**
     * 手机交易码
     */
    private String Smc;
    private String Smc_RC;
    /**
     * 动态口令
     */
    private String Otp;
    private String Otp_RC;
    /**
     * 手机硬件绑定
     */
    private String deviceInfo;
    private String deviceInfo_RC;
    private String state;
    private String activ;
    private String _signedData;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLossDays() {
        return lossDays;
    }

    public void setLossDays(String lossDays) {
        this.lossDays = lossDays;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String Otp) {
        this.Otp = Otp;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String Otp_RC) {
        this.Otp_RC = Otp_RC;
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

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }
}
