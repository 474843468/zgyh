package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by wangyuan on 2016/6/28.
 */
public class TransRemitResultViewModel implements Parcelable {




    private String  accType;//付款账户类型
    private String  status;//结果状态
    private String  transtype;//转账类型
    private String	transactionId;//网银交易序列号
    private String	amount;//转账金额
    private String  currency;//币种
    private String  cashRemit;//钞汇标示
    private BigDecimal finalCommissionCharge;//优惠后费率
    private String  batSequence;
    private String  toAccountNumber;
    private String	payeeName;//收款人姓名
    private String	payerName;//付款人姓名
    private String	bankname;//收款银行
    private String	toOrgname;//开户行
    private String	payeeMobile;//收款人手机号
    private String	remark;//附言
    private String	fromAccountNum;//转出账户
    private String	availableBalance2;//账户交易后可用余额
    private String	executeDate;//预约转账日期
    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }
    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    private String	executeType;//预约转账类型

    public String getExecuteTypeName() {
        return executeTypeName;
    }

    public void setExecuteTypeName(String executeTypeName) {
        this.executeTypeName = executeTypeName;
    }

    private String	executeTypeName;//预约转账类型
    private String  bankLocation;//所属地区

    public String getIschangeBooking() {
        return ischangeBooking;
    }

    public void setIschangeBooking(String ischangeBooking) {
        this.ischangeBooking = ischangeBooking;
    }

    private String  ischangeBooking;//是否是已预约交易


    public String getFunctionFrom() {
        return functionFrom;
    }

    public void setFunctionFrom(String functionFrom) {
        this.functionFrom = functionFrom;
    }

    private String  functionFrom;//是转账类型，转账默认"";
    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }


    public String getBankLocation() {
        return bankLocation;
    }

    public void setBankLocation(String bankLocation) {
        this.bankLocation = bankLocation;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayerAccIbkNum() {
        return payerAccIbkNum;
    }

    public void setPayerAccIbkNum(String payerAccIbkNum) {
        this.payerAccIbkNum = payerAccIbkNum;
    }

    private String  payerAccIbkNum;
    public String getPayeeBankIbkNum() {
        return payeeBankIbkNum;
    }

    public void setPayeeBankIbkNum(String payeeBankIbkNum) {
        this.payeeBankIbkNum = payeeBankIbkNum;
    }

    public static Creator<TransRemitResultViewModel> getCREATOR() {
        return CREATOR;
    }

    private String  payeeBankIbkNum;//收款银行地区码


    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }
    public String getTranstype() {
        return transtype;
    }

    public void setTranstype(String transtype) {
        this.transtype = transtype;
    }
    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getBatSequence() {
        return batSequence;
    }

    public void setBatSequence(String batSequence) {
        this.batSequence = batSequence;
    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAvailableBalance2() {
        return availableBalance2;
    }

    public void setAvailableBalance2(String availableBalance) {
        this.availableBalance2 = availableBalance;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getFinalCommissionCharge() {
        return finalCommissionCharge;
    }

    public void setFinalCommissionCharge(BigDecimal finalCommissionCharge) {
        this.finalCommissionCharge = finalCommissionCharge;
    }

    public String getFromAccountNum() {
        return fromAccountNum;
    }

    public void setFromAccountNum(String fromAccountNum) {
        this.fromAccountNum = fromAccountNum;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToOrgname() {
        return toOrgname;
    }

    public void setToOrgname(String toOrgname) {
        this.toOrgname = toOrgname;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransRemitResultViewModel(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.transtype);
        dest.writeString(this.transactionId);
        dest.writeString(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.cashRemit);
        dest.writeSerializable(this.finalCommissionCharge);
        dest.writeString(this.batSequence);
        dest.writeString(this.toAccountNumber);
        dest.writeString(this.payeeName);
        dest.writeString(this.payerName);
        dest.writeString(this.bankname);
        dest.writeString(this.toOrgname);
        dest.writeString(this.payeeMobile);
        dest.writeString(this.remark);
        dest.writeString(this.fromAccountNum);
        dest.writeString(this.availableBalance2);
        dest.writeString(this.executeDate);
        dest.writeString(this.executeType);
        dest.writeString(this.executeTypeName);
        dest.writeString(this.bankLocation);
        dest.writeString(this.ischangeBooking);
        dest.writeString(this.functionFrom);
        dest.writeString(this.payerAccIbkNum);
        dest.writeString(this.payeeBankIbkNum);
    }

    protected TransRemitResultViewModel(Parcel in) {
        this.status = in.readString();
        this.transtype = in.readString();
        this.transactionId = in.readString();
        this.amount = in.readString();
        this.currency = in.readString();
        this.cashRemit = in.readString();
        this.finalCommissionCharge = (BigDecimal) in.readSerializable();
        this.batSequence = in.readString();
        this.toAccountNumber = in.readString();
        this.payeeName = in.readString();
        this.payerName = in.readString();
        this.bankname = in.readString();
        this.toOrgname = in.readString();
        this.payeeMobile = in.readString();
        this.remark = in.readString();
        this.fromAccountNum = in.readString();
        this.availableBalance2 = in.readString();
        this.executeDate = in.readString();
        this.executeType = in.readString();
        this.executeTypeName = in.readString();
        this.bankLocation = in.readString();
        this.ischangeBooking = in.readString();
        this.functionFrom = in.readString();
        this.payerAccIbkNum = in.readString();
        this.payeeBankIbkNum = in.readString();
    }

    public static final Creator<TransRemitResultViewModel> CREATOR = new Creator<TransRemitResultViewModel>() {
        @Override
        public TransRemitResultViewModel createFromParcel(Parcel source) {
            return new TransRemitResultViewModel(source);
        }

        @Override
        public TransRemitResultViewModel[] newArray(int size) {
            return new TransRemitResultViewModel[size];
        }
    };
}
