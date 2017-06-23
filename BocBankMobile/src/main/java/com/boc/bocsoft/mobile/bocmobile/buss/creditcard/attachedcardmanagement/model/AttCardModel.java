package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Name: liukai
 * Time：2016/12/5 9:56.
 * Created by lk7066 on 2016/12/5.
 * It's used to 附属卡首页的数据，该页面是将接口返回的两级列表转换为一级列表保存
 */

public class AttCardModel implements Parcelable {

    /**
     * 标识是否为主卡的第一张附属卡，如果是1，则显示title，如果是0，则不显示
     * */
    private int flag;//标识是否为第一张附属卡

    /**
     * 主卡的类型
     * */
    private String masterCrcdType;

    /**
     * 主卡的卡号
     * */
    private String masterCrcdNum;

    /**
     * 主卡的产品名
     * */
    private String masterCrcdProductName;

    /**
     * 主卡的Id
     * */
    private String masterCrcdId;

    /**
     * 附属卡卡号
     * */
    private String subCrcdNum;

    /**
     * 附属卡户主
     * */
    private String subCrcdHolder;

    /**
     * 附属卡类型
     * */
    private String subCrcdType;

    /**
     * 附属卡账户ID
     * */
    private String subAccountId;

    @Override
    public String toString() {
        return "AttCardModel{" +
                "flag=" + flag +
                ", masterCrcdType='" + masterCrcdType + '\'' +
                ", masterCrcdNum='" + masterCrcdNum + '\'' +
                ", masterCrcdProductName='" + masterCrcdProductName + '\'' +
                ", masterCrcdId='" + masterCrcdId + '\'' +
                ", subCrcdNum='" + subCrcdNum + '\'' +
                ", subCrcdHolder='" + subCrcdHolder + '\'' +
                ", subCrcdType='" + subCrcdType + '\'' +
                ", subAccountId='" + subAccountId + '\'' +
                '}';
    }

    public String getSubCrcdType() {
        return subCrcdType;
    }

    public void setSubCrcdType(String subCrcdType) {
        this.subCrcdType = subCrcdType;
    }

    public String getSubAccountId() {
        return subAccountId;
    }

    public void setSubAccountId(String subAccountId) {
        this.subAccountId = subAccountId;
    }

    public static Creator<AttCardModel> getCREATOR() {
        return CREATOR;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMasterCrcdType() {
        return masterCrcdType;
    }

    public void setMasterCrcdType(String masterCrcdType) {
        this.masterCrcdType = masterCrcdType;
    }

    public String getMasterCrcdNum() {
        return masterCrcdNum;
    }

    public void setMasterCrcdNum(String masterCrcdNum) {
        this.masterCrcdNum = masterCrcdNum;
    }

    public String getMasterCrcdProductName() {
        return masterCrcdProductName;
    }

    public void setMasterCrcdProductName(String masterCrcdProductName) {
        this.masterCrcdProductName = masterCrcdProductName;
    }

    public String getMasterCrcdId() {
        return masterCrcdId;
    }

    public void setMasterCrcdId(String masterCrcdId) {
        this.masterCrcdId = masterCrcdId;
    }

    public String getSubCrcdNum() {
        return subCrcdNum;
    }

    public void setSubCrcdNum(String subCrcdNum) {
        this.subCrcdNum = subCrcdNum;
    }

    public String getSubCrcdHolder() {
        return subCrcdHolder;
    }

    public void setSubCrcdHolder(String subCrcdHolder) {
        this.subCrcdHolder = subCrcdHolder;
    }

    public AttCardModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.flag);
        dest.writeString(this.masterCrcdType);
        dest.writeString(this.masterCrcdNum);
        dest.writeString(this.masterCrcdProductName);
        dest.writeString(this.masterCrcdId);
        dest.writeString(this.subCrcdNum);
        dest.writeString(this.subCrcdHolder);
        dest.writeString(this.subCrcdType);
        dest.writeString(this.subAccountId);
    }

    protected AttCardModel(Parcel in) {
        this.flag = in.readInt();
        this.masterCrcdType = in.readString();
        this.masterCrcdNum = in.readString();
        this.masterCrcdProductName = in.readString();
        this.masterCrcdId = in.readString();
        this.subCrcdNum = in.readString();
        this.subCrcdHolder = in.readString();
        this.subCrcdType = in.readString();
        this.subAccountId = in.readString();
    }

    public static final Creator<AttCardModel> CREATOR = new Creator<AttCardModel>() {
        @Override
        public AttCardModel createFromParcel(Parcel source) {
            return new AttCardModel(source);
        }

        @Override
        public AttCardModel[] newArray(int size) {
            return new AttCardModel[size];
        }
    };
}
