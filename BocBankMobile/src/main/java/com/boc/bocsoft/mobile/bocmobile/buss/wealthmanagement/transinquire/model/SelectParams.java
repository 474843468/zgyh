package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 中银理财-交易查询和撤单-筛选的参数
 * 说明：取参数的时候，如果为空，那么不传；如果不为空，那么传递
 * Created by zhx on 2016/9/20
 */
public class SelectParams implements Parcelable {
    // 开始日期
    private String startDate = "";
    // 结束日期
    private String endDate = "";
    // 交易类型(0代表常规，1代表组合)
    private String transType = "";
    // 币种
    private String currency = "";

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SelectParams that = (SelectParams) o;

        if (!startDate.equals(that.startDate))
            return false;
        if (!endDate.equals(that.endDate))
            return false;
        if (!transType.equals(that.transType))
            return false;
        return currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + transType.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.transType);
        dest.writeString(this.currency);
    }

    public SelectParams() {
    }

    private SelectParams(Parcel in) {
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.transType = in.readString();
        this.currency = in.readString();
    }

    public static final Parcelable.Creator<SelectParams> CREATOR = new Parcelable.Creator<SelectParams>() {
        public SelectParams createFromParcel(Parcel source) {
            return new SelectParams(source);
        }

        public SelectParams[] newArray(int size) {
            return new SelectParams[size];
        }
    };
}
