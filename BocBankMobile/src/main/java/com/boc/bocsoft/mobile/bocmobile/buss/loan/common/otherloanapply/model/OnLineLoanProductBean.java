package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XieDu on 2016/7/21.
 */
public class OnLineLoanProductBean implements Parcelable {
    private String productCode;
    private String productName;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productCode);
        dest.writeString(this.productName);
    }

    public OnLineLoanProductBean() {}

    protected OnLineLoanProductBean(Parcel in) {
        this.productCode = in.readString();
        this.productName = in.readString();
    }

    public static final Parcelable.Creator<OnLineLoanProductBean> CREATOR =
            new Parcelable.Creator<OnLineLoanProductBean>() {
                @Override
                public OnLineLoanProductBean createFromParcel(
                        Parcel source) {return new OnLineLoanProductBean(source);}

                @Override
                public OnLineLoanProductBean[] newArray(
                        int size) {return new OnLineLoanProductBean[size];}
            };
}
