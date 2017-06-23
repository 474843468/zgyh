package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 作者：xwg on 16/11/25 16:11
 */
public class RepaymentInfoBean implements Parcelable{
    /**
     * 服务id
     * 关联账户转账：PB021
     * 中行内转账：PB031
     * 中行内定向转账：PB033
     * 主动收款-付款：PB037C
     * 手机号转账：PB035
     */

    /**
     * 转出账户id
     */
    private String fromAccountId;
    /**
     * 转入账户id
     * 仅当关联账户转账时，上送此值，数字，非空
     */
    private String toAccountId;

    private String toAccountNo;

    private String fromAccountNo;
    private String toName;
    /**
     * 币种
     */
    private String currency;
    /**
     * 小于等于18位，必须是按币种格式化的金额，例如人民币：58,355.00
     */
    private String amount;
    /**
     * 钞汇标识
     * 仅当关联账户转账、主动收款-付款时，上送此值，且此时为必输项
     * -：00
     * 现钞：01
     * 现汇：02
     */
    private String cashRemit;


    private String devicePrint;


    private int payway;
    /**
     * 账单币种
     */
    private String billCurrency;

    /**
    *  本币账单已清
    */
    private  boolean localBillClear;
    /**
     *  本币账单已清
     */
    private boolean foreignBillClear;
    /**
     *  当前还款为本币还款
     */
    private boolean payLocalBill;


    private Double haveNotRepay;

    private String conversationId;

    //响应信息////////////////////////////////////////////////
    //交易流水号
    private String transactionId;
    //卡效期
    private String cardDate;
    //账户别名
    private String nickName;

    //手续费
    private String tranFee;
    //账户名称
    private String accountName;

    private BigDecimal exchangeRate;

    /**
    *  全球交易人民币记账标识  外币账户才有
     *  “ADTE”表示已设定
    */
    private String chargeFlag;

    /**
    *  外币账单自动购汇设置标识
     *  “1”表示已设定  “0”--未设定
    */
    private String foreignPayOffStatus;


//////////////////////////////////////////////////////////
    public RepaymentInfoBean() {
    }


    protected RepaymentInfoBean(Parcel in) {
        fromAccountId = in.readString();
        toAccountId = in.readString();
        toAccountNo = in.readString();
        fromAccountNo = in.readString();
        toName = in.readString();
        currency = in.readString();
        amount = in.readString();
        cashRemit = in.readString();
        devicePrint = in.readString();
        payway = in.readInt();
        localBillClear = in.readByte() != 0;
        foreignBillClear = in.readByte() != 0;
        payLocalBill = in.readByte() != 0;
        transactionId = in.readString();
        cardDate = in.readString();
        nickName = in.readString();
        tranFee = in.readString();
        billCurrency = in.readString();
        accountName = in.readString();
        chargeFlag = in.readString();
        foreignPayOffStatus = in.readString();
        conversationId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromAccountId);
        dest.writeString(toAccountId);
        dest.writeString(toAccountNo);
        dest.writeString(fromAccountNo);
        dest.writeString(toName);
        dest.writeString(currency);
        dest.writeString(amount);
        dest.writeString(cashRemit);
        dest.writeString(devicePrint);
        dest.writeInt(payway);
        dest.writeByte((byte) (localBillClear ? 1 : 0));
        dest.writeByte((byte) (foreignBillClear ? 1 : 0));
        dest.writeByte((byte) (payLocalBill ? 1 : 0));
        dest.writeString(transactionId);
        dest.writeString(cardDate);
        dest.writeString(nickName);
        dest.writeString(billCurrency);
        dest.writeString(tranFee);
        dest.writeString(accountName);
        dest.writeString(chargeFlag);
        dest.writeString(foreignPayOffStatus);
        dest.writeString(conversationId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RepaymentInfoBean> CREATOR = new Creator<RepaymentInfoBean>() {
        @Override
        public RepaymentInfoBean createFromParcel(Parcel in) {
            return new RepaymentInfoBean(in);
        }

        @Override
        public RepaymentInfoBean[] newArray(int size) {
            return new RepaymentInfoBean[size];
        }
    };

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public boolean isForeignBillClear() {
        return foreignBillClear;
    }

    public void setForeignBillClear(boolean foreignBillClear) {
        this.foreignBillClear = foreignBillClear;
    }

    public boolean isPayLocalBill() {
        return payLocalBill;
    }

    public void setPayLocalBill(boolean payLocalBill) {
        this.payLocalBill = payLocalBill;
    }

    public boolean isLocalBillClear() {
        return localBillClear;
    }

    public void setLocalBillClear(boolean localBillClear) {
        this.localBillClear = localBillClear;
    }

    public int getPayway() {
        return payway;
    }

    public void setPayway(int payway) {
        this.payway = payway;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getFromAccountNo() {
        return fromAccountNo;
    }

    public void setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    public String getToAccountNo() {
        return toAccountNo;
    }

    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTranFee() {
        return tranFee;
    }

    public void setTranFee(String tranFee) {
        this.tranFee = tranFee;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public Double getHaveNotRepay() {
        return haveNotRepay;
    }

    public void setHaveNotRepay(Double haveNotRepay) {
        this.haveNotRepay = haveNotRepay;
    }

    public String getBillCurrency() {
        return billCurrency;
    }

    public void setBillCurrency(String billCurrency) {
        this.billCurrency = billCurrency;
    }

    public String getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(String chargeFlag) {
        this.chargeFlag = chargeFlag;
    }

    public String getForeignPayOffStatus() {
        return foreignPayOffStatus;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setForeignPayOffStatus(String foreignPayOffStatus) {
        this.foreignPayOffStatus = foreignPayOffStatus;
    }
}
