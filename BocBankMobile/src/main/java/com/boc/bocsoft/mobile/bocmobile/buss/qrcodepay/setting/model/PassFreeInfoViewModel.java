package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 小额免密信息
 * Created by wangf on 2016/9/10.
 */
public class PassFreeInfoViewModel implements Parcelable {

	//信用卡免密开通标识
    private String creditCardFlag;
    //借记卡免密开通标识
    private String debitCardFlag;
    //信用卡免密金额
    private String creditCardPassFreeAmount;
    //借记卡免密金额
    private String debitCardPassFreeAmount;

    //免密开通标识
    private String passFreeFlag;
    //免密金额
    private String passFreeAmount;


    public String getCreditCardFlag() {
        return creditCardFlag;
    }

    public void setCreditCardFlag(String creditCardFlag) {
    	this.creditCardFlag = creditCardFlag;
    }

    public String getDebitCardFlag() {
        return debitCardFlag;
    }

    public void setDebitCardFlag(String debitCardFlag) {
    	this.debitCardFlag = debitCardFlag;
    }

    public String getCreditCardPassFreeAmount() {
        return creditCardPassFreeAmount;
    }

    public void setCreditCardPassFreeAmount(String creditCardPassFreeAmount) {
    	this.creditCardPassFreeAmount = creditCardPassFreeAmount;
    }

    public String getDebitCardPassFreeAmount() {
        return debitCardPassFreeAmount;
    }

    public void setDebitCardPassFreeAmount(String debitCardPassFreeAmount) {
    	this.debitCardPassFreeAmount = debitCardPassFreeAmount;
    }

    public String getPassFreeAmount() {
        return passFreeAmount;
    }

    public void setPassFreeAmount(String passFreeAmount) {
        this.passFreeAmount = passFreeAmount;
    }

    public String getPassFreeFlag() {
        return passFreeFlag;
    }

    public void setPassFreeFlag(String passFreeFlag) {
        this.passFreeFlag = passFreeFlag;
    }

    public PassFreeInfoViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.creditCardFlag);
        dest.writeString(this.debitCardFlag);
        dest.writeString(this.creditCardPassFreeAmount);
        dest.writeString(this.debitCardPassFreeAmount);
        dest.writeString(this.passFreeFlag);
        dest.writeString(this.passFreeAmount);
    }

    protected PassFreeInfoViewModel(Parcel in) {
        this.creditCardFlag = in.readString();
        this.debitCardFlag = in.readString();
        this.creditCardPassFreeAmount = in.readString();
        this.debitCardPassFreeAmount = in.readString();
        this.passFreeFlag = in.readString();
        this.passFreeAmount = in.readString();
    }

    public static final Creator<PassFreeInfoViewModel> CREATOR = new Creator<PassFreeInfoViewModel>() {
        @Override
        public PassFreeInfoViewModel createFromParcel(Parcel source) {
            return new PassFreeInfoViewModel(source);
        }

        @Override
        public PassFreeInfoViewModel[] newArray(int size) {
            return new PassFreeInfoViewModel[size];
        }
    };
}
