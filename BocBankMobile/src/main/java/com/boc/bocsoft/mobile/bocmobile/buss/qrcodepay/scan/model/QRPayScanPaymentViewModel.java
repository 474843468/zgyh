package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

/**
 * 正扫支付 View层model
 * Created by wangf on 2016/9/5.
 */
public class QRPayScanPaymentViewModel implements Parcelable {


    /**
     * 正扫支付
     * 上送数据项
     */
    //网银账户流水号
    private String actSeq;
    //收款商户号
    private String merchantNo;
    //收款商户名称
    private String merchantName;
    //终端号
    private String terminalId ;
    //支付密码
    private String password;
    //支付密码
    private String password_RC;
    //订单金额
    private String tranAmount;
    private String passType;
    private String activ;
    private String state;


    /**
     * 正扫支付
     * 返回数据项
     */
    //交易币种
    private String curCode;
    //错误码
    private String errCode;
    //错误信息
    private String errMsg;
    private String merchantCatNo;
//    //收款商户名称
//    private String merchantName;
//    //收款商户号
//    private String merchantNo;
    //HHAP交易流水号
    private String settleKey;
    //交易状态
    private String status;
//    //交易金额
//    private String tranAmount;
    //收款商户号
    private String voucherNo;
    //交易时间
    private LocalDateTime tranTime;
    //网银交易流水号
    private String tranSeq;


    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_RC() {
        return password_RC;
    }

    public void setPassword_RC(String password_RC) {
        this.password_RC = password_RC;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
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

    public String getMerchantCatNo() {
        return merchantCatNo;
    }

    public void setMerchantCatNo(String merchantCatNo) {
        this.merchantCatNo = merchantCatNo;
    }

    public String getSettleKey() {
        return settleKey;
    }

    public void setSettleKey(String settleKey) {
        this.settleKey = settleKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public LocalDateTime getTranTime() {
        return tranTime;
    }

    public void setTranTime(LocalDateTime tranTime) {
        this.tranTime = tranTime;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.actSeq);
        dest.writeString(this.merchantNo);
        dest.writeString(this.merchantName);
        dest.writeString(this.terminalId);
        dest.writeString(this.password);
        dest.writeString(this.password_RC);
        dest.writeString(this.tranAmount);
        dest.writeString(this.passType);
        dest.writeString(this.activ);
        dest.writeString(this.state);
        dest.writeString(this.curCode);
        dest.writeString(this.errCode);
        dest.writeString(this.errMsg);
        dest.writeString(this.merchantCatNo);
        dest.writeString(this.settleKey);
        dest.writeString(this.status);
        dest.writeString(this.voucherNo);
        dest.writeSerializable(this.tranTime);
        dest.writeString(this.tranSeq);
    }

    public QRPayScanPaymentViewModel() {
    }

    protected QRPayScanPaymentViewModel(Parcel in) {
        this.actSeq = in.readString();
        this.merchantNo = in.readString();
        this.merchantName = in.readString();
        this.terminalId = in.readString();
        this.password = in.readString();
        this.password_RC = in.readString();
        this.tranAmount = in.readString();
        this.passType = in.readString();
        this.activ = in.readString();
        this.state = in.readString();
        this.curCode = in.readString();
        this.errCode = in.readString();
        this.errMsg = in.readString();
        this.merchantCatNo = in.readString();
        this.settleKey = in.readString();
        this.status = in.readString();
        this.voucherNo = in.readString();
        this.tranTime = (LocalDateTime) in.readSerializable();
        this.tranSeq = in.readString();
    }

    public static final Creator<QRPayScanPaymentViewModel> CREATOR = new Creator<QRPayScanPaymentViewModel>() {
        @Override
        public QRPayScanPaymentViewModel createFromParcel(Parcel source) {
            return new QRPayScanPaymentViewModel(source);
        }

        @Override
        public QRPayScanPaymentViewModel[] newArray(int size) {
            return new QRPayScanPaymentViewModel[size];
        }
    };
}
