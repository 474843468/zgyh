package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/10.
 */
public class InvestTreatyBean implements Parcelable {
    protected InvestTreatyBean(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InvestTreatyBean> CREATOR = new Creator<InvestTreatyBean>() {
        @Override
        public InvestTreatyBean createFromParcel(Parcel in) {
            return new InvestTreatyBean(in);
        }

        @Override
        public InvestTreatyBean[] newArray(int size) {
            return new InvestTreatyBean[size];
        }
    };
}
