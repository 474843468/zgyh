package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Result:银行实体
 * Created by zhx on 2016/7/20
 */
public class BankEntity implements Comparable<BankEntity>, Parcelable {
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行行号
     */
    private String bankCode;
    /**
     * 银行别名
     */
    private String bankAlias;
    /**
     * 银行类型
     */
    private String bankType;
    /**
     * 银行行别
     */
    private String bankBtp;
    /**
     * 银行行别名称
     */
    private String bankBpm;
    /**
     * 银行所属CCPC
     */
    private String bankCcpc;
    /**
     * 银行清算行号
     */
    private String bankClr;
    /**
     * 银行状态
     */
    private String bankStatus;
    /**
     * 银行名称拼音
     */
    private String bankNamePinyin;

    private String bankNamePinyin1;

    private boolean isHot;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAlias() {
        return bankAlias;
    }

    public void setBankAlias(String bankAlias) {
        this.bankAlias = bankAlias;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankBtp() {
        return bankBtp;
    }

    public void setBankBtp(String bankBtp) {
        this.bankBtp = bankBtp;
    }

    public String getBankBpm() {
        return bankBpm;
    }

    public void setBankBpm(String bankBpm) {
        this.bankBpm = bankBpm;
    }

    public String getBankCcpc() {
        return bankCcpc;
    }

    public void setBankCcpc(String bankCcpc) {
        this.bankCcpc = bankCcpc;
    }

    public String getBankClr() {
        return bankClr;
    }

    public void setBankClr(String bankClr) {
        this.bankClr = bankClr;
    }

    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    public String getBankNamePinyin() {
        return bankNamePinyin;
    }

    public void setBankNamePinyin(String bankNamePinyin) {
        this.bankNamePinyin = bankNamePinyin;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public String getBankNamePinyin1() {
        return bankNamePinyin1;
    }

    public void setBankNamePinyin1(String bankNamePinyin1) {
        this.bankNamePinyin1 = bankNamePinyin1;
    }

    @Override
    public int compareTo(BankEntity another) {
        return this.getBankNamePinyin().compareTo(another.getBankNamePinyin());
    }


    public BankEntity() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bankName);
        dest.writeString(this.bankCode);
        dest.writeString(this.bankAlias);
        dest.writeString(this.bankType);
        dest.writeString(this.bankBtp);
        dest.writeString(this.bankBpm);
        dest.writeString(this.bankCcpc);
        dest.writeString(this.bankClr);
        dest.writeString(this.bankStatus);
        dest.writeString(this.bankNamePinyin);
        dest.writeString(this.bankNamePinyin1);
        dest.writeByte(isHot ? (byte) 1 : (byte) 0);
    }

    private BankEntity(Parcel in) {
        this.bankName = in.readString();
        this.bankCode = in.readString();
        this.bankAlias = in.readString();
        this.bankType = in.readString();
        this.bankBtp = in.readString();
        this.bankBpm = in.readString();
        this.bankCcpc = in.readString();
        this.bankClr = in.readString();
        this.bankStatus = in.readString();
        this.bankNamePinyin = in.readString();
        this.bankNamePinyin1 = in.readString();
        this.isHot = in.readByte() != 0;
    }

    public static final Creator<BankEntity> CREATOR = new Creator<BankEntity>() {
        public BankEntity createFromParcel(Parcel source) {
            return new BankEntity(source);
        }

        public BankEntity[] newArray(int size) {
            return new BankEntity[size];
        }
    };
}
