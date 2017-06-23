package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 省市区联动查询,返回参数实体类
 * Created by xitong on 2016/6/13.
 */
public class DistrictRes implements Parcelable {


    //字母码
    private String orgCode;
    //名称
    private String orgName;
    //省代码
    private String privCode;
    //省名称
    private String privName;
    //市级行政区划代码
    private String cityCode;
    //市名称
    private String cityName;
    //区/县级行政区划代码
    private String areaCode;
    //县名称
    private String areaName;
    //6位行政区划代码
    private String zoneCode;



    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPrivCode() {
        return privCode;
    }

    public void setPrivCode(String privCode) {
        this.privCode = privCode;
    }

    public String getPrivName() {
        return privName;
    }

    public void setPrivName(String privName) {
        this.privName = privName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }


    @Override
    public String toString() {
        return "DistrictRes{" +
                "orgCode='" + orgCode + '\'' +
                ", orgName='" + orgName + '\'' +
                ", privCode='" + privCode + '\'' +
                ", privName='" + privName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", areaName='" + areaName + '\'' +
                ", zoneCode='" + zoneCode + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orgCode);
        dest.writeString(this.orgName);
        dest.writeString(this.privCode);
        dest.writeString(this.privName);
        dest.writeString(this.cityCode);
        dest.writeString(this.cityName);
        dest.writeString(this.areaCode);
        dest.writeString(this.areaName);
        dest.writeString(this.zoneCode);
    }

    public DistrictRes() {
    }

    protected DistrictRes(Parcel in) {
        this.orgCode = in.readString();
        this.orgName = in.readString();
        this.privCode = in.readString();
        this.privName = in.readString();
        this.cityCode = in.readString();
        this.cityName = in.readString();
        this.areaCode = in.readString();
        this.areaName = in.readString();
        this.zoneCode = in.readString();
    }

    public static final Creator<DistrictRes> CREATOR = new Creator<DistrictRes>() {
        @Override
        public DistrictRes createFromParcel(Parcel source) {
            return new DistrictRes(source);
        }

        @Override
        public DistrictRes[] newArray(int size) {
            return new DistrictRes[size];
        }
    };
}
