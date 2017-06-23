package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model;

/**
 * PsnLOANChangeLoanERepayAccountSubmit变更（中E贷）还款账户提交交易，请求参数
 * Created by xintong on 2016/6/24.
 */
public class ChangeAccountSubmitReq {

    //创建交易会话后的id
    private String conversationId;
    //手机交易码,加密，（可选参数）
    private String Smc;
    //动态口令,加密，（可选参数）
    private String Otp;
    //防重机制，通过PSNGetTokenId接口获取
    private String token;
    //CA认证生成的密文,（可选参数）
    private String _signedData;
    //额度编号
    private String quoteNo;
    //原还款账户对应卡号
    private String oldPayCardNum;
    //新还款账户
    private String newPayAccountNum;
    //新还款账户ID
    private String newPayAccountId;
    //贷款品种
    private String loanType;
    //币种,同PsnLOANQuoteDetailEQuery接口中的返回字段
    private String currency;

    //贷款账号
    private String loanActNum;
    //原贷款账号
    private String oldPayAccountNum;
    //贷款币种
    private String currencyCode;
    //钞汇标识
    private String cashRemit;


    //新添字段  用于获取操作员ID
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;

    private String Smc_RC;
    private String Otp_RC;

    private String activ;
    private String state;
    
    private String factorId;

    public String getFactorId() {
		return factorId;
	}

	public void setFactorId(String factorId) {
		this.factorId = factorId;
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

    public String getLoanActNum() {
        return loanActNum;
    }

    public void setLoanActNum(String loanActNum) {
        this.loanActNum = loanActNum;
    }

    public String getOldPayAccountNum() {
        return oldPayAccountNum;
    }

    public void setOldPayAccountNum(String oldPayAccountNum) {
        this.oldPayAccountNum = oldPayAccountNum;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
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

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public String getOldPayCardNum() {
        return oldPayCardNum;
    }

    public void setOldPayCardNum(String oldPayCardNum) {
        this.oldPayCardNum = oldPayCardNum;
    }

    public String getNewPayAccountNum() {
        return newPayAccountNum;
    }

    public void setNewPayAccountNum(String newPayAccountNum) {
        this.newPayAccountNum = newPayAccountNum;
    }

    public String getNewPayAccountId() {
        return newPayAccountId;
    }

    public void setNewPayAccountId(String newPayAccountId) {
        this.newPayAccountId = newPayAccountId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "ChangeAccountSubmitReq{" +
                "conversationId='" + conversationId + '\'' +
                ", Smc='" + Smc + '\'' +
                ", Otp='" + Otp + '\'' +
                ", token='" + token + '\'' +
                ", _signedData='" + _signedData + '\'' +
                ", quoteNo='" + quoteNo + '\'' +
                ", oldPayCardNum='" + oldPayCardNum + '\'' +
                ", newPayAccountNum='" + newPayAccountNum + '\'' +
                ", newPayAccountId='" + newPayAccountId + '\'' +
                ", loanType='" + loanType + '\'' +
                ", currency='" + currency + '\'' +
                ", loanActNum='" + loanActNum + '\'' +
                ", oldPayAccountNum='" + oldPayAccountNum + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", cashRemit='" + cashRemit + '\'' +
                '}';
    }
}
