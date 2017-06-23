package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 信用卡消费分期预交易
 * Created by lq7090 on 2016/11/17.
 */
public class ConsumeTransModel {


    private int recordNumber;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    /**
     * sequence : 2
     * transType : null
     * transId : 2
     * transDate : 2013/10/15
     * transCode : 2010
     * acctNum : 5257451165233259
     * mainAcctId : 001A03F700491C03
     * clearingCurrency : 001
     * clearingAmount : 7000
     * tranCurrency : 001
     * tranAmount : 7000
     * acctNumTail : 3259
     * transDesc : POS消费
     */
@ListItemType(instantiate =ConsumeBean.class )
    private List<ConsumeBean> list;

    public List<ConsumeBean> getList() {
        return list;
    }

    public void setList(List<ConsumeBean> list) {
        this.list = list;
    }

    /**
     * 一笔消费的对象
     */
    public static class ConsumeBean implements Parcelable{
        private int sequence;
        private Object transType;
        private int transId;
        private String transDate;
        private String transCode;
        private String acctNum;
        private String mainAcctId;
        private String clearingCurrency;
        private BigDecimal clearingAmount;
        private String tranCurrency;
        private BigDecimal tranAmount;
        private String acctNumTail;
        private String transDesc;

        public ConsumeBean() {
        }

        protected ConsumeBean(Parcel in) {
            sequence = in.readInt();
            transId = in.readInt();
            transDate = in.readString();
            transCode = in.readString();
            acctNum = in.readString();
            mainAcctId = in.readString();
            clearingCurrency = in.readString();
            tranCurrency = in.readString();
            acctNumTail = in.readString();
            transDesc = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(sequence);
            dest.writeInt(transId);
            dest.writeString(transDate);
            dest.writeString(transCode);
            dest.writeString(acctNum);
            dest.writeString(mainAcctId);
            dest.writeString(clearingCurrency);
            dest.writeString(tranCurrency);
            dest.writeString(acctNumTail);
            dest.writeString(transDesc);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ConsumeBean> CREATOR = new Creator<ConsumeBean>() {
            @Override
            public ConsumeBean createFromParcel(Parcel in) {
                return new ConsumeBean(in);
            }

            @Override
            public ConsumeBean[] newArray(int size) {
                return new ConsumeBean[size];
            }
        };

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public Object getTransType() {
            return transType;
        }

        public void setTransType(Object transType) {
            this.transType = transType;
        }

        public int getTransId() {
            return transId;
        }

        public void setTransId(int transId) {
            this.transId = transId;
        }

        public String getTransDate() {
            return transDate;
        }

        public void setTransDate(String transDate) {
            this.transDate = transDate;
        }

        public String getTransCode() {
            return transCode;
        }

        public void setTransCode(String transCode) {
            this.transCode = transCode;
        }

        public String getAcctNum() {
            return acctNum;
        }

        public void setAcctNum(String acctNum) {
            this.acctNum = acctNum;
        }

        public String getMainAcctId() {
            return mainAcctId;
        }

        public void setMainAcctId(String mainAcctId) {
            this.mainAcctId = mainAcctId;
        }

        public String getClearingCurrency() {
            return clearingCurrency;
        }

        public void setClearingCurrency(String clearingCurrency) {
            this.clearingCurrency = clearingCurrency;
        }

        public BigDecimal getClearingAmount() {
            return clearingAmount;
        }

        public void setClearingAmount(BigDecimal clearingAmount) {
            this.clearingAmount = clearingAmount;
        }

        public String getTranCurrency() {
            return tranCurrency;
        }

        public void setTranCurrency(String tranCurrency) {
            this.tranCurrency = tranCurrency;
        }

        public BigDecimal getTranAmount() {
            return tranAmount;
        }

        public void setTranAmount(BigDecimal tranAmount) {
            this.tranAmount = tranAmount;
        }

        public String getAcctNumTail() {
            return acctNumTail;
        }

        public void setAcctNumTail(String acctNumTail) {
            this.acctNumTail = acctNumTail;
        }

        public String getTransDesc() {
            return transDesc;
        }

        public void setTransDesc(String transDesc) {
            this.transDesc = transDesc;
        }
    }
}




