package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangtong on 2016/7/25.
 */
public class ReminderOperatorModel implements Parcelable {

    private String totalAmount;
    private String capitaAmount;
    private String payeeAccount;
    private String tips;
    private String payer;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCapitaAmount() {
        return capitaAmount;
    }

    public void setCapitaAmount(String capitaAmount) {
        this.capitaAmount = capitaAmount;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalAmount);
        dest.writeString(this.capitaAmount);
        dest.writeString(this.payeeAccount);
        dest.writeString(this.tips);
        dest.writeString(this.payer);
    }

    public ReminderOperatorModel() {
    }

    protected ReminderOperatorModel(Parcel in) {
        this.totalAmount = in.readString();
        this.capitaAmount = in.readString();
        this.payeeAccount = in.readString();
        this.tips = in.readString();
        this.payer = in.readString();
    }

    public static final Parcelable.Creator<ReminderOperatorModel> CREATOR = new Parcelable.Creator<ReminderOperatorModel>() {
        @Override
        public ReminderOperatorModel createFromParcel(Parcel source) {
            return new ReminderOperatorModel(source);
        }

        @Override
        public ReminderOperatorModel[] newArray(int size) {
            return new ReminderOperatorModel[size];
        }
    };
}
