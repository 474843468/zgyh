package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult;

import java.math.BigDecimal;

/**
 * Created by lq7090 on 2016/11/17.
 * 信用卡消费分期提交交易
 */
public class PsnCrcdDividedPayConsumeResultParams {

    /**
     * accountId : 37905296
     * currencyCode : 001
     * amount : 100
     * divPeriod : 3
     * chargeMode : 0
     * firstInAmount : 2000
     * instmtCharge : 1
     * restPerTimeInAmount : 1000
     * crcdFinalFour : 1234
     * token : 12345678
     * Smc : 111111
     * Otp : 111111
     * _plainData : null
     * _certDN : null
     * _signedData : null
     */

    private int accountId;
    private String currencyCode;
    private BigDecimal amount;
    private int divPeriod;
    private String chargeMode;
    private BigDecimal firstInAmount;
    private BigDecimal instmtCharge;
    private BigDecimal restPerTimeInAmount;
    private String crcdFinalFour;
    private String token;
    private String Smc;
    private String Otp;
    private String _plainData;
    private String _certDN;
    private String _signedData;

    private String Smc_RC;//手机交易码
    private String Otp_RC;//动态口令

    private String	deviceInfo	;//设备信息
    private String  deviceInfo_RC;
    private String	devicePrint	;//

    private String activ;//	控件取值	string
    private String	state;//	控件取值	string
    private String conversationId;//会话id


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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getDivPeriod() {
        return divPeriod;
    }

    public void setDivPeriod(int divPeriod) {
        this.divPeriod = divPeriod;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public BigDecimal getFirstInAmount() {
        return firstInAmount;
    }

    public void setFirstInAmount(BigDecimal firstInAmount) {
        this.firstInAmount = firstInAmount;
    }

    public BigDecimal getInstmtCharge() {
        return instmtCharge;
    }

    public void setInstmtCharge(BigDecimal instmtCharge) {
        this.instmtCharge = instmtCharge;
    }

    public BigDecimal getRestPerTimeInAmount() {
        return restPerTimeInAmount;
    }

    public void setRestPerTimeInAmount(BigDecimal restPerTimeInAmount) {
        this.restPerTimeInAmount = restPerTimeInAmount;
    }

    public String getCrcdFinalFour() {
        return crcdFinalFour;
    }

    public void setCrcdFinalFour(String crcdFinalFour) {
        this.crcdFinalFour = crcdFinalFour;
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

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
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
}


