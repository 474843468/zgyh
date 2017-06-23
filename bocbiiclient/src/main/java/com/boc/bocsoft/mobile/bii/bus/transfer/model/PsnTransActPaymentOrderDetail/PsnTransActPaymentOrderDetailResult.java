package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail;

import org.threeten.bp.LocalDateTime;

/**
 * 付款指令记录详情
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActPaymentOrderDetailResult {
    /**
     * 交易状态
     */
    private String status;
    /**
     * 交易序号
     */
    private int transactionId;
    /**
     * 催款日期
     */
    private LocalDateTime createDate;
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
    private String payerAccountNumber;
    /**
     * 付款日期
     */
    private String paymentDate;
    /**
     * 收款人账号
     */
    private String payeeAccountNumber;
    /**
     * 付款人客户号
     */
    private String payerCustomerId;
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
     * 付款人联行号
     */
    private String payerIbknum;
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
    private double trfAmount;
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
    private double requestAmount;
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

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
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

    public String getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(String payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
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

    public String getPayerIbknum() {
        return payerIbknum;
    }

    public void setPayerIbknum(String payerIbknum) {
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

    public double getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(double trfAmount) {
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

    public double getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(double requestAmount) {
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
