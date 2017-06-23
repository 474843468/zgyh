package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WYme on 2016/9/21.
 */
public class CardQueryBindInfoResult implements Parcelable {
    private String accountNumber;
    private String payeeaccountNumber;
    private String payeeaccountType;
    private String openingBankName;
    private String cnapsCode;
    private String accountStatus;
    private String payeeAccountName;
    private String payeeCnaps;
    private String payeeBankName;
    private String bankCode;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPayeeaccountNumber() {
        return payeeaccountNumber;
    }

    public void setPayeeaccountNumber(String payeeaccountNumber) {
        this.payeeaccountNumber = payeeaccountNumber;}
    public String getPayeeaccountType() {
        return payeeaccountType;}
    public void setPayeeaccountType(String payeeaccountType) {
        this.payeeaccountType = payeeaccountType;}
    public String getOpeningBankName() {
        return openingBankName;
    }
    public void setOpeningBankName(String openingBankName) {
        this.openingBankName = openingBankName;}

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
    }

    public String getPayeeCnaps() {
        return payeeCnaps;
    }

    public void setPayeeCnaps(String payeeCnaps) {
        this.payeeCnaps = payeeCnaps;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountNumber);
        dest.writeString(this.payeeaccountNumber);
        dest.writeString(this.payeeaccountType);
        dest.writeString(this.openingBankName);
        dest.writeString(this.cnapsCode);
        dest.writeString(this.accountStatus);
        dest.writeString(this.payeeAccountName);
        dest.writeString(this.payeeCnaps);
        dest.writeString(this.payeeBankName);
        dest.writeString(this.bankCode);
    }

    public CardQueryBindInfoResult() {
    }

    protected CardQueryBindInfoResult(Parcel in) {
        this.accountNumber = in.readString();
        this.payeeaccountNumber = in.readString();
        this.payeeaccountType = in.readString();
        this.openingBankName = in.readString();
        this.cnapsCode = in.readString();
        this.accountStatus = in.readString();
        this.payeeAccountName = in.readString();
        this.payeeCnaps = in.readString();
        this.payeeBankName = in.readString();
        this.bankCode = in.readString();
    }

    public static final Parcelable.Creator<CardQueryBindInfoResult> CREATOR = new Parcelable.Creator<CardQueryBindInfoResult>() {
        @Override
        public CardQueryBindInfoResult createFromParcel(Parcel source) {
            return new CardQueryBindInfoResult(source);
        }

        @Override
        public CardQueryBindInfoResult[] newArray(int size) {
            return new CardQueryBindInfoResult[size];
        }
    };
}
