package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect.IAddress;

/**
 * Created by XieDu on 2016/7/19.
 */
public class OnLineLoanProvinceBean implements IAddress, Parcelable {
    private String provinceCode;
    private String provinceName;

    public String getProvinceCode() { return provinceCode;}

    public void setProvinceCode(String provinceCode) { this.provinceCode = provinceCode;}

    public String getProvinceName() { return provinceName;}

    public void setProvinceName(String provinceName) { this.provinceName = provinceName;}

    @Override
    public String getCode() {
        return provinceCode;
    }

    @Override
    public String getName() {
        return provinceName;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.provinceCode);
        dest.writeString(this.provinceName);
    }

    public OnLineLoanProvinceBean() {}

    protected OnLineLoanProvinceBean(Parcel in) {
        this.provinceCode = in.readString();
        this.provinceName = in.readString();
    }

    public static final Parcelable.Creator<OnLineLoanProvinceBean> CREATOR =
            new Parcelable.Creator<OnLineLoanProvinceBean>() {
                @Override
                public OnLineLoanProvinceBean createFromParcel(
                        Parcel source) {return new OnLineLoanProvinceBean(source);}

                @Override
                public OnLineLoanProvinceBean[] newArray(
                        int size) {return new OnLineLoanProvinceBean[size];}
            };
}
