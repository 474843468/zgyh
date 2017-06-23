package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 医保账户交易明细查询 View层数据模型
 * Created by wangf on 2016/7/16.
 */
public class MedicalTransferDetailQueryViewModel {

    /**
     * 医保账户交易明细查询 上送数据项
     */

    /**
     * 账户标识
     */
    private String accountId;
    /**
     * 币种
     */
    private String currency;
    /**
     * 钞汇
     * 00=无钞汇标识
     * 01=现钞
     * 02=现汇
     */
    private String cashRemit;
    /**
     * 起始日期
     * yyyy/mm/dd
     */
    private String startDate;
    /**
     * 结束日期
     * yyyy/mm/dd
     */
    private String endDate;
    /**
     * 是否重新查询结果
     * true:重新查询
     * false:不重新查询
     * 当查询条件改变时需要重新查询
     */
    private String _refresh;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     *当前页索引
     * _refresh=true
     * 必须送0
     */
    private int currentIndex;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
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

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
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
     * 医保账户交易明细查询 返回数据项
     */

    /**
     * 总记录数
     */
    private int recordNumber;
    /**
     * amount : -0.08
     * balance : 909.55
     * businessDigest : 结利息税
     * cashRemit :
     * chargeBack : false
     * currency : 001
     * furInfo :
     * payeeAccountName :
     * payeeAccountNumber :
     * paymentDate : 2016/09/20
     */

    private java.util.List<ListBean> List;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public java.util.List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean implements Parcelable {

        /**
         * 收入或支出金额
         */
        private String amount;
        /**
         * 余额
         */
        private String balance;
        /**
         * 业务摘要
         */
        private String businessDigest;
        /**
         * 钞汇
         */
        private String cashRemit;
        /**
         * 冲正标识
         */
        private boolean chargeBack;
        /**
         * 交易货币
         */
        private String currency;
        /**
         * 附言
         */
        private String furInfo;
        /**
         * 对方账户名称
         */
        private String payeeAccountName;
        /**
         * 对方账户账号
         */
        private String payeeAccountNumber;
        /**
         * 交易日期
         */
        private LocalDate paymentDate;
        /**
         * 交易渠道
         */
        private String transChnl;
        /**
         * 网点或终端信息
         */
        private String chnlDetail;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getBusinessDigest() {
            return businessDigest;
        }

        public void setBusinessDigest(String businessDigest) {
            this.businessDigest = businessDigest;
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

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getFurInfo() {
            return furInfo;
        }

        public void setFurInfo(String furInfo) {
            this.furInfo = furInfo;
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

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getTransChnl() {
            return transChnl;
        }

        public void setTransChnl(String transChnl) {
            this.transChnl = transChnl;
        }

        public String getChnlDetail() {
            return chnlDetail;
        }

        public void setChnlDetail(String chnlDetail) {
            this.chnlDetail = chnlDetail;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.amount);
            dest.writeString(this.balance);
            dest.writeString(this.businessDigest);
            dest.writeString(this.cashRemit);
            dest.writeByte(this.chargeBack ? (byte) 1 : (byte) 0);
            dest.writeString(this.currency);
            dest.writeString(this.furInfo);
            dest.writeString(this.payeeAccountName);
            dest.writeString(this.payeeAccountNumber);
            dest.writeSerializable(this.paymentDate);
            dest.writeString(this.transChnl);
            dest.writeString(this.chnlDetail);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.amount = in.readString();
            this.balance = in.readString();
            this.businessDigest = in.readString();
            this.cashRemit = in.readString();
            this.chargeBack = in.readByte() != 0;
            this.currency = in.readString();
            this.furInfo = in.readString();
            this.payeeAccountName = in.readString();
            this.payeeAccountNumber = in.readString();
            this.paymentDate = (LocalDate) in.readSerializable();
            this.transChnl = in.readString();
            this.chnlDetail = in.readString();
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
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
