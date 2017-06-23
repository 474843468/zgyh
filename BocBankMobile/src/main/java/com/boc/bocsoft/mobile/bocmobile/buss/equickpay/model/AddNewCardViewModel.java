package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by yangle on 2016/12/15.
 * 描述:
 */
public class AddNewCardViewModel implements Parcelable {

    private static final String DEFAULT_COUNT = "10";

    private String deviceNo;//	设备号
    private String masterCardNo;// 主卡卡号
    private String accountId;// 账户标识
    private String accountType;//卡的类型
    private String singleQuota;// 单笔限额
    private String perDayQuota;// 日限额
    private String keyNum = DEFAULT_COUNT; // 密钥数 暂固定10
    private boolean isAgree;
    private String conversationId;
    private CardBrandModel cardBrandModel;// 卡品牌


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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSingleQuota() {
        return singleQuota;
    }

    public void setSingleQuota(String singleQuota) {
        this.singleQuota = singleQuota;
    }

    public String getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(String keyNum) {
        this.keyNum = keyNum;
    }

    public boolean isAgree() {
        return isAgree;
    }

    public void setAgree(boolean agree) {
        isAgree = agree;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public CardBrandModel getCardBrandModel() {
        return cardBrandModel;
    }

    public void setCardBrandModel(CardBrandModel cardBrandModel) {
        this.cardBrandModel = cardBrandModel;
    }

    public String getPerDayQuota() {
        return perDayQuota;
    }

    public void setPerDayQuota(String perDayQuota) {
        this.perDayQuota = perDayQuota;
    }

    public String getMasterCardNoFormat() {
        return NumberUtils.formatCardNumber(masterCardNo);
    }


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isCreditCard() {
        if (StringUtils.isEmpty(accountType)) {
            throw new IllegalArgumentException("accountType == null or ''");
        }
        return AccountUtils.getCardType(accountType) == AccountUtils.CardType.CREDIT_CARD;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceNo);
        dest.writeString(this.masterCardNo);
        dest.writeString(this.accountId);
        dest.writeString(this.singleQuota);
        dest.writeString(this.perDayQuota);
        dest.writeString(this.keyNum);
        dest.writeByte(isAgree ? (byte) 1 : (byte) 0);
        dest.writeString(this.conversationId);
        dest.writeParcelable(this.cardBrandModel, flags);
    }

    public AddNewCardViewModel() {
    }

    protected AddNewCardViewModel(Parcel in) {
        this.deviceNo = in.readString();
        this.masterCardNo = in.readString();
        this.accountId = in.readString();
        this.singleQuota = in.readString();
        this.perDayQuota = in.readString();
        this.keyNum = in.readString();
        this.isAgree = in.readByte() != 0;
        this.conversationId = in.readString();
        this.cardBrandModel = in.readParcelable(CardBrandModel.class.getClassLoader());
    }

    public static final Creator<AddNewCardViewModel> CREATOR = new Creator<AddNewCardViewModel>() {
        @Override
        public AddNewCardViewModel createFromParcel(Parcel source) {
            return new AddNewCardViewModel(source);
        }

        @Override
        public AddNewCardViewModel[] newArray(int size) {
            return new AddNewCardViewModel[size];
        }
    };
}
