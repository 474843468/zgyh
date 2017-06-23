package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import java.math.BigDecimal;

/**
 * ViewModel:主动收款提交
 * Created by zhx on 2016/8/11
 */
public class PsnTransActCollectionSubmitViewModel {
    /**
     * 收款人账ID
     */
    private String toAccountId;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 币种
     */
    private String currency;
    /**
     * 收款金额
     */
    private String notifyPayeeAmount;
    /**
     * 付款人客户号
     */
    private String payerCustId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 收款人手机
     */
    private String payeeMobile;
    /**
     * 付款人类型
     */
    private String payerChannel;
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * 动态口令
     */
    private String Otp;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;
    /**
     * CA认证生成的密文
     */
    private String _signedData;

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    //    /**
    //     * 币种
    //     */
    //    private String currency;
    /**
     * 收款账户类型
     */
    private String accountType;
    /**
     * 收款账户联行号
     */
    private String accountIbkNum;
    /**
     * 转账金额
     */
    private BigDecimal amount;
    /**
     * 收款账户号
     */
    private String accountNum;
    /**
     * 指令序号
     */
    private long notifyId;
    //    /**
    //     * 付款人客户号
    //     */
    //    private int payerCustId;

    private String state; // 此字段传入SecurityVerity.SECURITY_VERIFY_STATE即可
    private String deviceInfo;
    private String Smc;
    private String devicePrint;


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotifyPayeeAmount() {
        return notifyPayeeAmount;
    }

    public void setNotifyPayeeAmount(String notifyPayeeAmount) {
        this.notifyPayeeAmount = notifyPayeeAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(String payerCustId) {
        this.payerCustId = payerCustId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerChannel() {
        return payerChannel;
    }

    public void setPayerChannel(String payerChannel) {
        this.payerChannel = payerChannel;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(long notifyId) {
        this.notifyId = notifyId;
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

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }
}
