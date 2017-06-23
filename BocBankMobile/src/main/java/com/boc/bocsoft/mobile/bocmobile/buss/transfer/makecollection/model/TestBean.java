package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TestBean implements Parcelable {
    public boolean isChecked;
    public String name;
    public String mobilePhone;

    public TestBean() {
    }

    public TestBean(boolean isChecked, String name, String mobilePhone) {
        this.isChecked = isChecked;
        this.name = name;
        this.mobilePhone = mobilePhone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeString(this.mobilePhone);
    }

    private TestBean(Parcel in) {
        this.isChecked = in.readByte() != 0;
        this.name = in.readString();
        this.mobilePhone = in.readString();
    }

    public static final Parcelable.Creator<TestBean> CREATOR = new Parcelable.Creator<TestBean>() {
        public TestBean createFromParcel(Parcel source) {
            return new TestBean(source);
        }

        public TestBean[] newArray(int size) {
            return new TestBean[size];
        }
    };
}
