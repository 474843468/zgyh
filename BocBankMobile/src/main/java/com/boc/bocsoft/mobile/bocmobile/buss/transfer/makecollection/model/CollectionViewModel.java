package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 主动收款
 * Created by zhx on 2016/7/18
 */
public class CollectionViewModel implements Parcelable {

    /**
     * 币种
     */
    private String currency;
    /**
     * 收款金额
     */
    private String notifyPayeeAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 付款人客户号
     */
    private String payerCustId;
    /**
     * 收款人账ID
     */
    private String toAccountId;
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 付款人类型
     */
    private String payerChannel;
    /**
     * 收款人手机
     */
    private String payeeMobile;
    /**
     * 收款人姓名
     */
    private String payeeName;
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
    // 响应
    //======================================//
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

    public String getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(String payerCustId) {
        this.payerCustId = payerCustId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.currency);
        dest.writeString(this.notifyPayeeAmount);
        dest.writeString(this.remark);
        dest.writeString(this.payerCustId);
        dest.writeString(this.toAccountId);
        dest.writeString(this.payeeActno);
        dest.writeString(this.payerMobile);
        dest.writeString(this.payerName);
        dest.writeString(this.payerChannel);
        dest.writeString(this.payeeMobile);
        dest.writeString(this.payeeName);
        dest.writeString(this.Otp);
        dest.writeString(this.token);
        dest.writeString(this._signedData);
        dest.writeString(this.accountType);
        dest.writeString(this.accountIbkNum);
        dest.writeSerializable(this.amount);
        dest.writeString(this.accountNum);
        dest.writeLong(this.notifyId);
    }

    public CollectionViewModel() {
    }

    private CollectionViewModel(Parcel in) {
        this.currency = in.readString();
        this.notifyPayeeAmount = in.readString();
        this.remark = in.readString();
        this.payerCustId = in.readString();
        this.toAccountId = in.readString();
        this.payeeActno = in.readString();
        this.payerMobile = in.readString();
        this.payerName = in.readString();
        this.payerChannel = in.readString();
        this.payeeMobile = in.readString();
        this.payeeName = in.readString();
        this.Otp = in.readString();
        this.token = in.readString();
        this._signedData = in.readString();
        this.accountType = in.readString();
        this.accountIbkNum = in.readString();
        this.amount = (BigDecimal) in.readSerializable();
        this.accountNum = in.readString();
        this.notifyId = in.readLong();
    }

    public static final Parcelable.Creator<CollectionViewModel> CREATOR = new Parcelable.Creator<CollectionViewModel>() {
        public CollectionViewModel createFromParcel(Parcel source) {
            return new CollectionViewModel(source);
        }

        public CollectionViewModel[] newArray(int size) {
            return new CollectionViewModel[size];
        }
    };
}