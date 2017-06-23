package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect.IAddress;

/**
 * Created by XieDu on 2016/7/21.
 */
public class OnLineLoanCityBean implements IAddress, Parcelable {
    private String cityCode;
    private String cityName;

    public String getCityCode() { return cityCode;}

    public void setCityCode(String cityCode) { this.cityCode = cityCode;}

    public String getCityName() { return cityName;}

    public void setCityName(String cityName) { this.cityName = cityName;}

    @Override
    public String getCode() {
        return cityCode;
    }

    @Override
    public String getName() {
        return cityName;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityCode);
        dest.writeString(this.cityName);
    }

    public OnLineLoanCityBean() {}

    protected OnLineLoanCityBean(Parcel in) {
        this.cityCode = in.readString();
        this.cityName = in.readString();
    }

    public static final Parcelable.Creator<OnLineLoanCityBean> CREATOR =
            new Parcelable.Creator<OnLineLoanCityBean>() {
                @Override
                public OnLineLoanCityBean createFromParcel(
                        Parcel source) {return new OnLineLoanCityBean(source);}

                @Override
                public OnLineLoanCityBean[] newArray(
                        int size) {return new OnLineLoanCityBean[size];}
            };
}
