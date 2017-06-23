package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 * Created by wzn7074 on 2016/12/14.
 */
public class TradeQueryListModel {

    private String conversationId; //会话

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
//请求字段

    /* *
            "startDate": "2021/10/25",
                    "endDate": "2021/10/31",
                    "fessFlag": "00",
                    "currentIndex": "0",
                    "pageSize": "15",
                    "_refresh": "true"
      */

    private String startDate; //开始时间
    private String endDate; //结束时间
    private String fessFlag;//结售汇业务类型
    private String currentIndex;//当前页索引
    private String pageSize;//页面大小
    private String _refresh;//刷新标志

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

    public String getFessFlag() {
        return fessFlag;
    }

    public void setFessFlag(String fessFlag) {
        this.fessFlag = fessFlag;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }


    //下面对应响应字段

    //记录总数
    private int recordNumber;
    private List<TradeQueryResultEntity> list;


    public int getRecordNumber() {
        return recordNumber;
    }
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public List<TradeQueryResultEntity> getList() {return list;}
    public void setList(List<TradeQueryResultEntity> List) {
        this.list = List ;
    }

    public static class TradeQueryResultEntity implements Parcelable {
        /**
         * amount : 100.00
         * channel : 01
         * trsChannelNum : 131152040114878
         * accountNumber : 1028****2505
         * cashRemit : 02
         * refNum : G1100000001012016121800000005
         * transType : 11
         * fundTypeCode : 3223
         * handleStatus : 9
         * bankSelfNum : 00095103
         * paymentDate : 2021/10/31
         * paymentTime : 15:20:41
         * tranRetCode : 0000000
         * currencyCode : 021
         * status : 00
         */
        private String amount;
        private String channel;
        private String trsChannelNum;
        private String accountNumber;
        private String cashRemit;
        private String refNum;
        private String transType;
        private String fundTypeCode;
        private String handleStatus;
        private String bankSelfNum;
        private String paymentDate;
        private String paymentTime;
        private String tranRetCode;
        private String currencyCode;
        private String status;

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setTrsChannelNum(String trsChannelNum) {
            this.trsChannelNum = trsChannelNum;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public void setRefNum(String refNum) {
            this.refNum = refNum;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public void setFundTypeCode(String fundTypeCode) {
            this.fundTypeCode = fundTypeCode;
        }

        public void setHandleStatus(String handleStatus) {
            this.handleStatus = handleStatus;
        }

        public void setBankSelfNum(String bankSelfNum) {
            this.bankSelfNum = bankSelfNum;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public void setPaymentTime(String paymentTime) {
            this.paymentTime = paymentTime;
        }

        public void setTranRetCode(String tranRetCode) {
            this.tranRetCode = tranRetCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAmount() {
            return amount;
        }

        public String getChannel() {
            return channel;
        }

        public String getTrsChannelNum() {
            return trsChannelNum;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public String getRefNum() {
            return refNum;
        }

        public String getTransType() {
            return transType;
        }

        public String getFundTypeCode() {
            return fundTypeCode;
        }

        public String getHandleStatus() {
            return handleStatus;
        }

        public String getBankSelfNum() {
            return bankSelfNum;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public String getPaymentTime() {
            return paymentTime;
        }

        public String getTranRetCode() {
            return tranRetCode;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getStatus() {
            return status;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.amount);
            dest.writeString(this.channel);
            dest.writeString(this.trsChannelNum);
            dest.writeString(this.accountNumber);
            dest.writeString(this.cashRemit);
            dest.writeString(this.refNum);
            dest.writeString(this.transType);
            dest.writeString(this.fundTypeCode);
            dest.writeString(this.handleStatus);
            dest.writeString(this.bankSelfNum);
            dest.writeString(this.paymentDate);
            dest.writeString(this.paymentTime);
            dest.writeString(this.tranRetCode);
            dest.writeString(this.currencyCode);
            dest.writeString(this.status);
        }

        public TradeQueryResultEntity() {
        }

        private TradeQueryResultEntity(Parcel in) {
            this.amount = in.readString();
            this.channel = in.readString();
            this.trsChannelNum = in.readString();
            this.accountNumber = in.readString();
            this.cashRemit = in.readString();
            this.refNum = in.readString();
            this.transType = in.readString();
            this.fundTypeCode = in.readString();
            this.handleStatus = in.readString();
            this.bankSelfNum = in.readString();
            this.paymentDate = in.readString();
            this.paymentTime = in.readString();
            this.tranRetCode = in.readString();
            this.currencyCode = in.readString();
            this.status = in.readString();
        }

        public static final Parcelable.Creator<TradeQueryResultEntity> CREATOR = new Parcelable.Creator<TradeQueryResultEntity>() {
            public TradeQueryResultEntity createFromParcel(Parcel source) {
                return new TradeQueryResultEntity(source);
            }

            public TradeQueryResultEntity[] newArray(int size) {
                return new TradeQueryResultEntity[size];
            }
        };
    }
}


