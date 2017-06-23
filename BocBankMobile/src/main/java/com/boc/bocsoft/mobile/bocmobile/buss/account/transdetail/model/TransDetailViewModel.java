package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 交易明细View层数据模型
 * Created by wangf on 2016/6/8.
 */
public class TransDetailViewModel {

    /**
     * 查询交易明细上送数据项
     * <p>
     * accountId : 101020305  账户标识
     * currency : 币种
     * cashRemit : 钞汇
     * startDate : 2019/01/26 起始日期
     * endDate : 2019/02/01 结束日期
     * currentIndex : 0 当前页索引
     * _refresh : true 是否重新查询结果
     * pageSize : 10 页面大小
     */

    // 账户标识
    private String accountId;
    // 币种
    // 001 = 人民币
    // 014 = 美元
    // 027 = 日元
    // 038 = 欧元
    private String currency;
    // 钞汇
    // 01 = 现钞
    // 02 = 现汇
    private String cashRemit;
    // 起始日期
    private String startDate;
    // 结束日期
    private String endDate;
    // 当前页索引
    //_refresh = true 时必须送0
    private int currentIndex;
    // 是否重新查询结果
    // true：重新查询
    // false：不重新查询
    // 当前查询条件改变时需要重新查询
    private String _refresh;
    // 页面大小
    private int pageSize;


    /**
     * 查询交易明细返回数据项
     */

    //总记录数
    private int recordNumber;
    //交易列表
    private List<ListBean> List;


    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }


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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
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


    public static class ListBean implements Parcelable {

        /**
         * 冲正标识
         */
        private boolean chargeBack;
        /**
         * 交易日期
         */
        private LocalDate paymentDate;
        /**
         * 交易币种
         * 001 = 人民币
         * 014 = 美元
         * 027 = 日元
         * 038 = 欧元
         */
        private String currency;
        /**
         * 钞汇
         * 01 = 现钞
         * 02 = 现汇
         */
        private String cashRemit;
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
         * 交易渠道
         * 后台直接返回汉字
         */
        private String transChnl;
        /**
         * 网店或终端信息
         * 后台直接返回汉字
         */
        private String chnlDetail;


        public boolean isChargeBack() {
            return chargeBack;
        }

        public void setChargeBack(boolean chargeBack) {
            this.chargeBack = chargeBack;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
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
            dest.writeByte(this.chargeBack ? (byte) 1 : (byte) 0);
            dest.writeSerializable(this.paymentDate);
            dest.writeString(this.currency);
            dest.writeString(this.cashRemit);
            dest.writeString(this.amount);
            dest.writeString(this.balance);
            dest.writeString(this.businessDigest);
            dest.writeString(this.furInfo);
            dest.writeString(this.payeeAccountName);
            dest.writeString(this.payeeAccountNumber);
            dest.writeString(this.transChnl);
            dest.writeString(this.chnlDetail);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.chargeBack = in.readByte() != 0;
            this.paymentDate = (LocalDate) in.readSerializable();
            this.currency = in.readString();
            this.cashRemit = in.readString();
            this.amount = in.readString();
            this.balance = in.readString();
            this.businessDigest = in.readString();
            this.furInfo = in.readString();
            this.payeeAccountName = in.readString();
            this.payeeAccountNumber = in.readString();
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
