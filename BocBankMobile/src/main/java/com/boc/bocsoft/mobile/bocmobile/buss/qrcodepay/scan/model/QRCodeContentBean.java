package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangf on 2016/9/9.
 */
public class QRCodeContentBean implements Parcelable {


    /**
     * v : BOC1.00
     * m : 111111111111111
     * t : 22222222
     * n : 测试商户
     */

    //版本标识
    private String v;
    //商户号
    private String m;
    //终端号
    private String t;
    //商户名称
    private String n;

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.v);
        dest.writeString(this.m);
        dest.writeString(this.t);
        dest.writeString(this.n);
    }

    public QRCodeContentBean() {
    }

    protected QRCodeContentBean(Parcel in) {
        this.v = in.readString();
        this.m = in.readString();
        this.t = in.readString();
        this.n = in.readString();
    }

    public static final Parcelable.Creator<QRCodeContentBean> CREATOR = new Parcelable.Creator<QRCodeContentBean>() {
        @Override
        public QRCodeContentBean createFromParcel(Parcel source) {
            return new QRCodeContentBean(source);
        }

        @Override
        public QRCodeContentBean[] newArray(int size) {
            return new QRCodeContentBean[size];
        }
    };
}
