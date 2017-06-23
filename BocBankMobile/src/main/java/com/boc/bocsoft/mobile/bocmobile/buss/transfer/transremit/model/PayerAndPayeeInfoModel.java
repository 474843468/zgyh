package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by WYme on 2016/9/29.
 */
public class PayerAndPayeeInfoModel implements Parcelable {
    private String payerAccoutNum;
    private String payeeAccoutNum;
    private String transCurrency;
    private String transCsahRemit;

    public List<String> getPayerCurrencyList() {
        return payerCurrencyList;
    }


    public void setPayerCurrencyList(List<String> payerCurrencyList) {
        this.payerCurrencyList = payerCurrencyList;
    }

    private List<String> payerCurrencyList;

    public List<String> getPayeeCurrencyList() {
        return payeeCurrencyList;
    }

    public void setPayeeCurrencyList(List<String> payeeCurrencyList) {
        this.payeeCurrencyList = payeeCurrencyList;
    }

    private List<String> payeeCurrencyList;

    public boolean isPayeeBoc() {
        return isPayeeBoc;
    }

    public void setPayeeBoc(boolean payeeBoc) {
        isPayeeBoc = payeeBoc;
    }


    public boolean isPayeeLinked() {
        return isPayeeLinked;
    }

    public void setPayeeLinked(boolean payeeLinked) {
        isPayeeLinked = payeeLinked;
    }


    private boolean isPayeeLinked;
    private boolean isPayeeBoc;


    public String getTransCsahRemit() {
        return transCsahRemit;
    }

    public void setTransCsahRemit(String transCsahRemit) {
        this.transCsahRemit = transCsahRemit;
    }

    public String getPayerAccoutNum() {
        return payerAccoutNum;
    }

    public void setPayerAccoutNum(String payerAccoutNum) {
        this.payerAccoutNum = payerAccoutNum;
    }

    public String getPayeeAccoutNum() {
        return payeeAccoutNum;
    }

    public void setPayeeAccoutNum(String payeeAccoutNum) {
        this.payeeAccoutNum = payeeAccoutNum;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }


    public PayerAndPayeeInfoModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payerAccoutNum);
        dest.writeString(this.payeeAccoutNum);
        dest.writeString(this.transCurrency);
        dest.writeString(this.transCsahRemit);
        dest.writeStringList(this.payerCurrencyList);
        dest.writeStringList(this.payeeCurrencyList);
        dest.writeByte(this.isPayeeLinked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPayeeBoc ? (byte) 1 : (byte) 0);
    }

    protected PayerAndPayeeInfoModel(Parcel in) {
        this.payerAccoutNum = in.readString();
        this.payeeAccoutNum = in.readString();
        this.transCurrency = in.readString();
        this.transCsahRemit = in.readString();
        this.payerCurrencyList = in.createStringArrayList();
        this.payeeCurrencyList = in.createStringArrayList();
        this.isPayeeLinked = in.readByte() != 0;
        this.isPayeeBoc = in.readByte() != 0;
    }

    public static final Creator<PayerAndPayeeInfoModel> CREATOR = new Creator<PayerAndPayeeInfoModel>() {
        @Override
        public PayerAndPayeeInfoModel createFromParcel(Parcel source) {
            return new PayerAndPayeeInfoModel(source);
        }

        @Override
        public PayerAndPayeeInfoModel[] newArray(int size) {
            return new PayerAndPayeeInfoModel[size];
        }
    };
}
