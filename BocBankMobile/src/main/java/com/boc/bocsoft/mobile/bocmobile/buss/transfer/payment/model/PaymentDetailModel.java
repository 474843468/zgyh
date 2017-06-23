package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnPaymentOrderDetailResult;

/**
 * Created by wangtong on 2016/6/28.
 */
public class PaymentDetailModel implements Parcelable {
    //发起渠道
    private String createChannel;
    //催款日期
    private String createDate;
    //备注
    private String furInfo;
    //指令序号
    private String notifyId;
    //收款人账号
    private String payeeAccountNumber;
    //收款人账户类型
    private String payeeAccountType;
    //收款人联行号
    private String payeeIbk;
    //收款人手机
    private String payeeMobile;
    //收款人姓名
    private String payeeName;
    //付款人账号
    private String payerAccountNumber;
    //付款人账户类型
    private String payerAccountType;
    //付款人客户号
    private String payerCustomerId;
    //付款人联行号
    private String payerIbknum;
    //付款人手机
    private String payerMobile;
    //付款人姓名
    private String payerName;
    //付款日期
    private String paymentDate;
    //催款金额
    private String requestAmount;
    //交易状态
    private String status;
    //实付金额
    private String trfAmount;
    //付款渠道
    private String trfChannel;
    //币种
    private String trfCur;
    //交易序列号
    private String transactionId;

    public void transFromPresent(PsnPaymentOrderDetailResult result) {
        createChannel = result.getCreateChannel();
        createDate = result.getCreateDate();
        furInfo = result.getFurInfo();
        notifyId = result.getNotifyId() + "";
        payeeName = result.getPayeeName();
        payeeMobile = result.getPayeeMobile();
        payeeAccountNumber = result.getPayeeAccountNumber();
        payeeAccountType = result.getPayeeAccountType();
        payerCustomerId = result.getPayerCustomerId();
        payerIbknum = result.getPayerIbknum();
        payerMobile = result.getPayerMobile();
        payerName = result.getPayerName();
        paymentDate = result.getPaymentDate();
        payerAccountNumber = result.getPayerAccountNumber();
        requestAmount = result.getRequestAmount() + "";
        status = result.getStatus();
        trfAmount = result.getTrfAmount() + "";
        if (trfAmount.equals("0.0")) {
            trfAmount = "";
        }
        trfChannel = result.getTrfChannel();
        trfCur = result.getTrfCur();
        transactionId = result.getTransactionId();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String trfNumber) {
        this.transactionId = trfNumber;
    }

    public String getCreateChannel() {
        return createChannel;
    }

    public void setCreateChannel(String createChannel) {
        this.createChannel = createChannel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getPayeeIbk() {
        return payeeIbk;
    }

    public void setPayeeIbk(String payeeIbk) {
        this.payeeIbk = payeeIbk;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(String payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
    }

    public String getPayerAccountType() {
        return payerAccountType;
    }

    public void setPayerAccountType(String payerAccountType) {
        this.payerAccountType = payerAccountType;
    }

    public String getPayerCustomerId() {
        return payerCustomerId;
    }

    public void setPayerCustomerId(String payerCustomerId) {
        this.payerCustomerId = payerCustomerId;
    }

    public String getPayerIbknum() {
        return payerIbknum;
    }

    public void setPayerIbknum(String payerIbknum) {
        this.payerIbknum = payerIbknum;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(String requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(String trfAmount) {
        this.trfAmount = trfAmount;
    }

    public String getTrfChannel() {
        return trfChannel;
    }

    public void setTrfChannel(String trfChannel) {
        this.trfChannel = trfChannel;
    }

    public String getTrfCur() {
        return trfCur;
    }

    public void setTrfCur(String trfCur) {
        this.trfCur = trfCur;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createChannel);
        dest.writeString(this.createDate);
        dest.writeString(this.furInfo);
        dest.writeString(this.notifyId);
        dest.writeString(this.payeeAccountNumber);
        dest.writeString(this.payeeAccountType);
        dest.writeString(this.payeeIbk);
        dest.writeString(this.payeeMobile);
        dest.writeString(this.payeeName);
        dest.writeString(this.payerAccountNumber);
        dest.writeString(this.payerAccountType);
        dest.writeString(this.payerCustomerId);
        dest.writeString(this.payerIbknum);
        dest.writeString(this.payerMobile);
        dest.writeString(this.payerName);
        dest.writeString(this.paymentDate);
        dest.writeString(this.requestAmount);
        dest.writeString(this.status);
        dest.writeString(this.trfAmount);
        dest.writeString(this.trfChannel);
        dest.writeString(this.trfCur);
        dest.writeString(this.transactionId);
    }

    public PaymentDetailModel() {
    }

    protected PaymentDetailModel(Parcel in) {
        this.createChannel = in.readString();
        this.createDate = in.readString();
        this.furInfo = in.readString();
        this.notifyId = in.readString();
        this.payeeAccountNumber = in.readString();
        this.payeeAccountType = in.readString();
        this.payeeIbk = in.readString();
        this.payeeMobile = in.readString();
        this.payeeName = in.readString();
        this.payerAccountNumber = in.readString();
        this.payerAccountType = in.readString();
        this.payerCustomerId = in.readString();
        this.payerIbknum = in.readString();
        this.payerMobile = in.readString();
        this.payerName = in.readString();
        this.paymentDate = in.readString();
        this.requestAmount = in.readString();
        this.status = in.readString();
        this.trfAmount = in.readString();
        this.trfChannel = in.readString();
        this.trfCur = in.readString();
        this.transactionId = in.readString();
    }

    public static final Parcelable.Creator<PaymentDetailModel> CREATOR = new Parcelable.Creator<PaymentDetailModel>() {
        @Override
        public PaymentDetailModel createFromParcel(Parcel source) {
            return new PaymentDetailModel(source);
        }

        @Override
        public PaymentDetailModel[] newArray(int size) {
            return new PaymentDetailModel[size];
        }
    };
}
