package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

/**
 * Created by wangf on 2016/7/5.
 */
public class TransferRecordDetailInfoViewModel implements Parcelable {

    /**
     * 上送报文
     */

    /**
     * transId : 1913189
     */

    //网银交易序号
    private String transId;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }


    /**
     * 返回报文
     */
    //交易渠道
    private String channel;
    //转账状态
    private String status;
    //网银交易流水号
    private String transactionId;
    //转出账户
    private String payerAccountNumber;
    //金额
    private String amount;
    //转账日期
    private LocalDate paymentDate;
    //初次提交日期
    private LocalDate firstSubmitDate;
    //返回码标示失败原因
    private String returnCode;
    //转入帐户名称
    private String payeeAccountName;
    //转入账户
    private String payeeAccountNumber;
    //转出帐户名称
    private String payerAccountName;
    //转账批次号
    private String batSeq;
    //手续费
    private String commissionCharge;
    //备　　注
    private String furInfo;
    //转入账户开户行
    private String payeeBankName;
    //转入账户地区
    private String payeeIbk;
    //电汇费
    private String postage;
    //转出账号地区
    private String payerIbknum;
    //转入帐户类型
    private String payeeAccountType;
    //转出帐户类型
    private String payerAccountType;
    //钞汇标志
    private String cashRemit;
    //转账类型
    private String transferType;
    //转账方式
    private String transMode;
    //收款人常驻国家（地区）
    private String payeeCountry;
    //币　　种
    private String feeCur;
    //付费币种
    private String feeCur2;
    //钞转汇差价
    private String cashRemitExchange;
    //退汇交易状态
    private String reexchangeStatus;

    public String getFeeCur2() {
        return feeCur2;
    }

    public void setFeeCur2(String feeCur2) {
        this.feeCur2 = feeCur2;
    }

    public String getCashRemitExchange() {
        return cashRemitExchange;
    }

    public void setCashRemitExchange(String cashRemitExchange) {
        this.cashRemitExchange = cashRemitExchange;
    }

    public String getReexchangeStatus() {
        return reexchangeStatus;
    }

    public void setReexchangeStatus(String reexchangeStatus) {
        this.reexchangeStatus = reexchangeStatus;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(String payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getFirstSubmitDate() {
        return firstSubmitDate;
    }

    public void setFirstSubmitDate(LocalDate firstSubmitDate) {
        this.firstSubmitDate = firstSubmitDate;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    public String getPayerAccountName() {
        return payerAccountName;
    }

    public void setPayerAccountName(String payerAccountName) {
        this.payerAccountName = payerAccountName;
    }

    public String getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(String batSeq) {
        this.batSeq = batSeq;
    }

    public String getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(String commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public String getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeIbk() {
        return payeeIbk;
    }

    public void setPayeeIbk(String payeeIbk) {
        this.payeeIbk = payeeIbk;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getPayerIbknum() {
        return payerIbknum;
    }

    public void setPayerIbknum(String payerIbknum) {
        this.payerIbknum = payerIbknum;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getPayerAccountType() {
        return payerAccountType;
    }

    public void setPayerAccountType(String payerAccountType) {
        this.payerAccountType = payerAccountType;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getPayeeCountry() {
        return payeeCountry;
    }

    public void setPayeeCountry(String payeeCountry) {
        this.payeeCountry = payeeCountry;
    }

    public String getFeeCur() {
        return feeCur;
    }

    public void setFeeCur(String feeCur) {
        this.feeCur = feeCur;
    }

    public TransferRecordDetailInfoViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transId);
        dest.writeString(this.channel);
        dest.writeString(this.status);
        dest.writeString(this.transactionId);
        dest.writeString(this.payerAccountNumber);
        dest.writeString(this.amount);
        dest.writeSerializable(this.paymentDate);
        dest.writeSerializable(this.firstSubmitDate);
        dest.writeString(this.returnCode);
        dest.writeString(this.payeeAccountName);
        dest.writeString(this.payeeAccountNumber);
        dest.writeString(this.payerAccountName);
        dest.writeString(this.batSeq);
        dest.writeString(this.commissionCharge);
        dest.writeString(this.furInfo);
        dest.writeString(this.payeeBankName);
        dest.writeString(this.payeeIbk);
        dest.writeString(this.postage);
        dest.writeString(this.payerIbknum);
        dest.writeString(this.payeeAccountType);
        dest.writeString(this.payerAccountType);
        dest.writeString(this.cashRemit);
        dest.writeString(this.transferType);
        dest.writeString(this.transMode);
        dest.writeString(this.payeeCountry);
        dest.writeString(this.feeCur);
        dest.writeString(this.feeCur2);
        dest.writeString(this.cashRemitExchange);
        dest.writeString(this.reexchangeStatus);
    }

    protected TransferRecordDetailInfoViewModel(Parcel in) {
        this.transId = in.readString();
        this.channel = in.readString();
        this.status = in.readString();
        this.transactionId = in.readString();
        this.payerAccountNumber = in.readString();
        this.amount = in.readString();
        this.paymentDate = (LocalDate) in.readSerializable();
        this.firstSubmitDate = (LocalDate) in.readSerializable();
        this.returnCode = in.readString();
        this.payeeAccountName = in.readString();
        this.payeeAccountNumber = in.readString();
        this.payerAccountName = in.readString();
        this.batSeq = in.readString();
        this.commissionCharge = in.readString();
        this.furInfo = in.readString();
        this.payeeBankName = in.readString();
        this.payeeIbk = in.readString();
        this.postage = in.readString();
        this.payerIbknum = in.readString();
        this.payeeAccountType = in.readString();
        this.payerAccountType = in.readString();
        this.cashRemit = in.readString();
        this.transferType = in.readString();
        this.transMode = in.readString();
        this.payeeCountry = in.readString();
        this.feeCur = in.readString();
        this.feeCur2 = in.readString();
        this.cashRemitExchange = in.readString();
        this.reexchangeStatus = in.readString();
    }

    public static final Creator<TransferRecordDetailInfoViewModel> CREATOR = new Creator<TransferRecordDetailInfoViewModel>() {
        @Override
        public TransferRecordDetailInfoViewModel createFromParcel(Parcel source) {
            return new TransferRecordDetailInfoViewModel(source);
        }

        @Override
        public TransferRecordDetailInfoViewModel[] newArray(int size) {
            return new TransferRecordDetailInfoViewModel[size];
        }
    };
}
