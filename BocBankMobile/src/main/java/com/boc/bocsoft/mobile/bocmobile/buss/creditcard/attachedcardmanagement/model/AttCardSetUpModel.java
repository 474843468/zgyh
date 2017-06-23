package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Name: liukai
 * Time：2016/12/16 10:02.
 * Created by lk7066 on 2016/12/16.
 * It's used to 短信和流量设置跳转需要的数据集合
 */

public class AttCardSetUpModel implements Parcelable {

    /**
     * 主卡Id
     * */
    private String accountId;

    /**
     * 信用卡卡号
     * */
    private String crcdNo;

    /**
     * 附属卡名字
     * */
    private String attCardName;

    /**
     * 附属卡卡号
     * */
    private String subCrcdNo;

    /**
     * 第一币种
     * */
    private String currency1;

    /**
     * 第二币种
     * */
    private String currency2;

    /**
     * 币种标识码，0单外币，1单本币，2多币种
     * */
    private int mCurrency = -1;

    /**
     * 主卡类型
     * */
    private String masterCrcdType;

    @Override
    public String toString() {
        return "AttCardSetUpModel{" +
                "accountId='" + accountId + '\'' +
                ", crcdNo='" + crcdNo + '\'' +
                ", attCardName='" + attCardName + '\'' +
                ", subCrcdNo='" + subCrcdNo + '\'' +
                ", currency1='" + currency1 + '\'' +
                ", currency2='" + currency2 + '\'' +
                ", mCurrency=" + mCurrency +
                ", masterCrcdType='" + masterCrcdType + '\'' +
                '}';
    }

    public String getMasterCrcdType() {
        return masterCrcdType;
    }

    public void setMasterCrcdType(String masterCrcdType) {
        this.masterCrcdType = masterCrcdType;
    }

    public int getmCurrency() {
        return mCurrency;
    }

    public void setmCurrency(int mCurrency) {
        this.mCurrency = mCurrency;
    }

    public String getCrcdNo() {
        return crcdNo;
    }

    public void setCrcdNo(String crcdNo) {
        this.crcdNo = crcdNo;
    }

    public String getAttCardName() {
        return attCardName;
    }

    public void setAttCardName(String attCardName) {
        this.attCardName = attCardName;
    }

    public static Creator<AttCardSetUpModel> getCREATOR() {
        return CREATOR;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSubCrcdNo() {
        return subCrcdNo;
    }

    public void setSubCrcdNo(String subCrcdNo) {
        this.subCrcdNo = subCrcdNo;
    }

    public String getCurrency2() {
        return currency2;
    }

    public void setCurrency2(String currency2) {
        this.currency2 = currency2;
    }

    public String getCurrency1() {
        return currency1;
    }

    public void setCurrency1(String currency1) {
        this.currency1 = currency1;
    }

    public AttCardSetUpModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.crcdNo);
        dest.writeString(this.attCardName);
        dest.writeString(this.subCrcdNo);
        dest.writeString(this.currency1);
        dest.writeString(this.currency2);
        dest.writeInt(this.mCurrency);
        dest.writeString(this.masterCrcdType);
    }

    protected AttCardSetUpModel(Parcel in) {
        this.accountId = in.readString();
        this.crcdNo = in.readString();
        this.attCardName = in.readString();
        this.subCrcdNo = in.readString();
        this.currency1 = in.readString();
        this.currency2 = in.readString();
        this.mCurrency = in.readInt();
        this.masterCrcdType = in.readString();
    }

    public static final Creator<AttCardSetUpModel> CREATOR = new Creator<AttCardSetUpModel>() {
        @Override
        public AttCardSetUpModel createFromParcel(Parcel source) {
            return new AttCardSetUpModel(source);
        }

        @Override
        public AttCardSetUpModel[] newArray(int size) {
            return new AttCardSetUpModel[size];
        }
    };

}
