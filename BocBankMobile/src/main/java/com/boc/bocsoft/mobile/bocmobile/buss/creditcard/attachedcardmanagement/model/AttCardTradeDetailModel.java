package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: liukai
 * Time：2016/12/10 13:36.
 * Created by lk7066 on 2016/12/10.
 * It's used to 只存了交易明细的所有信息
 */

public class AttCardTradeDetailModel implements Parcelable{

    /**
     "params": {
     "accountId": "4693801147362722",
     "startDate": "2022/01/28",
     "endDate": "2022/02/28",
     "pageSize": "10",
     "currentIndex": "1",
     "_refresh": "true",
     "conversationId": "a34ae0c2-9a1a-4d45-aaa8-718f66cc33ee"
     }
     */

    /*---------上送数据---------*/

    /**
     * 附属卡卡号
     * */
    private String subCrcdNo;

    /**
     * 开始日期
     * */
    private String startData;

    /**
     * 结束日期
     * */
    private String endData;

    /**
     * 页面大小
     * */
    private String pageSize;

    /**
     * 索引
     * */
    private String currentIndex;

    /**
     * 重新查询标识
     * */
    private String _refresh;

    /*----------返回数据-----------*/

    /**
     * 卡号
     * */
    private String cardNumberTail;

    /**
     * 交易记录数字
     * */
    private int recordNumber;

    /**
     * 每条记录
     * */
    private List<ListBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable{

        /**
         * 跳转至详情页面需要传递这个集合
         * */

        /**
         * 交易描述
         * */
        private String remark;

        /**
         * 交易日期
         * */
        private String transDate;

        /**
         * 交易类型
         * */
        private String transCode;

        /**
         * 记账币种
         * */
        private String bookCurrency;

        /**
         * 借方，贷方
         * */
        private String debitCreditFlag;

        /**
         * 记账日期
         * */
        private String bookDate;

        /**
         * 交易币种
         * */
        private String tranCurrency;

        /**
         * 交易金额
         * */
        private String tranAmount;

        /**
         * 记账金额
         * */
        private String bookAmount;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getBookCurrency() {
            return bookCurrency;
        }

        public void setBookCurrency(String bookCurrency) {
            this.bookCurrency = bookCurrency;
        }

        public String getDebitCreditFlag() {
            return debitCreditFlag;
        }

        public void setDebitCreditFlag(String debitCreditFlag) {
            this.debitCreditFlag = debitCreditFlag;
        }

        public String getBookDate() {
            return bookDate;
        }

        public void setBookDate(String bookDate) {
            this.bookDate = bookDate;
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

        public String getBookAmount() {
            return bookAmount;
        }

        public void setBookAmount(String bookAmount) {
            this.bookAmount = bookAmount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.remark);
            dest.writeString(this.transDate);
            dest.writeString(this.transCode);
            dest.writeString(this.bookCurrency);
            dest.writeString(this.debitCreditFlag);
            dest.writeString(this.bookDate);
            dest.writeString(this.tranCurrency);
            dest.writeString(this.tranAmount);
            dest.writeString(this.bookAmount);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.remark = in.readString();
            this.transDate = in.readString();
            this.transCode = in.readString();
            this.bookCurrency = in.readString();
            this.debitCreditFlag = in.readString();
            this.bookDate = in.readString();
            this.tranCurrency = in.readString();
            this.tranAmount = in.readString();
            this.bookAmount = in.readString();
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

        @Override
        public String toString() {
            return "ListBean{" +
                    "remark='" + remark + '\'' +
                    ", transDate='" + transDate + '\'' +
                    ", transCode='" + transCode + '\'' +
                    ", bookCurrency='" + bookCurrency + '\'' +
                    ", debitCreditFlag='" + debitCreditFlag + '\'' +
                    ", bookDate='" + bookDate + '\'' +
                    ", tranCurrency='" + tranCurrency + '\'' +
                    ", tranAmount='" + tranAmount + '\'' +
                    ", bookAmount='" + bookAmount + '\'' +
                    '}';
        }
    }

    public String getSubCrcdNo() {
        return subCrcdNo;
    }

    public void setSubCrcdNo(String subCrcdNo) {
        this.subCrcdNo = subCrcdNo;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getCardNumberTail() {
        return cardNumberTail;
    }

    public void setCardNumberTail(String cardNumberTail) {
        this.cardNumberTail = cardNumberTail;
    }

    public AttCardTradeDetailModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subCrcdNo);
        dest.writeString(this.startData);
        dest.writeString(this.endData);
        dest.writeString(this.pageSize);
        dest.writeString(this.currentIndex);
        dest.writeString(this._refresh);
        dest.writeString(this.cardNumberTail);
        dest.writeInt(this.recordNumber);
        dest.writeList(this.list);
    }

    protected AttCardTradeDetailModel(Parcel in) {
        this.subCrcdNo = in.readString();
        this.startData = in.readString();
        this.endData = in.readString();
        this.pageSize = in.readString();
        this.currentIndex = in.readString();
        this._refresh = in.readString();
        this.cardNumberTail = in.readString();
        this.recordNumber = in.readInt();
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Creator<AttCardTradeDetailModel> CREATOR = new Creator<AttCardTradeDetailModel>() {
        @Override
        public AttCardTradeDetailModel createFromParcel(Parcel source) {
            return new AttCardTradeDetailModel(source);
        }

        @Override
        public AttCardTradeDetailModel[] newArray(int size) {
            return new AttCardTradeDetailModel[size];
        }
    };

    @Override
    public String toString() {
        return "AttCardTradeDetailModel{" +
                "subCrcdNo='" + subCrcdNo + '\'' +
                ", startData='" + startData + '\'' +
                ", endData='" + endData + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", currentIndex='" + currentIndex + '\'' +
                ", _refresh='" + _refresh + '\'' +
                ", cardNumberTail='" + cardNumberTail + '\'' +
                ", recordNumber=" + recordNumber +
                ", list=" + list +
                '}';
    }
}
