package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitSubmit;

/**
 * 汇往取款人提交请求
 * <p/>
 * Created by liuweidong on 2016/7/12.
 */
public class PsnMobileRemitSubmitParams {

    /**
     * 会话ID
     */
    private String conversationId;

    private String accountId;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 汇款币种
     */
    private String remitCurrencyCode;
    /**
     * 汇款金额
     */
    private String remitAmount;
    /**
     * 附言
     */
    private String remark;
    /**
     * 取款密码
     */
    private String withDrawPwd;
    private String activ;
    private String state;
    /**
     * 加密后的取款密码
     */
    private String withDrawPwd_RC;
    /**
     * 确认密码
     */
    private String withDrawPwdConf;
    /**
     * 加密后的确认取款密码
     */
    private String withDrawPwdConf_RC;
    /**
     * 防重
     */
    private String token;
    /**
     * 手机交易码
     */
    private String Smc;
    private String Smc_RC;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getRemitCurrencyCode() {
        return remitCurrencyCode;
    }

    public void setRemitCurrencyCode(String remitCurrencyCode) {
        this.remitCurrencyCode = remitCurrencyCode;
    }

    public String getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(String remitAmount) {
        this.remitAmount = remitAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWithDrawPwd() {
        return withDrawPwd;
    }

    public void setWithDrawPwd(String withDrawPwd) {
        this.withDrawPwd = withDrawPwd;
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

    public String getWithDrawPwd_RC() {
        return withDrawPwd_RC;
    }

    public void setWithDrawPwd_RC(String withDrawPwd_RC) {
        this.withDrawPwd_RC = withDrawPwd_RC;
    }

    public String getWithDrawPwdConf() {
        return withDrawPwdConf;
    }

    public void setWithDrawPwdConf(String withDrawPwdConf) {
        this.withDrawPwdConf = withDrawPwdConf;
    }

    public String getWithDrawPwdConf_RC() {
        return withDrawPwdConf_RC;
    }

    public void setWithDrawPwdConf_RC(String withDrawPwdConf_RC) {
        this.withDrawPwdConf_RC = withDrawPwdConf_RC;
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
