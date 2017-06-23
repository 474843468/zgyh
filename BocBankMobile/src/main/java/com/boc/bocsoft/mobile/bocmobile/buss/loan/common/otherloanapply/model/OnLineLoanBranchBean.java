package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XieDu on 2016/7/26.
 */
public class OnLineLoanBranchBean implements Parcelable {
    /**
     * 网点编号
     */
    private String deptID;
    /**
     * 网点名称
     */
    private String deptName;
    /**
     * 网点地址
     */
    private String deptAddr;
    /**
     * 网点电话
     */
    private String deptPhone;

    public String getDeptID() { return deptID;}

    public void setDeptID(String deptID) { this.deptID = deptID;}

    public String getDeptName() { return deptName;}

    public void setDeptName(String deptName) { this.deptName = deptName;}

    public String getDeptAddr() { return deptAddr;}

    public void setDeptAddr(String deptAddr) { this.deptAddr = deptAddr;}

    public String getDeptPhone() { return deptPhone;}

    public void setDeptPhone(String deptPhone) { this.deptPhone = deptPhone;}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deptID);
        dest.writeString(this.deptName);
        dest.writeString(this.deptAddr);
        dest.writeString(this.deptPhone);
    }

    public OnLineLoanBranchBean() {}

    protected OnLineLoanBranchBean(Parcel in) {
        this.deptID = in.readString();
        this.deptName = in.readString();
        this.deptAddr = in.readString();
        this.deptPhone = in.readString();
    }

    public static final Parcelable.Creator<OnLineLoanBranchBean> CREATOR =
            new Parcelable.Creator<OnLineLoanBranchBean>() {
                @Override
                public OnLineLoanBranchBean createFromParcel(
                        Parcel source) {return new OnLineLoanBranchBean(source);}

                @Override
                public OnLineLoanBranchBean[] newArray(
                        int size) {return new OnLineLoanBranchBean[size];}
            };
}
