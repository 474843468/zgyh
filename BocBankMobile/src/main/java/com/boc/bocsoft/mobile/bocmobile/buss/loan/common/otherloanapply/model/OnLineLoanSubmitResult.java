package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XieDu on 2016/7/28.
 */
public class OnLineLoanSubmitResult implements Parcelable {
    /**
     * 申请结果
     * 1:提交成功
     * 2:提交失败
     */
    private String applyResult;
    /**
     * 贷款编号 16位数字
     */
    private String loanNumber;

    public String getApplyResult() { return applyResult;}

    public void setApplyResult(String applyResult) { this.applyResult = applyResult;}

    public String getLoanNumber() { return loanNumber;}

    public void setLoanNumber(String loanNumber) { this.loanNumber = loanNumber;}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.applyResult);
        dest.writeString(this.loanNumber);
    }

    public OnLineLoanSubmitResult() {}

    protected OnLineLoanSubmitResult(Parcel in) {
        this.applyResult = in.readString();
        this.loanNumber = in.readString();
    }

    public static final Parcelable.Creator<OnLineLoanSubmitResult> CREATOR =
            new Parcelable.Creator<OnLineLoanSubmitResult>() {
                @Override
                public OnLineLoanSubmitResult createFromParcel(
                        Parcel source) {return new OnLineLoanSubmitResult(source);}

                @Override
                public OnLineLoanSubmitResult[] newArray(
                        int size) {return new OnLineLoanSubmitResult[size];}
            };
}
