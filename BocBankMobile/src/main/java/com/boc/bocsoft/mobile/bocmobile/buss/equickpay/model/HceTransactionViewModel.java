package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangle on 2016/12/15.
 * 描述:
 */
public class HceTransactionViewModel implements Parcelable {

    private String deviceNo;//设备号
    private String masterCardNo;// 主卡卡号
    private String slaveCardNo;// 从卡卡号
    private String singleQuota;//	单笔限额
    private String perDayQuota;// 日限额
    private String _combinId;// 安全工具组合id
    private String conversationId;// 会话id
    private CardBrandModel cardBrandModel;// 卡品牌
    private From from;// 来自那个界面,
    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getMasterCardNo() {
        return masterCardNo;
    }

    public void setMasterCardNo(String masterCardNo) {
        this.masterCardNo = masterCardNo;
    }

    public String getSlaveCardNo() {
        return slaveCardNo;
    }

    public void setSlaveCardNo(String slaveCardNo) {
        this.slaveCardNo = slaveCardNo;
    }

    public String getSingleQuota() {
        return singleQuota;
    }

    public void setSingleQuota(String singleQuota) {
        this.singleQuota = singleQuota;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getPerDayQuota() {
        return perDayQuota;
    }

    public void setPerDayQuota(String perDayQuota) {
        this.perDayQuota = perDayQuota;
    }

    public CardBrandModel getCardBrandModel() {
        return cardBrandModel;
    }

    public void setCardBrandModel(CardBrandModel cardBrandModel) {
        this.cardBrandModel = cardBrandModel;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceNo);
        dest.writeString(this.masterCardNo);
        dest.writeString(this.slaveCardNo);
        dest.writeString(this.singleQuota);
        dest.writeString(this.perDayQuota);
        dest.writeString(this._combinId);
        dest.writeString(this.conversationId);
    }

    public HceTransactionViewModel() {
    }

    protected HceTransactionViewModel(Parcel in) {
        this.deviceNo = in.readString();
        this.masterCardNo = in.readString();
        this.slaveCardNo = in.readString();
        this.singleQuota = in.readString();
        this.perDayQuota = in.readString();
        this._combinId = in.readString();
        this.conversationId = in.readString();
    }

    public static final Creator<HceTransactionViewModel> CREATOR = new Creator<HceTransactionViewModel>() {
        @Override
        public HceTransactionViewModel createFromParcel(Parcel source) {
            return new HceTransactionViewModel(source);
        }

        @Override
        public HceTransactionViewModel[] newArray(int size) {
            return new HceTransactionViewModel[size];
        }
    };

    // 用于在结果界面区分是"激活成功"还是"申请并激活成功"
    public  enum From {
        APPLY, // 来自申请后激活
        CARD_LIST,// 来自卡列表激活
    }

}
