package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanbin on 16/10/16.
 */
public class QRPayGetPayeeInfoModel implements Parcelable {

    //金额
    private String tranAmount;
    //收款人账号
    private String payeeActNum;
    //收款人姓名  带*号
    private String payeeActNam;
    //收款方附言  有则出现
    private String payeeComments;
    //收款行行号
    private String payeeIbkNum;
    //收款行名称
    private String payeeIbkNam;

    @Override
    public String toString() {
        return "QRPayGetPayeeInfoModel{" +
                "tranAmount='" + tranAmount + '\'' +
                ", payeeActNum='" + payeeActNum + '\'' +
                ", payeeActNam='" + payeeActNam + '\'' +
                ", payeeComments='" + payeeComments + '\'' +
                ", payeeIbkNum='" + payeeIbkNum + '\'' +
                ", payeeIbkNam='" + payeeIbkNam + '\'' +
                '}';
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getPayeeActNum() {
        return payeeActNum;
    }

    public void setPayeeActNum(String payeeActNum) {
        this.payeeActNum = payeeActNum;
    }

    public String getPayeeActNam() {
        return payeeActNam;
    }

    public void setPayeeActNam(String payeeActNam) {
        this.payeeActNam = payeeActNam;
    }

    public String getPayeeComments() {
        return payeeComments;
    }

    public void setPayeeComments(String payeeComments) {
        this.payeeComments = payeeComments;
    }

    public String getPayeeIbkNum() {
        return payeeIbkNum;
    }

    public void setPayeeIbkNum(String payeeIbkNum) {
        this.payeeIbkNum = payeeIbkNum;
    }

    public String getPayeeIbkNam() {
        return payeeIbkNam;
    }

    public void setPayeeIbkNam(String payeeIbkNam) {
        this.payeeIbkNam = payeeIbkNam;
    }

    public QRPayGetPayeeInfoModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tranAmount);
        dest.writeString(this.payeeActNum);
        dest.writeString(this.payeeActNam);
        dest.writeString(this.payeeComments);
        dest.writeString(this.payeeIbkNum);
        dest.writeString(this.payeeIbkNam);
    }

    protected QRPayGetPayeeInfoModel(Parcel in) {
        this.tranAmount = in.readString();
        this.payeeActNum = in.readString();
        this.payeeActNam = in.readString();
        this.payeeComments = in.readString();
        this.payeeIbkNum = in.readString();
        this.payeeIbkNam = in.readString();
    }

    public static final Creator<QRPayGetPayeeInfoModel> CREATOR = new Creator<QRPayGetPayeeInfoModel>() {
        @Override
        public QRPayGetPayeeInfoModel createFromParcel(Parcel source) {
            return new QRPayGetPayeeInfoModel(source);
        }

        @Override
        public QRPayGetPayeeInfoModel[] newArray(int size) {
            return new QRPayGetPayeeInfoModel[size];
        }
    };
}
