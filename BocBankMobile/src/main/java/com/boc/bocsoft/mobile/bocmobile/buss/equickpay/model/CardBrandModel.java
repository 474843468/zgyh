package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;

/**
 * Created by yangle on 2016/12/15.
 * 描述:
 */
public class CardBrandModel implements Parcelable {

    /**
     * 100：银联
     * 010：VISA
     * 001: 万事达
     * 110: 银联、VISA  // 双标卡,默认非银联卡
     * 101：银联、万事达
     */
    private String cardBrandId;
    private String cardBrandName;

    //private String appTypeName;//对应的应用类型名称
    //private int appTypeValue;// 对应的应用类型
    private boolean isCredit = false;
    private HceConstants.CardOrg appType;
    private String cardBrandHceValue;

    public CardBrandModel(String cardBrandId) {
        this.cardBrandId = cardBrandId;
        initAppType();
    }

    public CardBrandModel() {
    }

    public String getCardBrandId() {
        return cardBrandId;
    }

    public void setCardBrandId(String cardBrandId) {
        this.cardBrandId = cardBrandId;
        initAppType();
    }

    private void initAppType() {
        if (isMasterCard()) {
            appType = HceConstants.CardOrg.MasterCard;
        }

        if (isVISACard()) {
            appType = HceConstants.CardOrg.VISA;
        }

        if (isPBOCCard()) {
            if (isCredit) {
                appType = HceConstants.CardOrg.PBOC_Credit;
            } else {
                appType = HceConstants.CardOrg.PBOC_Debit;
            }
        }
    }

    // 对就的应用类型
    public HceConstants.CardOrg getAppType() {
        return appType;
    }

    // 卡品牌对应的应用类型名称
    public String getAppTypeName() {
        return appType.toString();
    }

    public String getCardBrandName() {
        switch (appType) {
            case MasterCard:
                cardBrandName = "Master Card";
                break;
            case VISA:
                cardBrandHceValue = "VISA";
                break;
            case PBOC_Credit:
            case PBOC_Debit:
                cardBrandName = "银联";
                break;
            default:
                break;
        }
        return cardBrandName;
    }

    public int getAppTypeValue() {
        return appType.ordinal() + 1;
    }

    public String getCardBrandHceValue() {
        switch (appType) {
            case MasterCard:
                cardBrandHceValue = HceConstants.MasterTypeStr;
                break;
            case PBOC_Credit:
                cardBrandHceValue = HceConstants.PbocCreditTypeTypeStr;
                break;
            case VISA:
                cardBrandHceValue = HceConstants.VisaTypeStr;
                break;
            case PBOC_Debit:
                cardBrandHceValue = HceConstants.PbocDebitTypeTypeStr;
                break;
            default:
                break;
        }
        return cardBrandHceValue;
    }

    // 判断银联
    public boolean isPBOCCard() {
        if (!isCorrectCardBrandId()) {
            return false;
        }
        return "100".equals(cardBrandId);
    }

    // 判断VISA
    public boolean isVISACard() {
        if (!isCorrectCardBrandId()) {return false;}
        return "010".equals(cardBrandId) || "110".equals(cardBrandId);
    }

    // 判断MASTER
    public boolean isMasterCard() {
        if (!isCorrectCardBrandId()) {
            return false;
        }
        return "001".equals(cardBrandId) || "101".equals(cardBrandId);
    }

    private boolean isCorrectCardBrandId() {
        try {
            if (cardBrandId == null) {
                throw new NullPointerException("cardBrandId == null");
            }

            if (cardBrandId.isEmpty() || cardBrandId.length()!=3) {
                throw new IllegalArgumentException("cardBrandId wrong format");
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }

    public void setAppType(HceConstants.CardOrg appType) {
        this.appType = appType;
    }


    public void setCardBrandHceValue(String cardBrandHceValue) {
        this.cardBrandHceValue = cardBrandHceValue;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cardBrandId);
        dest.writeByte(isCredit ? (byte) 1 : (byte) 0);
        dest.writeInt(this.appType == null ? -1 : this.appType.ordinal());
        dest.writeString(this.cardBrandHceValue);
    }

    protected CardBrandModel(Parcel in) {
        this.cardBrandId = in.readString();
        this.isCredit = in.readByte() != 0;
        int tmpAppType = in.readInt();
        this.appType = tmpAppType == -1 ? null : HceConstants.CardOrg.values()[tmpAppType];
        this.cardBrandHceValue = in.readString();
    }

    public static final Creator<CardBrandModel> CREATOR = new Creator<CardBrandModel>() {
        @Override
        public CardBrandModel createFromParcel(Parcel source) {
            return new CardBrandModel(source);
        }

        @Override
        public CardBrandModel[] newArray(int size) {
            return new CardBrandModel[size];
        }
    };
}
