package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import java.math.BigDecimal;

/**
 * ViewModel:主动收款付款
 * Created by zhx on 2016/7/7
 */
public class PaymentSubmitViewModel {
    /**
     * 付款人账户ID
     */
    private int fromAccountId;
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

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    /**
     * 交易状态
     */
    private String status;
    /**
     * 交易序号
     */
    private Object transactionId;
    /**
     * 催款日期
     */
    private String createDate;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 付款人账号
     */
    private Object payerAccountNumber;
    /**
     * 付款日期
     */
    private Object paymentDate;
    /**
     * 收款人账号
     */
    private String payeeAccountNumber;
    /**
     * 付款人客户号
     */
    private String payerCustomerId;
    /**
     * 手续费
     */
    private BigDecimal commissionCharge;
    /**
     * 备注
     */
    private String furInfo;
    /**
     * 收款人联行号
     */
    private String payeeIbk;
    /**
     * 收款人手机
     */
    private String payeeMobile;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 实收费用电汇费电汇费
     */
    private BigDecimal postage;
    /**
     * 付款人联行号
     */
    private Object payerIbknum;
    /**
     * 收款人账户类型
     */
    private String payeeAccountType;
    /**
     * 付款人账户类型
     */
    private String payerAccountType;
    /**
     * 实付金额
     */
    private BigDecimal trfAmount;
    /**
     * 指令序号
     */
    private String notifyId;
    /**
     * 币种
     */
    private String trfCur;
    /**
     * 催款金额
     */
    private BigDecimal requestAmount;
    /**
     * 发起渠道
     */
    private String createChannel;
    /**
     * 付款渠道
     */
    private String trfChannel;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Object transactionId) {
        this.transactionId = transactionId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Object getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(Object payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
    }

    public Object getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Object paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    public String getPayerCustomerId() {
        return payerCustomerId;
    }

    public void setPayerCustomerId(String payerCustomerId) {
        this.payerCustomerId = payerCustomerId;
    }

    public BigDecimal getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(BigDecimal commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public String getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
    }

    public String getPayeeIbk() {
        return payeeIbk;
    }

    public void setPayeeIbk(String payeeIbk) {
        this.payeeIbk = payeeIbk;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public Object getPayerIbknum() {
        return payerIbknum;
    }

    public void setPayerIbknum(Object payerIbknum) {
        this.payerIbknum = payerIbknum;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getPayerAccountType() {
        return payerAccountType;
    }

    public void setPayerAccountType(String payerAccountType) {
        this.payerAccountType = payerAccountType;
    }

    public BigDecimal getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(BigDecimal trfAmount) {
        this.trfAmount = trfAmount;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getTrfCur() {
        return trfCur;
    }

    public void setTrfCur(String trfCur) {
        this.trfCur = trfCur;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(BigDecimal requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getCreateChannel() {
        return createChannel;
    }

    public void setCreateChannel(String createChannel) {
        this.createChannel = createChannel;
    }

    public String getTrfChannel() {
        return trfChannel;
    }

    public void setTrfChannel(String trfChannel) {
        this.trfChannel = trfChannel;
    }
}
