package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPayment;

/**
 * ATM无卡取款交易请求
 *
 * Created by liuweidong on 2016/7/19.
 */
public class PsnPasswordRemitPaymentParams {
    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 执行日期
     */
    private String dueDate;
    /**
     * 汇款类型
     */
    private String freeRemitType;
    /**
     * 账户ID
     */
    private String fromAccountId;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 转账金额
     */
    private String amount;
    /**
     * 币种
     */
    private String currencyCode;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 是否允许在ATM取现 1：是，2：否
     */
    private String ATMWithdraw;
    /**
     * 附言
     */
    private String furInf;
    private String obligatePassword;
    /**
     * 加密后的预留密码
     */
    private String obligatePassword_RC;
    /**
     * 请再输入一次密码
     */
    private String reObligatePassword;
    private String reObligatePassword_RC;
    private String activ;
    private String state;
    /**
     * 防重
     */
    private String token;
    /**
     * 手机交易码
     */
    private String Smc;
    private String Smc_RC;
    private String devicePrint;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getFreeRemitType() {
        return freeRemitType;
    }

    public void setFreeRemitType(String freeRemitType) {
        this.freeRemitType = freeRemitType;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getATMWithdraw() {
        return ATMWithdraw;
    }

    public void setATMWithdraw(String ATMWithdraw) {
        this.ATMWithdraw = ATMWithdraw;
    }

    public String getFurInf() {
        return furInf;
    }

    public void setFurInf(String furInf) {
        this.furInf = furInf;
    }

    public String getObligatePassword() {
        return obligatePassword;
    }

    public void setObligatePassword(String obligatePassword) {
        this.obligatePassword = obligatePassword;
    }

    public String getObligatePassword_RC() {
        return obligatePassword_RC;
    }

    public void setObligatePassword_RC(String obligatePassword_RC) {
        this.obligatePassword_RC = obligatePassword_RC;
    }

    public String getReObligatePassword() {
        return reObligatePassword;
    }

    public void setReObligatePassword(String reObligatePassword) {
        this.reObligatePassword = reObligatePassword;
    }

    public String getReObligatePassword_RC() {
        return reObligatePassword_RC;
    }

    public void setReObligatePassword_RC(String reObligatePassword_RC) {
        this.reObligatePassword_RC = reObligatePassword_RC;
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

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
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

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }
}
