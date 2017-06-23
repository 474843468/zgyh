package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedSubmit;

/**
 * 作者：XieDu
 * 创建时间：2016/9/5 15:23
 * 描述：支付签约修改提交交易接口
 */
public class PsnLOANChangeSignedSubmitParams {
    private String conversationId;

    private String quoteOrActNo;
    private String quoteFlag;
    private String usePref;
    /**
     * 签约账户Id
     */
    private String signAccountId;
    /**
     * 签约账户
     */
    private String signAccountNum;
    private String oldsignAccount;
    private String signPeriod;
    private String repayFlag;
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;
    private String state;
    private String activ;
    /**
     * 手机交易码
     * 加密
     */
    private String Smc;
    /**
     * 动态口令
     * 加密
     */
    private String Otp;

    private String Smc_RC;
    private String Otp_RC;
    private String token;
    private  String _signedData;

    public String getQuoteOrActNo() {
        return quoteOrActNo;
    }

    public void setQuoteOrActNo(String quoteOrActNo) {
        this.quoteOrActNo = quoteOrActNo;
    }

    public String getQuoteFlag() {
        return quoteFlag;
    }

    public void setQuoteFlag(String quoteFlag) {
        this.quoteFlag = quoteFlag;
    }

    public String getUsePref() {
        return usePref;
    }

    public void setUsePref(String usePref) {
        this.usePref = usePref;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSignAccountId() {
        return signAccountId;
    }

    public void setSignAccountId(String signAccountId) {
        this.signAccountId = signAccountId;
    }

    public String getSignAccountNum() {
        return signAccountNum;
    }

    public void setSignAccountNum(String signAccountNum) {
        this.signAccountNum = signAccountNum;
    }

    public String getOldsignAccount() {
        return oldsignAccount;
    }

    public void setOldsignAccount(String oldsignAccount) {
        this.oldsignAccount = oldsignAccount;
    }

    public String getSignPeriod() {
        return signPeriod;
    }

    public void setSignPeriod(String signPeriod) {
        this.signPeriod = signPeriod;
    }

    public String getRepayFlag() {
        return repayFlag;
    }

    public void setRepayFlag(String repayFlag) {
        this.repayFlag = repayFlag;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }
}
