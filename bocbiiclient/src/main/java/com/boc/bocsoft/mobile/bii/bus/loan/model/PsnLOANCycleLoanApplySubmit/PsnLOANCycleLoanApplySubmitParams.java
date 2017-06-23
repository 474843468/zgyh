package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplySubmit;

/**
 * 贷款管理-循环/非循环类贷款（非中银E贷）-用款交易请求参数
 * Created by liuzc on 2016/8/23.
 */
public class PsnLOANCycleLoanApplySubmitParams {

    /**
     * conversationId : a6746c8d-2098-44f6-8ef7-86c650e6a532
     * Smc : 333222
     * Otp : 333222
     * loanType : 1047
     * loanActNum : 100940097865
     * availableAmount : 499944
     * currencyCode : CNY
     * amount : 1569.00
     * loanPeriod : 6
     * loanRate : 5.600000
     * loanCycleDrawdownDate : 2016/12/21
     * loanCycleMatDate : 2016/12/26
     * toAccountId : 40403028
     * loanCycleToActNum : 4563510100892770235
     * payAccount : 100760095454
     * payType : B
     * payCycle : 98
     * token : t5h2e21h
     */

    private String conversationId; //会话ID
    private String Smc; //手机交易码
    private String Otp; //动态口令
    private String loanType; //贷款品种
    private String _signedData; //CA认证生成的密文(可选参数)
    private String loanActNum; //贷款账号
    private String availableAmount; //可用金额
    private String currencyCode; //币种
    private String amount;  //本次用款金额
    private String loanPeriod; //贷款期限
    private String loanRate; //贷款利率
    private String loanCycleDrawdownDate; //放款截止日
    private String loanCycleMatDate; //贷款到期日
    private String toAccountId; //收款账户Id
    private String loanCycleToActNum;//收款账户
    private String payAccount; //还款账户
    private String payType; //还款方式
    private String payCycle; //还款周期
    private String token; //防重机制，通过PSNGetTokenId接口获取

    //新添字段  用于获取操作员ID
    private  String deviceInfo;
    private  String deviceInfo_RC;
    private  String devicePrint;

    private  String Smc_RC;
    private  String Otp_RC;

    //cfn版本号和状态
    private String activ;
    private String state;

    private String factorId;

    public String getFactorId() {
        return factorId;
    }

    public void setFactorId(String factorId) {
        this.factorId = factorId;
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
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String Smc) {
        this.Smc = Smc;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String Otp) {
        this.Otp = Otp;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanActNum() {
        return loanActNum;
    }

    public void setLoanActNum(String loanActNum) {
        this.loanActNum = loanActNum;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public String getLoanCycleDrawdownDate() {
        return loanCycleDrawdownDate;
    }

    public void setLoanCycleDrawdownDate(String loanCycleDrawdownDate) {
        this.loanCycleDrawdownDate = loanCycleDrawdownDate;
    }

    public String getLoanCycleMatDate() {
        return loanCycleMatDate;
    }

    public void setLoanCycleMatDate(String loanCycleMatDate) {
        this.loanCycleMatDate = loanCycleMatDate;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getLoanCycleToActNum() {
        return loanCycleToActNum;
    }

    public void setLoanCycleToActNum(String loanCycleToActNum) {
        this.loanCycleToActNum = loanCycleToActNum;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayCycle() {
        return payCycle;
    }

    public void setPayCycle(String payCycle) {
        this.payCycle = payCycle;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }
}
