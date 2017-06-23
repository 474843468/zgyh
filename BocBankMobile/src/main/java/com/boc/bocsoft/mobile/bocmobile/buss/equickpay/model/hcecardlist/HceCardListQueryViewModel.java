package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengjunying on 2016/12/6.
 */
public class HceCardListQueryViewModel implements Parcelable {

    private String masterCardNo;
    private String cardType;
    private String slaveCardNo;
    private BigDecimal singleQuota;
    private BigDecimal dayQuota;
    private String cardBrand;
    private String custMobile;
    private String cardStatus;

    public String getMasterCardNo() {
        return masterCardNo;
    }

    public void setMasterCardNo(String masterCardNo) {
        this.masterCardNo = masterCardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getSlaveCardNo() {
        return slaveCardNo;
    }

    public void setSlaveCardNo(String slaveCardNo) {
        this.slaveCardNo = slaveCardNo;
    }

    public BigDecimal getSingleQuota() {
        return singleQuota;
    }

    public void setSingleQuota(BigDecimal singleQuota) {
        this.singleQuota = singleQuota;
    }

    public BigDecimal getDayQuota() {
        return dayQuota;
    }

    public void setDayQuota(BigDecimal dayQuota) {
        this.dayQuota = dayQuota;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.masterCardNo);
        dest.writeString(this.cardType);
        dest.writeString(this.slaveCardNo);
        dest.writeSerializable(this.singleQuota);
        dest.writeSerializable(this.dayQuota);
        dest.writeString(this.cardBrand);
        dest.writeString(this.custMobile);
        dest.writeString(this.cardStatus);
    }

    public HceCardListQueryViewModel() {
    }

    private HceCardListQueryViewModel(Parcel in) {
        this.masterCardNo = in.readString();
        this.cardType = in.readString();
        this.slaveCardNo = in.readString();
        this.singleQuota = (BigDecimal) in.readSerializable();
        this.dayQuota = (BigDecimal) in.readSerializable();
        this.cardBrand = in.readString();
        this.custMobile = in.readString();
        this.cardStatus = in.readString();
    }

    public static final Creator<HceCardListQueryViewModel> CREATOR = new Creator<HceCardListQueryViewModel>() {
        public HceCardListQueryViewModel createFromParcel(Parcel source) {
            return new HceCardListQueryViewModel(source);
        }

        public HceCardListQueryViewModel[] newArray(int size) {
            return new HceCardListQueryViewModel[size];
        }
    };
}