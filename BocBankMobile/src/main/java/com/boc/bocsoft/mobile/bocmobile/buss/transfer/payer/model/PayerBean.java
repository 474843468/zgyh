package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;

/**
 * Created by liuyang on 2016/7/25.
 */
public class PayerBean implements Parcelable {

    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 付款人客户号
     */
    private int payerCustomerId;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 付款人类型
     */
    private String identifyType;
    /**
     * 付款人ID
     */
    private int payerId;
    /**
     * 用来搜索的字母
     */
    private String pinyin;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public int getPayerCustomerId() {
        return payerCustomerId;
    }

    public void setPayerCustomerId(int payerCustomerId) {
        this.payerCustomerId = payerCustomerId;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }


    public String getPinyin() {
        return pinyin;
    }


    /**
     * 汉字转拼音
     * @param pinyin
     */
    public void setPinyin(String pinyin) {
        if (!TextUtils.isEmpty(pinyin)) {
            this.pinyin = pinyin;
        } else {
            String pinYin = PinYinUtil.getPinYin(this.payerName);
            pinYin = pinYin.trim();
            this.pinyin = TextUtils.isEmpty(pinYin) ? "z" : pinYin.toUpperCase();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payerName);
        dest.writeInt(this.payerCustomerId);
        dest.writeString(this.payerMobile);
        dest.writeString(this.identifyType);
        dest.writeInt(this.payerId);
        dest.writeString(this.pinyin);
    }

    public PayerBean() {
    }

    private PayerBean(Parcel in) {
        this.payerName = in.readString();
        this.payerCustomerId = in.readInt();
        this.payerMobile = in.readString();
        this.identifyType = in.readString();
        this.payerId = in.readInt();
        this.pinyin = in.readString();
    }

    public static final Parcelable.Creator<PayerBean> CREATOR = new Parcelable.Creator<PayerBean>() {
        public PayerBean createFromParcel(Parcel source) {
            return new PayerBean(source);
        }

        public PayerBean[] newArray(int size) {
            return new PayerBean[size];
        }
    };
}
