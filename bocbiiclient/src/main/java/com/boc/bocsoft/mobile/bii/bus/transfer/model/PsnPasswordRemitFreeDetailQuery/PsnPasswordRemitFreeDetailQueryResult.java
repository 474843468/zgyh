package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeDetailQuery;

import java.math.BigDecimal;

/**
 * 密码汇款详情查询（包括收款，取消汇款和交易详情查询）
 * ATM无卡取款查询详情响应
 *
 * Created by liuweidong on 2016/6/25.
 */
public class PsnPasswordRemitFreeDetailQueryResult {

    /**
     * 汇款用途
     */
    private String comment;
    /**
     * 收款状态
     OU-已汇款未收款(已预约未取现)
     CR-已汇款未收款(已预约未取现)
     OK-已成功收款（已成功取现）
     CL-已取消汇款（已取消取现）
     L3-密码错3次锁定
     L6-永久锁定
     */
    private String status;
    /**
     *
     */
    private String branchId;
    /**
     *
     */
    private String paymentDate;
    /**
     * 汇出账户
     */
    private String cardNo;
    private int fromActId;
    private String fromName;
    /**
     * 付款人账号
     */
    private String fromActNumber;
    /**
     * 付款人账号类型
     */
    private String fromActType;
    /**
     * 转账金额
     */
    private BigDecimal paymentAmount;
    private String paymentCashRemit;
    private String mobileVerifyCode;
    private String remitNumber;
    /**
     * 收款人姓名
     */
    private String toName;
    /**
     * 收款人手机号
     */
    private String toMobile;
    /**
     * 验证方式
     1-	预留密码
     2-	手机验证码
     3-	预留密码和手机验证码
     */
    private String drawMode;
    private String drawPassword;
    /**
     * 收款账号
     */
    private String toActNumber;
    private String toActType;
    private String inOutward;
    private String receiptState;
    /**
     * 收款日期
     */
    private String receiptDate;
    private String toActId;
    private String fromBranchId;
    /**
     * 币种
     */
    private String paymentCode;
    /**
     * 到期日期 仅当交易状态为“OU-已汇款未收款/CR-解付冲正/L3-密码错3次锁定”时，显示到期日期
     */
    private String dueDate;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getFromActId() {
        return fromActId;
    }

    public void setFromActId(int fromActId) {
        this.fromActId = fromActId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromActNumber() {
        return fromActNumber;
    }

    public void setFromActNumber(String fromActNumber) {
        this.fromActNumber = fromActNumber;
    }

    public String getFromActType() {
        return fromActType;
    }

    public void setFromActType(String fromActType) {
        this.fromActType = fromActType;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentCashRemit() {
        return paymentCashRemit;
    }

    public void setPaymentCashRemit(String paymentCashRemit) {
        this.paymentCashRemit = paymentCashRemit;
    }

    public String getMobileVerifyCode() {
        return mobileVerifyCode;
    }

    public void setMobileVerifyCode(String mobileVerifyCode) {
        this.mobileVerifyCode = mobileVerifyCode;
    }

    public String getRemitNumber() {
        return remitNumber;
    }

    public void setRemitNumber(String remitNumber) {
        this.remitNumber = remitNumber;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToMobile() {
        return toMobile;
    }

    public void setToMobile(String toMobile) {
        this.toMobile = toMobile;
    }

    public String getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(String drawMode) {
        this.drawMode = drawMode;
    }

    public String getDrawPassword() {
        return drawPassword;
    }

    public void setDrawPassword(String drawPassword) {
        this.drawPassword = drawPassword;
    }

    public String getToActNumber() {
        return toActNumber;
    }

    public void setToActNumber(String toActNumber) {
        this.toActNumber = toActNumber;
    }

    public String getToActType() {
        return toActType;
    }

    public void setToActType(String toActType) {
        this.toActType = toActType;
    }

    public String getInOutward() {
        return inOutward;
    }

    public void setInOutward(String inOutward) {
        this.inOutward = inOutward;
    }

    public String getReceiptState() {
        return receiptState;
    }

    public void setReceiptState(String receiptState) {
        this.receiptState = receiptState;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getToActId() {
        return toActId;
    }

    public void setToActId(String toActId) {
        this.toActId = toActId;
    }

    public String getFromBranchId() {
        return fromBranchId;
    }

    public void setFromBranchId(String fromBranchId) {
        this.fromBranchId = fromBranchId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
