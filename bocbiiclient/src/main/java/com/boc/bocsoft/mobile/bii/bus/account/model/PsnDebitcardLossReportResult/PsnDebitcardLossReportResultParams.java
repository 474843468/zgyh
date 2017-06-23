package com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportResult;

/**
 * 借记卡临时挂失提交交易请求
 *
 * Created by liuweidong on 2016/6/6.
 */
public class PsnDebitcardLossReportResultParams {

    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 账户ID
     */
    private int accountId;
    /**
     * 借记卡账号
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
     * 是否同时冻结借记卡主账户
     */
    private String accFlozenFlag;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;
    private String activ;
    private String state;
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

    public String getAccFlozenFlag() {
        return accFlozenFlag;
    }

    public void setAccFlozenFlag(String accFlozenFlag) {
        this.accFlozenFlag = accFlozenFlag;
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

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String Otp) {
        this.Otp = Otp;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
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

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }
}
