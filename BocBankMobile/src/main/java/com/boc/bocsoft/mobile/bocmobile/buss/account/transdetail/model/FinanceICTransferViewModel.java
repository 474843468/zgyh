package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电子现金账户 交易明细
 * Created by wangf on 2016/7/16.
 */
public class FinanceICTransferViewModel {

    /**
     * 电子现金账户 交易明细 上送数据
     */

    /**
     * 账号Id
     */
    private String accountId;
    /**
     * 起始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 当前页索引
     */
    private int currentIndex;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     * 电子现金账户 交易明细 返回数据
     */


    /** 总记录数 */
    private int recordNumber;

    /** 交易明细列表 */
    private List<ListBean> listBeen;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getListBeen() {
        return listBeen;
    }

    public void setListBeen(List<ListBean> listBeen) {
        this.listBeen = listBeen;
    }

    public static class ListBean implements Parcelable {
        /**
         * 币种
         */
        private String currency;
        /**
         * 交易类型
         */
        private String transferType;
        /**
         * 余额
         */
        private BigDecimal balance;
        /**
         * 收入/支出总额
         */
        private BigDecimal amount;
        /**
         * 交易日期
         */
        private LocalDate returnDate;
        /**
         * 钞汇
         */
        private String cashRemit;
        /**
         * 冲正标识
         */
        private boolean chargeBack;
        /**
         * 转入/转出标识  true-转出 0,false-转入 1
         */
        private boolean amountFlag;

        public boolean isAmountFlag() {
            return amountFlag;
        }

        public void setAmountFlag(boolean amountFlag) {
            this.amountFlag = amountFlag;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getTransferType() {
            return transferType;
        }

        public void setTransferType(String transferType) {
            this.transferType = transferType;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public boolean isChargeBack() {
            return chargeBack;
        }

        public void setChargeBack(boolean chargeBack) {
            this.chargeBack = chargeBack;
        }

        public ListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.currency);
            dest.writeString(this.transferType);
            dest.writeSerializable(this.balance);
            dest.writeSerializable(this.amount);
            dest.writeSerializable(this.returnDate);
            dest.writeString(this.cashRemit);
            dest.writeByte(this.chargeBack ? (byte) 1 : (byte) 0);
            dest.writeByte(this.amountFlag ? (byte) 1 : (byte) 0);
        }

        protected ListBean(Parcel in) {
            this.currency = in.readString();
            this.transferType = in.readString();
            this.balance = (BigDecimal) in.readSerializable();
            this.amount = (BigDecimal) in.readSerializable();
            this.returnDate = (LocalDate) in.readSerializable();
            this.cashRemit = in.readString();
            this.chargeBack = in.readByte() != 0;
            this.amountFlag = in.readByte() != 0;
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

}
