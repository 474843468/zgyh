package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by wangtong on 2016/7/28.
 */
public class QrcodeOperateModel implements Parcelable {

    private String trfAmount;
    private String payeeMobel;
    private String payeeName;
    private String payeeAccount;
    private String fromAccountNum;
    private String tips;
    private String commissionCharge;
    private String finalCommissionCharge;
    private BigDecimal remainAmount;
    private String remainCurrency;
    private String fromIbkNum;
    private long transactionId;

    public String getFromIbkNum() {
        return fromIbkNum;
    }

    public void setFromIbkNum(String fromIbkNum) {
        this.fromIbkNum = fromIbkNum;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getRemainCurrency() {
        return remainCurrency;
    }

    public void setRemainCurrency(String remainCurrency) {
        this.remainCurrency = remainCurrency;
    }

    public String getFromAccountNum() {
        return fromAccountNum;
    }

    public void setFromAccountNum(String fromAccountNum) {
        this.fromAccountNum = fromAccountNum;
    }

    public String getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(String commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public String getFinalCommissionCharge() {
        return finalCommissionCharge;
    }

    public void setFinalCommissionCharge(String finalCommissionCharge) {
        this.finalCommissionCharge = finalCommissionCharge;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(String trfAmount) {
        this.trfAmount = trfAmount;
    }

    public String getPayeeMobel() {
        return payeeMobel;
    }

    public void setPayeeMobel(String payeeMobel) {
        this.payeeMobel = payeeMobel;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trfAmount);
        dest.writeString(this.payeeMobel);
        dest.writeString(this.payeeName);
        dest.writeString(this.payeeAccount);
        dest.writeString(this.fromAccountNum);
        dest.writeString(this.tips);
        dest.writeString(this.commissionCharge);
        dest.writeString(this.finalCommissionCharge);
        dest.writeSerializable(this.remainAmount);
        dest.writeString(this.remainCurrency);
        dest.writeString(this.fromIbkNum);
        dest.writeLong(this.transactionId);
    }

    public QrcodeOperateModel() {
    }

    protected QrcodeOperateModel(Parcel in) {
        this.trfAmount = in.readString();
        this.payeeMobel = in.readString();
        this.payeeName = in.readString();
        this.payeeAccount = in.readString();
        this.fromAccountNum = in.readString();
        this.tips = in.readString();
        this.commissionCharge = in.readString();
        this.finalCommissionCharge = in.readString();
        this.remainAmount = (BigDecimal) in.readSerializable();
        this.remainCurrency = in.readString();
        this.fromIbkNum = in.readString();
        this.transactionId = in.readLong();
    }

    public static final Creator<QrcodeOperateModel> CREATOR = new Creator<QrcodeOperateModel>() {
        @Override
        public QrcodeOperateModel createFromParcel(Parcel source) {
            return new QrcodeOperateModel(source);
        }

        @Override
        public QrcodeOperateModel[] newArray(int size) {
            return new QrcodeOperateModel[size];
        }
    };
}
