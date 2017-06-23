package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult;

import java.math.BigDecimal;

/**
 * Created by lq7090 on 2016/11/17.
 */
public class PsnCrcdDividedPayBillSetResultParams {


    /**
     * accountId : 37905296
     * currencyCode : 001
     * amount : 11111
     * divPeriod : 6
     * chargeMode : 0
     * lowAmt : 100
     * instmtCharge : 1000
     * firstInAmount : 1000
     * restPerTimeInAmount : 500
     * restAmount : 10000
     * minAmount : 1000
     * crcdFinalFour : 1234
     * Smc : 111111
     * Otp : 111111
     * token : 12345678
     * _plainData : null
     * _certDN : null
     * _signedData : null
     */

    private int accountId;
    private String currencyCode;
    private BigDecimal amount;
    private int divPeriod;
    private String chargeMode;
    private BigDecimal lowAmt;
    private BigDecimal instmtCharge;
    private BigDecimal firstInAmount;
    private BigDecimal restPerTimeInAmount;
    private BigDecimal restAmount;
    private BigDecimal minAmount;
    private String crcdFinalFour;
    private String Smc;
    private String Otp;
    private String token;
    private String _plainData;
    private String _certDN;
    private String _signedData;
    private String conversationId;


    private String Smc_RC;//手机交易码
    private String Otp_RC;//动态口令

    private String	deviceInfo	;//设备信息
    private String  deviceInfo_RC;
    private String	devicePrint	;//

    private String activ;//	控件取值	string
    private String	state;//	控件取值	string

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

    public BigDecimal getLowAmt() {
        return lowAmt;
    }

    public void setLowAmt(BigDecimal lowAmt) {
        this.lowAmt = lowAmt;
    }

    public BigDecimal getInstmtCharge() {
        return instmtCharge;
    }

    public void setInstmtCharge(BigDecimal instmtCharge) {
        this.instmtCharge = instmtCharge;
    }

    public BigDecimal getFirstInAmount() {
        return firstInAmount;
    }

    public void setFirstInAmount(BigDecimal firstInAmount) {
        this.firstInAmount = firstInAmount;
    }

    public BigDecimal getRestPerTimeInAmount() {
        return restPerTimeInAmount;
    }

    public void setRestPerTimeInAmount(BigDecimal restPerTimeInAmount) {
        this.restPerTimeInAmount = restPerTimeInAmount;
    }

    public BigDecimal getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(BigDecimal restAmount) {
        this.restAmount = restAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public String getCrcdFinalFour() {
        return crcdFinalFour;
    }

    public void setCrcdFinalFour(String crcdFinalFour) {
        this.crcdFinalFour = crcdFinalFour;
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
