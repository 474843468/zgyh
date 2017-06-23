package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDiv;

/**
 * Created by cry7096 on 2016/11/22.
 */
public class PsnCrcdApplyCashDivParams {
    /**
     * fromAccountId : 转出账户Id
     * toAccountId : 存入账户Id
     * toCardNo : 存入卡号
     * currency : 币种
     * divAmount : 分期金额
     * divPeriod : 分期期数
     * chargeMode : 手续费收取方式
     * divCharge : 分期手续费
     * divRate : 分期手续费率
     * firstPayAmount : 分期后每期应还金额-首期
     * perPayAmount : 分期后每期应还金额-后每期
     * _combinId : 安全因子组合id
     * conversationId : 会话ID
     * token : 防重机制
     * activ : 200102017-版本控制-前端控件加密默认就上送这两个字段？
     * state : TUFDPTZjLTBiLTg0LWE3LTE1LWUzO0lQPTIyLjExLjI2LjY1O0RJU0tJRD0wMDAwNDgyMztCT0NH
     * VUlEPXs0MEUyQkZENy01RkM4LTQ5RkUtQjRFOS0yOEQzNTFBNDg5QUR9O1NUQVRFQ09ERT0wNDkw
     * MDAwMTs=   前端控件加密默认就上送这两个字段？
     * Smc : 手机交易码
     * Smc_RC : V4IABa5YQCMCWZwRjx0wNkvyxWdtWRMYmme1dZVYzTVzcZlzKJWpUk+cl/tvcZMjZCP3mNnuT+Nr
     * SjhEAlKGUdnwOGx8hC36qPSqSKUeS1wRjaVgk/dc/GVZuuNmRdU+NCSwthkOw8AFGV9gtPPXLA==
     * Otp : 动态口令
     * _signedData : CA认证生成的密文
     */
    private String fromAccountId;
    private String toAccountId;
    private String toCardNo;
    private String currency;
    private String divAmount;
    private String divPeriod;
    private String chargeMode;
    private String divCharge;
    private String divRate;
    private String firstPayAmount;
    private String perPayAmount;
    private String _combinId;
    private String conversationId;
    private String token;
    private String activ;
    private String state;
    private String Smc;
    private String Smc_RC;
    private String Otp;
    private String Otp_RC;
    private String _signedData;
    private String devicePrint;
    private String deviceInfo;
    private String deviceInfo_RC;

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

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getOtp() {
        return Otp;
    }

    public String get_signedData() {
        return _signedData;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToCardNo() {
        return toCardNo;
    }

    public void setToCardNo(String toCardNo) {
        this.toCardNo = toCardNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDivAmount() {
        return divAmount;
    }

    public void setDivAmount(String divAmount) {
        this.divAmount = divAmount;
    }

    public String getDivPeriod() {
        return divPeriod;
    }

    public void setDivPeriod(String divPeriod) {
        this.divPeriod = divPeriod;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getDivCharge() {
        return divCharge;
    }

    public void setDivCharge(String divCharge) {
        this.divCharge = divCharge;
    }

    public String getDivRate() {
        return divRate;
    }

    public void setDivRate(String divRate) {
        this.divRate = divRate;
    }

    public String getFirstPayAmount() {
        return firstPayAmount;
    }

    public void setFirstPayAmount(String firstPayAmount) {
        this.firstPayAmount = firstPayAmount;
    }

    public String getPerPayAmount() {
        return perPayAmount;
    }

    public void setPerPayAmount(String perPayAmount) {
        this.perPayAmount = perPayAmount;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
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
