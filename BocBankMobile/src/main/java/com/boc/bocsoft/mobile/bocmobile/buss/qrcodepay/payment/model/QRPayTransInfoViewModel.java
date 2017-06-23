package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 查询反扫支付交易信息
 * Created by wangf on 2016/9/19.
 */
public class QRPayTransInfoViewModel implements Parcelable{

    /**
     * 查询反扫支付交易信息
     * 返回数据项
     */
    //交易时间
    private String tranTime;
    //交易结果
    private String tranStatus;
    //错误码
    private String errCode;
    //错误信息
    private String errMsg;
    //交易金额
    private String tranAmount;
    //付款凭证号 交易成功时返回
    private String voucherNum;
    //交易说明
    private String tranRemark;

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getTranRemark() {
        return tranRemark;
    }

    public void setTranRemark(String tranRemark) {
        this.tranRemark = tranRemark;
    }

    public String getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(String voucherNum) {
        this.voucherNum = voucherNum;
    }


    public QRPayTransInfoViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tranTime);
        dest.writeString(this.tranStatus);
        dest.writeString(this.errCode);
        dest.writeString(this.errMsg);
        dest.writeString(this.tranAmount);
        dest.writeString(this.voucherNum);
        dest.writeString(this.tranRemark);
    }

    protected QRPayTransInfoViewModel(Parcel in) {
        this.tranTime = in.readString();
        this.tranStatus = in.readString();
        this.errCode = in.readString();
        this.errMsg = in.readString();
        this.tranAmount = in.readString();
        this.voucherNum = in.readString();
        this.tranRemark = in.readString();
    }

    public static final Creator<QRPayTransInfoViewModel> CREATOR = new Creator<QRPayTransInfoViewModel>() {
        @Override
        public QRPayTransInfoViewModel createFromParcel(Parcel source) {
            return new QRPayTransInfoViewModel(source);
        }

        @Override
        public QRPayTransInfoViewModel[] newArray(int size) {
            return new QRPayTransInfoViewModel[size];
        }
    };
}
