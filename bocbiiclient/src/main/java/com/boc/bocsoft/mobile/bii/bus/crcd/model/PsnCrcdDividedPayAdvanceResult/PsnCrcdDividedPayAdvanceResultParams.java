package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceResult;

/**
 * Created by yangle on 2016/11/22.
 */
public class PsnCrcdDividedPayAdvanceResultParams {


    //账户标识
    private String accountId;
    //币种
    private String currencyCode;
    //分期计划
    private String instalmentPlan;
    //已入账期数
    private String incomeTimeCount;
    //已入账金额
    private String incomeAmount;
    //剩余未入账期数
    private String restTimeCount;
    //剩余未入账金额
    private String restAmount;
    //下次入账日期
    private String nextIncomeDate;
    //分期付款标识
    private String instmtFlag;
    //动态口令
    private String Otp;
    private String Otp_RC;
    //短信验证
    private String Smc;
    private String Smc_RC;
    //设备信息-加密
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;
    //安全工具的状态与加密键盘版本
    private String state;
    private String activ;
    //CA认证生成的密文
    private String _signedData;
    //防重机制token
    private String token;
    private String conversationId;//需要与“PsnCrcdDividedPayAdvanceConfirm“上送相同ConversationId

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getInstalmentPlan() {
        return instalmentPlan;
    }

    public void setInstalmentPlan(String instalmentPlan) {
        this.instalmentPlan = instalmentPlan;
    }

    public String getNextIncomeDate() {
        return nextIncomeDate;
    }

    public void setNextIncomeDate(String nextIncomeDate) {
        this.nextIncomeDate = nextIncomeDate;
    }

    public String getIncomeTimeCount() {
        return incomeTimeCount;
    }

    public void setIncomeTimeCount(String incomeTimeCount) {
        this.incomeTimeCount = incomeTimeCount;
    }

    public String getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(String incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getRestTimeCount() {
        return restTimeCount;
    }

    public void setRestTimeCount(String restTimeCount) {
        this.restTimeCount = restTimeCount;
    }

    public String getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(String restAmount) {
        this.restAmount = restAmount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInstmtFlag() {
        return instmtFlag;
    }

    public void setInstmtFlag(String instmtFlag) {
        this.instmtFlag = instmtFlag;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
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
}
