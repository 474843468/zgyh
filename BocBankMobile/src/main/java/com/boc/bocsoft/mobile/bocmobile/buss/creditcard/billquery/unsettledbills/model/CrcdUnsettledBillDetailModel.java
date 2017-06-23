package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.util.List;

/**
 *   Created by xwg on "16/12/26". on "10:18".
 *   交易详情
 */
public class CrcdUnsettledBillDetailModel {
    private String recordNumber;
    @ListItemType(instantiate = CrcdTransactionListBean.class)
    private List<CrcdTransactionListBean> crcdTransactionList;

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<CrcdTransactionListBean> getCrcdTransactionList() {
        return crcdTransactionList;
    }

    public void setCrcdTransactionList(List<CrcdTransactionListBean> crcdTransactionList) {
        this.crcdTransactionList = crcdTransactionList;
    }

    public CrcdUnsettledBillDetailModel() {
    }

    public static class CrcdTransactionListBean implements Parcelable{
        private String transDate;// 交易日期
        private String bookCurrency;// 记账币种
        private String bookDate;// 记账日期
        private String bookAmount;// 记账金额
        private String debitCreditFlag;// 借贷标识
        private String cardNumberTail;// 卡号
        private String tranCurrency;// 交易币种
        private String tranAmount;// 交易金额
        private String remark;// 摘要
        private String transCode;// 借方合计

        public CrcdTransactionListBean() {
        }

        protected CrcdTransactionListBean(Parcel in) {
            transDate = in.readString();
            bookCurrency = in.readString();
            bookDate = in.readString();
            bookAmount = in.readString();
            debitCreditFlag = in.readString();
            cardNumberTail = in.readString();
            tranCurrency = in.readString();
            tranAmount = in.readString();
            remark = in.readString();
            transCode = in.readString();
        }

        public static final Creator<CrcdTransactionListBean> CREATOR = new Creator<CrcdTransactionListBean>() {
            @Override
            public CrcdTransactionListBean createFromParcel(Parcel in) {
                return new CrcdTransactionListBean(in);
            }

            @Override
            public CrcdTransactionListBean[] newArray(int size) {
                return new CrcdTransactionListBean[size];
            }
        };

        public String getTransDate() {
            return transDate;
        }

        public void setTransDate(String transDate) {
            this.transDate = transDate;
        }

        public String getBookCurrency() {
            return bookCurrency;
        }

        public void setBookCurrency(String bookCurrency) {
            this.bookCurrency = bookCurrency;
        }

        public String getBookDate() {
            return bookDate;
        }

        public void setBookDate(String bookDate) {
            this.bookDate = bookDate;
        }

        public String getBookAmount() {
            return bookAmount;
        }

        public void setBookAmount(String bookAmount) {
            this.bookAmount = bookAmount;
        }

        public String getDebitCreditFlag() {
            return debitCreditFlag;
        }

        public void setDebitCreditFlag(String debitCreditFlag) {
            this.debitCreditFlag = debitCreditFlag;
        }

        public String getCardNumberTail() {
            return cardNumberTail;
        }

        public void setCardNumberTail(String cardNumberTail) {
            this.cardNumberTail = cardNumberTail;
        }

        public String getTranCurrency() {
            return tranCurrency;
        }

        public void setTranCurrency(String tranCurrency) {
            this.tranCurrency = tranCurrency;
        }

        public String getTranAmount() {
            return tranAmount;
        }

        public void setTranAmount(String tranAmount) {
            this.tranAmount = tranAmount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTransCode() {
            return transCode;
        }

        public void setTransCode(String transCode) {
            this.transCode = transCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(transDate);
            dest.writeString(bookCurrency);
            dest.writeString(bookDate);
            dest.writeString(bookAmount);
            dest.writeString(debitCreditFlag);
            dest.writeString(cardNumberTail);
            dest.writeString(tranCurrency);
            dest.writeString(tranAmount);
            dest.writeString(remark);
            dest.writeString(transCode);
        }
    }
}
