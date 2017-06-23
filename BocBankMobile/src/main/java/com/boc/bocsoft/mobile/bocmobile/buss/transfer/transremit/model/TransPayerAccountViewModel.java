package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wy on 2016/6/15.
 */
public class TransPayerAccountViewModel implements Parcelable {
  

        private String accountStatus;
        private String accountType;
        private String nickName;
        private String cardnumber;
        private String accountId;

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCardnumber() {
            return cardnumber;
        }

        public void setCardnumber(String cardnumber) {
            this.cardnumber = cardnumber;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<TransPayerAccountViewModel> CREATOR = new Parcelable.Creator<TransPayerAccountViewModel>() {
        @Override
        public TransPayerAccountViewModel createFromParcel(Parcel source) {
            return new TransPayerAccountViewModel(source);
        }

        @Override
        public TransPayerAccountViewModel[] newArray(int size) {
            return new TransPayerAccountViewModel[size];
        }
    };

    protected TransPayerAccountViewModel(Parcel in ){
        this.accountStatus=in.readString();
        this.accountType=in.readString();
        this.nickName=in.readString();
        this.cardnumber=in.readString();
        this.accountId=in.readString();
    }
    
}
