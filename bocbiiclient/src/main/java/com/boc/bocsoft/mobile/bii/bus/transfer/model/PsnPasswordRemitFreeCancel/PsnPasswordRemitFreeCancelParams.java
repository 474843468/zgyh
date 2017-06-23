package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeCancel;

import java.math.BigDecimal;

/**
 * 密码汇款取消汇款（ ATM无卡取现取消时上送服务码PB046, 密码汇款取消时上送PB043）
 * ATM无卡取款撤销请求
 *
 * Created by liuweidong on 2016/6/25.
 */
public class PsnPasswordRemitFreeCancelParams {
    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 汇款类型 1：ATM无卡取现；0：密码汇款
     */
    private String freeRemitType;
    /**
     * 汇款编号
     */
    private String remitNo;
    /**
     * 付款人账号
     */
    private String fromActNumber;
    /**
     * 付款卡号
     */
    private String fromCardNo;
    /**
     * 转账金额
     */
    private BigDecimal amount;
    /**
     * 币种
     */
    private String currencyCode;
    /**
     * 付款账户类型
     */
    private String fromActType;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 取款方式 0：现金1：转账  2：代理点解付
     */
    private String receiptMode;
    /**
     * 收款人支取方式 1、 凭密支取 2、 凭手机验证码 3、 凭密码+手机验证码
     */
    private String drawMode;
    /**
     * 附言 小于等于50个中英文字符
     */
    private String furInf;
    /**
     * 防重机制
     */
    private String token;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFreeRemitType() {
        return freeRemitType;
    }

    public void setFreeRemitType(String freeRemitType) {
        this.freeRemitType = freeRemitType;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getFromActNumber() {
        return fromActNumber;
    }

    public void setFromActNumber(String fromActNumber) {
        this.fromActNumber = fromActNumber;
    }

    public String getFromCardNo() {
        return fromCardNo;
    }

    public void setFromCardNo(String fromCardNo) {
        this.fromCardNo = fromCardNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getFromActType() {
        return fromActType;
    }

    public void setFromActType(String fromActType) {
        this.fromActType = fromActType;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getReceiptMode() {
        return receiptMode;
    }

    public void setReceiptMode(String receiptMode) {
        this.receiptMode = receiptMode;
    }

    public String getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(String drawMode) {
        this.drawMode = drawMode;
    }

    public String getFurInf() {
        return furInf;
    }

    public void setFurInf(String furInf) {
        this.furInf = furInf;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
