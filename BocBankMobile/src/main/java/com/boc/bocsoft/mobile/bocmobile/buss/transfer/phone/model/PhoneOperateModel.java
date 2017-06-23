package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by wangtong on 2016/7/28.
 */
public class PhoneOperateModel implements Parcelable {

    private String trfAmount;
    private String payeeMobel;
    private String payeeName;
    private String tips;
    private String payeeLbk;
    private String fromAccoutnNum;
    private String fromAccoutnName;
    private String reminCurrency;
    private String isBundAccount;
    private String transNum;
    private String payeeAccount;
    private BigDecimal reminAmount;
    private String commisionCharge;

    public String getFromAccoutnName() {
        return fromAccoutnName;
    }

    public void setFromAccoutnName(String fromAccoutnName) {
        this.fromAccoutnName = fromAccoutnName;
    }

    public String getPayeeLbk() {
        return payeeLbk;
    }

    public void setPayeeLbk(String payeeLbk) {
        this.payeeLbk = payeeLbk;
    }

    public String getCommisionCharge() {
        return commisionCharge;
    }

    public void setCommisionCharge(String commisionCharge) {
        this.commisionCharge = commisionCharge;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public BigDecimal getReminAmount() {
        return reminAmount;
    }

    public void setReminAmount(BigDecimal reminAmount) {
        this.reminAmount = reminAmount;
    }

    public String getIsBundAccount() {
        return isBundAccount;
    }

    public void setIsBundAccount(String isBundAccount) {
        this.isBundAccount = isBundAccount;
    }

    public String getFromAccoutnNum() {
        return fromAccoutnNum;
    }

    public void setFromAccoutnNum(String fromAccoutnNum) {
        this.fromAccoutnNum = fromAccoutnNum;
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

    public String getReminCurrency() {
        return reminCurrency;
    }

    public void setReminCurrency(String reminCurrency) {
        this.reminCurrency = reminCurrency;
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
        dest.writeString(this.tips);
        dest.writeString(this.payeeLbk);
        dest.writeString(this.fromAccoutnNum);
        dest.writeString(this.reminCurrency);
        dest.writeString(this.isBundAccount);
        dest.writeString(this.transNum);
        dest.writeString(this.payeeAccount);
        dest.writeSerializable(this.reminAmount);
        dest.writeString(this.commisionCharge);
    }

    public PhoneOperateModel() {
    }

    protected PhoneOperateModel(Parcel in) {
        this.trfAmount = in.readString();
        this.payeeMobel = in.readString();
        this.payeeName = in.readString();
        this.tips = in.readString();
        this.payeeLbk = in.readString();
        this.fromAccoutnNum = in.readString();
        this.reminCurrency = in.readString();
        this.isBundAccount = in.readString();
        this.transNum = in.readString();
        this.payeeAccount = in.readString();
        this.reminAmount = (BigDecimal) in.readSerializable();
        this.commisionCharge = in.readString();
    }

    public static final Creator<PhoneOperateModel> CREATOR = new Creator<PhoneOperateModel>() {
        @Override
        public PhoneOperateModel createFromParcel(Parcel source) {
            return new PhoneOperateModel(source);
        }

        @Override
        public PhoneOperateModel[] newArray(int size) {
            return new PhoneOperateModel[size];
        }
    };
}
