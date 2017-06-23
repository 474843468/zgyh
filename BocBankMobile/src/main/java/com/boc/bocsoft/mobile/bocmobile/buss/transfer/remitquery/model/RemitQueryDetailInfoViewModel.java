package com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

/**
 * Created by wangf on 2016/7/5.
 */
public class RemitQueryDetailInfoViewModel implements Parcelable {

    /**
     * 上送数据
     */
    //网银交易序号
    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    /**
     * 返回数据
     */
    //汇款编号
    private String remitNo;
    //汇出账户
    private String cardNo;
    //汇出户名
    private String fromName;
    //汇款金额
    private String remitAmount;
    //币种
    private String currencyCode;
    //钞汇
    private String cashRemit;
    //收款人姓名
    private String payeeName;
    //收款人手机号
    private String payeeMobile;
    //汇款日期
    private LocalDate tranDate;
    //附言
    private String remark;
    //汇款状态
    private String remitStatus;
    //取款日期
    private LocalDate receiptDate;
    //代理点名称
    private String agentName;
    //代理点编号
    private String agentNum;
    //到期日期
    private LocalDate dueDate;

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(String remitAmount) {
        this.remitAmount = remitAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public LocalDate getTranDate() {
        return tranDate;
    }

    public void setTranDate(LocalDate tranDate) {
        this.tranDate = tranDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemitStatus() {
        return remitStatus;
    }

    public void setRemitStatus(String remitStatus) {
        this.remitStatus = remitStatus;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }

    public RemitQueryDetailInfoViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transactionId);
        dest.writeString(this.remitNo);
        dest.writeString(this.cardNo);
        dest.writeString(this.fromName);
        dest.writeString(this.remitAmount);
        dest.writeString(this.currencyCode);
        dest.writeString(this.cashRemit);
        dest.writeString(this.payeeName);
        dest.writeString(this.payeeMobile);
        dest.writeSerializable(this.tranDate);
        dest.writeString(this.remark);
        dest.writeString(this.remitStatus);
        dest.writeSerializable(this.receiptDate);
        dest.writeString(this.agentName);
        dest.writeString(this.agentNum);
        dest.writeSerializable(this.dueDate);
    }

    protected RemitQueryDetailInfoViewModel(Parcel in) {
        this.transactionId = in.readString();
        this.remitNo = in.readString();
        this.cardNo = in.readString();
        this.fromName = in.readString();
        this.remitAmount = in.readString();
        this.currencyCode = in.readString();
        this.cashRemit = in.readString();
        this.payeeName = in.readString();
        this.payeeMobile = in.readString();
        this.tranDate = (LocalDate) in.readSerializable();
        this.remark = in.readString();
        this.remitStatus = in.readString();
        this.receiptDate = (LocalDate) in.readSerializable();
        this.agentName = in.readString();
        this.agentNum = in.readString();
        this.dueDate = (LocalDate) in.readSerializable();
    }

    public static final Creator<RemitQueryDetailInfoViewModel> CREATOR = new Creator<RemitQueryDetailInfoViewModel>() {
        @Override
        public RemitQueryDetailInfoViewModel createFromParcel(Parcel source) {
            return new RemitQueryDetailInfoViewModel(source);
        }

        @Override
        public RemitQueryDetailInfoViewModel[] newArray(int size) {
            return new RemitQueryDetailInfoViewModel[size];
        }
    };
}
