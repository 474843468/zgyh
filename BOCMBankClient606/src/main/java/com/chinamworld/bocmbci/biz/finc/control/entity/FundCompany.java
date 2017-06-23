package com.chinamworld.bocmbci.biz.finc.control.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 基金公司
 * @author zxf
 *
 */
public class FundCompany implements Parcelable{
	private String fundCompanyName;
    private String fundCompanyCode;
    private String alpha;
    private boolean checked;

    public String getFundCompanyName() {
        return fundCompanyName;
    }

    public void setFundCompanyName(String fundCompanyName) {
        this.fundCompanyName = fundCompanyName;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "{" +
                "\"fundCompanyName\" : " + "\"" + fundCompanyName + "\"" +
                ", \"fundCompanyCode\" : " + "\"" + fundCompanyCode + "\"" +
                ", \"alpha\" : " + "\"" + alpha + "\"" +
                ", \"checked\" : " + "\"" + checked + "\"" +
                "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fundCompanyName);
        dest.writeString(this.fundCompanyCode);
        dest.writeString(this.alpha);
        dest.writeByte(checked ? (byte) 1 : (byte) 0);
    }

    public FundCompany() {
    }

    protected FundCompany(Parcel in) {
        this.fundCompanyName = in.readString();
        this.fundCompanyCode = in.readString();
        this.alpha = in.readString();
        this.checked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FundCompany> CREATOR = new Parcelable.Creator<FundCompany>() {
        public FundCompany createFromParcel(Parcel source) {
            return new FundCompany(source);
        }

        public FundCompany[] newArray(int size) {
            return new FundCompany[size];
        }
    };

}
