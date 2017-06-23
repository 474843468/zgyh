package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 二维码付款 View层数据模型
 * Created by wangf on 2016/8/29.
 */
public class QRPayGetQRCodeViewModel implements Parcelable{

    /**
     * 获取二维码
     * 返回数据项
     */

    private String seqNo;//二维码流水号
    private String lifeTime;//支付串的有效期
    private String getConfirmInfoFreq;//前端刷新频率


    /**
     * 查询反扫后的交易确认通知
     * 返回数据项
     */
    //商户名称
    private String merchantName;
    //商户号
    private String merchantNo;
    //商户类别代码
    private String merchantType;
    //支付金额
    private String amount;
    //系统跟踪号 -- ICCD返回的跟踪号
    private String settleKey;
    private String resStatus;

    private String confirmInfoConversationID;


//    /**
//     * 查询反扫支付交易信息
//     * 返回数据项
//     */
//    //交易时间
//    private String tranTime;
//    //交易结果
//    private String tranStatus;
//    //交易金额
//    private String tranAmount;
//    //交易说明
//    private String tranRemark;


    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getGetConfirmInfoFreq() {
        return getConfirmInfoFreq;
    }

    public void setGetConfirmInfoFreq(String getConfirmInfoFreq) {
        this.getConfirmInfoFreq = getConfirmInfoFreq;
    }

    public String getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(String lifeTime) {
        this.lifeTime = lifeTime;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSettleKey() {
        return settleKey;
    }

    public void setSettleKey(String settleKey) {
        this.settleKey = settleKey;
    }

    public String getResStatus() {
        return resStatus;
    }

    public void setResStatus(String resStatus) {
        this.resStatus = resStatus;
    }

    public String getConfirmInfoConversationID() {
        return confirmInfoConversationID;
    }

    public void setConfirmInfoConversationID(String confirmInfoConversationID) {
        this.confirmInfoConversationID = confirmInfoConversationID;
    }

    public QRPayGetQRCodeViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.seqNo);
        dest.writeString(this.lifeTime);
        dest.writeString(this.getConfirmInfoFreq);
        dest.writeString(this.merchantName);
        dest.writeString(this.merchantNo);
        dest.writeString(this.merchantType);
        dest.writeString(this.amount);
        dest.writeString(this.settleKey);
        dest.writeString(this.resStatus);
        dest.writeString(this.confirmInfoConversationID);
    }

    protected QRPayGetQRCodeViewModel(Parcel in) {
        this.seqNo = in.readString();
        this.lifeTime = in.readString();
        this.getConfirmInfoFreq = in.readString();
        this.merchantName = in.readString();
        this.merchantNo = in.readString();
        this.merchantType = in.readString();
        this.amount = in.readString();
        this.settleKey = in.readString();
        this.resStatus = in.readString();
        this.confirmInfoConversationID = in.readString();
    }

    public static final Creator<QRPayGetQRCodeViewModel> CREATOR = new Creator<QRPayGetQRCodeViewModel>() {
        @Override
        public QRPayGetQRCodeViewModel createFromParcel(Parcel source) {
            return new QRPayGetQRCodeViewModel(source);
        }

        @Override
        public QRPayGetQRCodeViewModel[] newArray(int size) {
            return new QRPayGetQRCodeViewModel[size];
        }
    };
}
