package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepaySubmit;

import java.math.BigDecimal;

/**
 * PsnLOANAdvanceRepaySubmit提前还款提交交易,请求参数
 * Created by xintong on 2016/6/23.
 */
public class PsnLOANAdvanceRepaySubmitParams {

    //创建交易会话后的id
    private String conversationId;
    //贷款品种（必输）
    private String loanType;
    //贷款账号（必输）
    private String loanAccount;
    //币种（必输）
    private String currency;
    //贷款金额（必输）
    private BigDecimal loanAmount;
    //期限（必输）
    private Integer loanPeriod;
    //到期日（必输）
    private String loanToDate;
    //提前还款利息（必输）
    private BigDecimal advanceRepayInterest;
    //提前还款本金（必输）
    private BigDecimal advanceRepayCapital;
    //提前还款金额（必输）
    private BigDecimal repayAmount;
    //转出账户ID（必输）
    private String fromAccountId;
    //转出账号
    private String accountNumber;
    //贷款期限单位（必输）
    private String loanPeriodUnit;
    //剩余应还本金（必输）
    private BigDecimal remainCapital;
    //本期截止当前应还利息（必输）
    private BigDecimal thisIssueRepayInterest;
    //贷款剩余期数（必输）
    private String remainIssue;
    //手续费
    private BigDecimal charges;
    //本期还款日（必输）
    private String thisIssueRepayDate;
    //本期应还款总额（本金+利息）（必输）
    private BigDecimal thisIssueRepayAmount;
    //提前还款后每期应还金额
    private BigDecimal afterRepayissueAmount;
    //计息方式(还款方式)
    private String interestType;
    //提前还款后剩余还款额
    private BigDecimal afterRepayRemainAmount;
    //提前还款后剩余期数
    private String afterRepayissues;
    /**
     * 线上标识
     * 0：非线上
     * 1：线上
     * 此字段取“提前还款账户查询”接口返回值
     */
    private String onlineFlag;
    /**
     *循环类型
     * R：循环动用
     * F：不循环，一次动用
     * M：不循环，分次动用
     * 此字段取“提前还款账户查询”接口返回值
     */
    private String cycleType;
    //手机交易码（必输），加密,(可选参数)
    private String Smc;
    //动态口令（必输），加密,(可选参数)，通过PSNGetTokenId接口获取
    private String Otp;
    //防重机制，通过PSNGetTokenId接口获取（必输）
    private String token;
    //CA认证生成的密文（必输），CA认证生成的密文，(可选参数)
    private String _signedData;


    //新添字段  用于获取操作员ID
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;
    private String Smc_RC;
    private String Otp_RC;

    private String activ;
    private String state;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(Integer loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getLoanToDate() {
        return loanToDate;
    }

    public void setLoanToDate(String loanToDate) {
        this.loanToDate = loanToDate;
    }

    public BigDecimal getAdvanceRepayInterest() {
        return advanceRepayInterest;
    }

    public void setAdvanceRepayInterest(BigDecimal advanceRepayInterest) {
        this.advanceRepayInterest = advanceRepayInterest;
    }

    public BigDecimal getAdvanceRepayCapital() {
        return advanceRepayCapital;
    }

    public void setAdvanceRepayCapital(BigDecimal advanceRepayCapital) {
        this.advanceRepayCapital = advanceRepayCapital;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getAccoutNumber() {
        return accountNumber;
    }

    public void setAccoutNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLoanPeriodUnit() {
        return loanPeriodUnit;
    }

    public void setLoanPeriodUnit(String loanPeriodUnit) {
        this.loanPeriodUnit = loanPeriodUnit;
    }

    public BigDecimal getRemainCapital() {
        return remainCapital;
    }

    public void setRemainCapital(BigDecimal remainCapital) {
        this.remainCapital = remainCapital;
    }

    public BigDecimal getThisIssueRepayInterest() {
        return thisIssueRepayInterest;
    }

    public void setThisIssueRepayInterest(BigDecimal thisIssueRepayInterest) {
        this.thisIssueRepayInterest = thisIssueRepayInterest;
    }

    public String getRemainIssue() {
        return remainIssue;
    }

    public void setRemainIssue(String remainIssue) {
        this.remainIssue = remainIssue;
    }

    public BigDecimal getCharges() {
        return charges;
    }

    public void setCharges(BigDecimal charges) {
        this.charges = charges;
    }

    public String getThisIssueRepayDate() {
        return thisIssueRepayDate;
    }

    public void setThisIssueRepayDate(String thisIssueRepayDate) {
        this.thisIssueRepayDate = thisIssueRepayDate;
    }

    public BigDecimal getThisIssueRepayAmount() {
        return thisIssueRepayAmount;
    }

    public void setThisIssueRepayAmount(BigDecimal thisIssueRepayAmount) {
        this.thisIssueRepayAmount = thisIssueRepayAmount;
    }

    public BigDecimal getAfterRepayissueAmount() {
        return afterRepayissueAmount;
    }

    public void setAfterRepayissueAmount(BigDecimal afterRepayissueAmount) {
        this.afterRepayissueAmount = afterRepayissueAmount;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public BigDecimal getAfterRepayRemainAmount() {
        return afterRepayRemainAmount;
    }

    public void setAfterRepayRemainAmount(BigDecimal afterRepayRemainAmount) {
        this.afterRepayRemainAmount = afterRepayRemainAmount;
    }

    public String getAfterRepayissues() {
        return afterRepayissues;
    }

    public void setAfterRepayissues(String afterRepayissues) {
        this.afterRepayissues = afterRepayissues;
    }

    public String getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(String onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
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
}
