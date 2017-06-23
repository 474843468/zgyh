package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 4.55 055 收益试算——请求
 * Created by liuweidong on 2016/10/22.
 */
public class WealthProfitBean implements Parcelable {
    private String proId;// 产品代码
    private String exyield;// 预计年收益率（%）
    private String dayTerm;// 产品期限（天数）
    private String puramt;// 投资金额
    private String totalPeriod = "";// 投资期数
    private String commonExyield = "";// 普通份额预计年收益率（%）
    private String commonDayTerm = "";// 普通份额产品期限（天数）
    private String commonPurAmt = "";// 普通份额投资金额

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getExyield() {
        return exyield;
    }

    public void setExyield(String exyield) {
        this.exyield = exyield;
    }

    public String getDayTerm() {
        return dayTerm;
    }

    public void setDayTerm(String dayTerm) {
        this.dayTerm = dayTerm;
    }

    public String getPuramt() {
        return puramt;
    }

    public void setPuramt(String puramt) {
        this.puramt = puramt;
    }

    public String getTotalPeriod() {
        return totalPeriod;
    }

    public void setTotalPeriod(String totalPeriod) {
        this.totalPeriod = totalPeriod;
    }

    public String getCommonExyield() {
        return commonExyield;
    }

    public void setCommonExyield(String commonExyield) {
        this.commonExyield = commonExyield;
    }

    public String getCommonDayTerm() {
        return commonDayTerm;
    }

    public void setCommonDayTerm(String commonDayTerm) {
        this.commonDayTerm = commonDayTerm;
    }

    public String getCommonPurAmt() {
        return commonPurAmt;
    }

    public void setCommonPurAmt(String commonPurAmt) {
        this.commonPurAmt = commonPurAmt;
    }

    public WealthProfitBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.proId);
        dest.writeString(this.exyield);
        dest.writeString(this.dayTerm);
        dest.writeString(this.puramt);
        dest.writeString(this.totalPeriod);
        dest.writeString(this.commonExyield);
        dest.writeString(this.commonDayTerm);
        dest.writeString(this.commonPurAmt);
    }

    protected WealthProfitBean(Parcel in) {
        this.proId = in.readString();
        this.exyield = in.readString();
        this.dayTerm = in.readString();
        this.puramt = in.readString();
        this.totalPeriod = in.readString();
        this.commonExyield = in.readString();
        this.commonDayTerm = in.readString();
        this.commonPurAmt = in.readString();
    }

    public static final Creator<WealthProfitBean> CREATOR = new Creator<WealthProfitBean>() {
        @Override
        public WealthProfitBean createFromParcel(Parcel source) {
            return new WealthProfitBean(source);
        }

        @Override
        public WealthProfitBean[] newArray(int size) {
            return new WealthProfitBean[size];
        }
    };
}
