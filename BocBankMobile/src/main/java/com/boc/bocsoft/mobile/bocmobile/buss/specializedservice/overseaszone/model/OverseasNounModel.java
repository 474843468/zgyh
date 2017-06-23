package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.model;

import android.os.Parcel;
import android.os.Parcelable;


public class OverseasNounModel implements Parcelable{
    private String title;
    private String content;

    public OverseasNounModel() {
    }

    protected OverseasNounModel(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<OverseasNounModel> CREATOR = new Creator<OverseasNounModel>() {
        @Override
        public OverseasNounModel createFromParcel(Parcel in) {
            return new OverseasNounModel(in);
        }

        @Override
        public OverseasNounModel[] newArray(int size) {
            return new OverseasNounModel[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }
}
