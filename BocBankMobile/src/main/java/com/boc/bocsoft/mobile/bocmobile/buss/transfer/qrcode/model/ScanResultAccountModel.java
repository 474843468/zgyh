package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xdy4486 on 2016/6/24.
 */
public class ScanResultAccountModel implements Parcelable {
    /**
     * custName
     * 客户名称
     */
    private String custName;
    /**
     * 客户账号
     */
    private String custActNum;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustActNum() {
        return custActNum;
    }

    public void setCustActNum(String custActNum) {
        this.custActNum = custActNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.custName);
        dest.writeString(this.custActNum);
    }

    public ScanResultAccountModel() {
    }

    protected ScanResultAccountModel(Parcel in) {
        this.custName = in.readString();
        this.custActNum = in.readString();
    }

    public static final Parcelable.Creator<ScanResultAccountModel> CREATOR = new Parcelable.Creator<ScanResultAccountModel>() {
        @Override
        public ScanResultAccountModel createFromParcel(Parcel source) {
            return new ScanResultAccountModel(source);
        }

        @Override
        public ScanResultAccountModel[] newArray(int size) {
            return new ScanResultAccountModel[size];
        }
    };

    @Override
    public String toString() {
        return "ScanResultAccountModel{" +
                "custName='" + custName + '\'' +
                ", custActNum='" + custActNum + '\'' +
                '}';
    }
}
