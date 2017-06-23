package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify;

import java.math.BigDecimal;

/**
 * 主动收款付款预交易
 * Created by zhx on 2016/6/29.
 * 所需要的参数调用付款指令记录详情接口(PsnTransActPaymentOrderDetail)获得。
 */
public class PsnTransActPaymentVerifyParams {
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
     * 指令序号
     */
    private String notifyId;
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
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 收款人账户账号
     */
    private String payeeActno;
    /**
     * 安全因子组合id
     */
    private String _combinId;

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

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
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

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }
}
