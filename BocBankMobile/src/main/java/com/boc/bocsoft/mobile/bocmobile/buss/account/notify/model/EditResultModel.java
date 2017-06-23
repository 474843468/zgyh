package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;

/**
 * Created by wangtong on 2016/6/17.
 */
public class EditResultModel implements Parcelable {
    //操作结果编码
    private int resultCode;
    //开通账户
    private String signAccount;
    //用户姓名
    private String userName;
    //付费账户
    private String feeAccount;
    //接收短信通知的手机
    private String phoneNum;

    private String headName;

    private String rangeAmount;

    private String feeStandard;

    public EditResultModel() {
    }

    public EditResultModel(ConfirmEditModel model) {
        resultCode = 0;
        signAccount = model.getEditModel().getSignAccount().getAccountNumber();
        userName = model.getEditModel().getUserName();
        feeAccount = model.getEditModel().getFeeAccount().getAccountNumber();
        phoneNum = model.getEditModel().getPhoneNum();
        feeStandard = model.getEditModel().getFeeRate();
        rangeAmount = MoneyUtils.transMoneyFormat(model.getEditModel().getMinMoney(), "001") + "元~"
                + MoneyUtils.transMoneyFormat(model.getEditModel().getMaxMoney(), "001") + "元";
    }

    public String getRangeAmount() {
        return rangeAmount;
    }

    public void setRangeAmount(String rangeAmount) {
        this.rangeAmount = rangeAmount;
    }

    public String getFeeStandard() {
        return feeStandard;
    }

    public void setFeeStandard(String feeStandard) {
        this.feeStandard = feeStandard;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getSignAccount() {
        return signAccount;
    }

    public void setSignAccount(String signAccount) {
        this.signAccount = signAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFeeAccount() {
        return feeAccount;
    }

    public void setFeeAccount(String feeAccount) {
        this.feeAccount = feeAccount;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resultCode);
        dest.writeString(this.signAccount);
        dest.writeString(this.userName);
        dest.writeString(this.feeAccount);
        dest.writeString(this.phoneNum);
        dest.writeString(this.headName);
        dest.writeString(this.rangeAmount);
        dest.writeString(this.feeStandard);
    }

    protected EditResultModel(Parcel in) {
        this.resultCode = in.readInt();
        this.signAccount = in.readString();
        this.userName = in.readString();
        this.feeAccount = in.readString();
        this.phoneNum = in.readString();
        this.headName = in.readString();
        this.rangeAmount = in.readString();
        this.feeStandard = in.readString();
    }

    public static final Parcelable.Creator<EditResultModel> CREATOR = new Parcelable.Creator<EditResultModel>() {
        @Override
        public EditResultModel createFromParcel(Parcel source) {
            return new EditResultModel(source);
        }

        @Override
        public EditResultModel[] newArray(int size) {
            return new EditResultModel[size];
        }
    };
}
