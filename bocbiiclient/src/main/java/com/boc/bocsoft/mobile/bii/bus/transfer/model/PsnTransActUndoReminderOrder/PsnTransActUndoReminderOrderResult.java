package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActUndoReminderOrder;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * Result:撤消催款指令
 * Created by zhx on 2016/7/6
 */
public class PsnTransActUndoReminderOrderResult {
    /**
     * 交易状态
     */
    private String status;
    /**
     * 催款日期
     */
    private LocalDate createDate;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 付款人姓名
     */
    private String payerName;
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
     * 付款账户类型
     */
    private String payeeAccountType;
    /**
     * 指令序号
     */
    private int notifyId;
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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
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

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public int getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(int notifyId) {
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
