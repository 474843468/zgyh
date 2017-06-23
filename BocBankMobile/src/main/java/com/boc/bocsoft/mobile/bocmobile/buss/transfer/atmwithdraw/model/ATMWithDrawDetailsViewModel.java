package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by liuweidong on 2016/6/30.
 */
public class ATMWithDrawDetailsViewModel implements Parcelable {
    /**
     * 汇款编号 必输，9~13位数字
     */
    private String remitNo;
    /**
     * 汇款人姓名
     */
    private String payerName;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 交易类型 必输，CL-取消汇款、OK-收款、QU-查询
     */
    private String freeRemitTrsType;

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getFreeRemitTrsType() {
        return freeRemitTrsType;
    }

    public void setFreeRemitTrsType(String freeRemitTrsType) {
        this.freeRemitTrsType = freeRemitTrsType;
    }


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remitNo);
        dest.writeString(this.payerName);
        dest.writeString(this.payeeName);
        dest.writeString(this.freeRemitTrsType);
        dest.writeString(this.comment);
        dest.writeString(this.status);
        dest.writeString(this.branchId);
        dest.writeString(this.paymentDate);
        dest.writeString(this.cardNo);
        dest.writeInt(this.fromActId);
        dest.writeString(this.fromName);
        dest.writeString(this.fromActNumber);
        dest.writeString(this.fromActType);
        dest.writeSerializable(this.paymentAmount);
        dest.writeString(this.paymentCashRemit);
        dest.writeString(this.mobileVerifyCode);
        dest.writeString(this.remitNumber);
        dest.writeString(this.toName);
        dest.writeString(this.toMobile);
        dest.writeString(this.drawMode);
        dest.writeString(this.drawPassword);
        dest.writeString(this.toActNumber);
        dest.writeString(this.toActType);
        dest.writeString(this.inOutward);
        dest.writeString(this.receiptState);
        dest.writeString(this.receiptDate);
        dest.writeString(this.toActId);
        dest.writeString(this.fromBranchId);
        dest.writeString(this.paymentCode);
        dest.writeString(this.dueDate);
    }

    public ATMWithDrawDetailsViewModel() {
    }

    protected ATMWithDrawDetailsViewModel(Parcel in) {
        this.remitNo = in.readString();
        this.payerName = in.readString();
        this.payeeName = in.readString();
        this.freeRemitTrsType = in.readString();
        this.comment = in.readString();
        this.status = in.readString();
        this.branchId = in.readString();
        this.paymentDate = in.readString();
        this.cardNo = in.readString();
        this.fromActId = in.readInt();
        this.fromName = in.readString();
        this.fromActNumber = in.readString();
        this.fromActType = in.readString();
        this.paymentAmount = (BigDecimal) in.readSerializable();
        this.paymentCashRemit = in.readString();
        this.mobileVerifyCode = in.readString();
        this.remitNumber = in.readString();
        this.toName = in.readString();
        this.toMobile = in.readString();
        this.drawMode = in.readString();
        this.drawPassword = in.readString();
        this.toActNumber = in.readString();
        this.toActType = in.readString();
        this.inOutward = in.readString();
        this.receiptState = in.readString();
        this.receiptDate = in.readString();
        this.toActId = in.readString();
        this.fromBranchId = in.readString();
        this.paymentCode = in.readString();
        this.dueDate = in.readString();
    }

    public static final Parcelable.Creator<ATMWithDrawDetailsViewModel> CREATOR = new Parcelable.Creator<ATMWithDrawDetailsViewModel>() {
        @Override
        public ATMWithDrawDetailsViewModel createFromParcel(Parcel source) {
            return new ATMWithDrawDetailsViewModel(source);
        }

        @Override
        public ATMWithDrawDetailsViewModel[] newArray(int size) {
            return new ATMWithDrawDetailsViewModel[size];
        }
    };
}
