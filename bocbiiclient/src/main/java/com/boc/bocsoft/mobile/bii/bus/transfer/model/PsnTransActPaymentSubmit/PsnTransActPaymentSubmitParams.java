package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit;

import java.math.BigDecimal;

/**
 * 主动收款付款提交
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActPaymentSubmitParams {
    /**
     * 付款人账户ID
     */
    private int fromAccountId;
    /**
     * 指令序号
     */
    private String notifyId;
    /**
     * 实付金额
     */
    private String notifyTrfAmount;
    /**
     * 付款币种
     */
    private String notifyTrfCur;
    /**
     * 付款币种
     */
    private String payeeActno;
    /**
     * 催款日期
     */
    private String notifyCreateDate;
    /**
     * 付款日期
     */
    private String notifyCurrentDate;
    /**
     * 请求金额
     */
    private BigDecimal notifyRequestAmount;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 付款人客户号
     */
    private String payerCustId;
    /**
     * 发起渠道
     */
    private String notifyCreateChannel;
    /**
     * 手机交易码
     */
    private String Smc;
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

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getNotifyTrfAmount() {
        return notifyTrfAmount;
    }

    public void setNotifyTrfAmount(String notifyTrfAmount) {
        this.notifyTrfAmount = notifyTrfAmount;
    }

    public String getNotifyTrfCur() {
        return notifyTrfCur;
    }

    public void setNotifyTrfCur(String notifyTrfCur) {
        this.notifyTrfCur = notifyTrfCur;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getNotifyCreateDate() {
        return notifyCreateDate;
    }

    public void setNotifyCreateDate(String notifyCreateDate) {
        this.notifyCreateDate = notifyCreateDate;
    }

    public String getNotifyCurrentDate() {
        return notifyCurrentDate;
    }

    public void setNotifyCurrentDate(String notifyCurrentDate) {
        this.notifyCurrentDate = notifyCurrentDate;
    }

    public BigDecimal getNotifyRequestAmount() {
        return notifyRequestAmount;
    }

    public void setNotifyRequestAmount(BigDecimal notifyRequestAmount) {
        this.notifyRequestAmount = notifyRequestAmount;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(String payerCustId) {
        this.payerCustId = payerCustId;
    }

    public String getNotifyCreateChannel() {
        return notifyCreateChannel;
    }

    public void setNotifyCreateChannel(String notifyCreateChannel) {
        this.notifyCreateChannel = notifyCreateChannel;
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
