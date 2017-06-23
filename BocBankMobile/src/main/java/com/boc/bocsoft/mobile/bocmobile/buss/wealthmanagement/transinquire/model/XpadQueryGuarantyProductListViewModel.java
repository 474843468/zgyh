package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zc7067 on 2016/9/23.
 * Contact：中银理财-组合购买-历史交易列表
 */
public class XpadQueryGuarantyProductListViewModel {

    // 账号缓存标识
    private String accountKey;
    // 起始日期（yyyy/mm/dd）
    private String startDate;
    // 结束日期（yyyy/mm/dd）
    private String endDate;
    // 000：全部、001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元
    private String xpadProductCurCode;
    // 页面大小（数字必输）
    private String pageSize;
    // 当前页索引（数字必输）
    private String currentIndex;

    // true：重新查询结果(在交易改变查询条件时是需要重新查询的, currentIndex需上送0) false:不需要重新查询，使用缓存中的结果
    private String _refresh;
    private String accountType;

    private String conversationId;

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId(String conversationId) {
        return conversationId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType(String accountType) {
        return accountType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getXpadProductCurCode() {
        return xpadProductCurCode;
    }

    public void setXpadProductCurCode(String xpadProductCurCode) {
        this.xpadProductCurCode = xpadProductCurCode;
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

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    // 总笔数

    private int recordNumber;
    private List<QueryGuarantyProductListEntity> list;

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<QueryGuarantyProductListEntity> list) {
        this.list = list;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<QueryGuarantyProductListEntity> getList() {
        return list;
    }

    public static class QueryGuarantyProductListEntity implements Parcelable, Comparable<QueryGuarantyProductListEntity> {
        /**
         * tranSeq: "5409865425",
         * accountNo: "3242******7776",
         * accountKey: "59843708",
         * prodCode: "DE094353453",
         * prodName: "中银力度理财",
         * currency: "014",
         * cashRemit: "02",
         * buyAmt: 5600,
         * trfPrice: 56,
         * amount: 5000,
         * cancelPrice: 4.12,
         * status: 0,
         * returnDate: "2015/10/10",
         * cancelDate: "2015/10/10",
         * channel: "02"
         */
        // 组合交易流水号
        private String tranSeq;
        //银行账号
        private String accountNo;
        //账号缓存标识
        private String accountKey;
        // 产品代码
        private String prodCode;
        // 产品名称
        private String prodName;
        // 币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        private String currency;
        // 钞汇标识（1：钞 2：汇 0：人民币）
        private String cashRemit;
        // 购买金额
        private String buyAmt;
        // 购买价格
        private String trfPrice;
        //成交金额
        private String amount;
        //解质价格
        private String cancelPrice;
        // 状态（0：正常 1：解除）
        private String status;
        // 交易日期（即质押日期 yyyy/mm/dd）
        private LocalDate returnDate;
        // 解质日期（ yyyy/mm/dd）
        private LocalDate cancelDate;
        // 渠道（交易渠道 0：理财系统交易 1：（核心系统 OFP）柜面 2：网银 3：电话银行自助 4：电话银行人工 5:手机银行 6:家居银行 7:微信银行 8:自助终端 9：OCRM）
        private String channel;
        //产品类型
        private String productKind;


        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getAccountNo(String accountNo) {
            return accountNo;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        public String getAccountKey(String accountKey) {
            return accountKey;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public BigDecimal getAmount(BigDecimal amount) {
            return amount;
        }

        public void setTrfPrice(String trfPrice) {
            this.trfPrice = trfPrice;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public void setCancelPrice(String cancelPrice) {
            this.cancelPrice = cancelPrice;
        }

        public String getCancelPrice(String cancelPrice) {
            return cancelPrice;
        }

        public void setProductKind(String productKind) {
            this.productKind = productKind;
        }

        public String getProductKind() {
            return productKind;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCancelDate(LocalDate cancelDate) {
            this.cancelDate = cancelDate;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }

        public void setBuyAmt(String buyAmt) {
            this.buyAmt = buyAmt;
        }

        public void setTranSeq(String tranSeq) {
            this.tranSeq = tranSeq;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getTrfPrice() {
            return trfPrice;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public String getProdName() {
            return prodName;
        }


        public String getStatus() {
            return status;
        }

        public LocalDate getCancelDate() {
            return cancelDate;
        }

        public String getProdCode() {
            return prodCode;
        }

        public String getChannel() {
            return channel;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public String getBuyAmt() {
            return buyAmt;
        }

        public String getTranSeq() {
            return tranSeq;
        }

        public String getCurrency() {
            return currency;
        }

        public QueryGuarantyProductListEntity() {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.tranSeq);
            dest.writeString(this.accountNo);
            dest.writeString(this.accountKey);
            dest.writeString(this.prodCode);
            dest.writeString(this.prodName);
            dest.writeString(this.currency);
            dest.writeString(this.cashRemit);
            dest.writeSerializable(this.buyAmt);
            dest.writeSerializable(this.trfPrice);
            dest.writeSerializable(this.amount);
            dest.writeSerializable(this.cancelPrice);
            dest.writeString(this.status);
            dest.writeSerializable(this.returnDate);
            dest.writeSerializable(this.cancelDate);
            dest.writeString(this.channel);
            dest.writeString(this.productKind);
        }

        private QueryGuarantyProductListEntity(Parcel in) {
            this.tranSeq = in.readString();
            this.accountNo = in.readString();
            this.accountKey = in.readString();
            this.prodCode = in.readString();
            this.prodName = in.readString();
            this.currency = in.readString();
            this.cashRemit = in.readString();
            this.buyAmt = in.readString();
            this.trfPrice = in.readString();
            this.amount = in.readString();
            this.cancelPrice = in.readString();
            this.status = in.readString();
            this.returnDate = (LocalDate) in.readSerializable();
            this.cancelDate = (LocalDate) in.readSerializable();
            this.channel = in.readString();
            this.productKind = in.readString();
        }

        public static final Creator<QueryGuarantyProductListEntity> CREATOR = new Creator<QueryGuarantyProductListEntity>() {
            public QueryGuarantyProductListEntity createFromParcel(Parcel source) {
                return new QueryGuarantyProductListEntity(source);
            }

            public QueryGuarantyProductListEntity[] newArray(int size) {
                return new QueryGuarantyProductListEntity[size];
            }
        };

        @Override
        public int compareTo(QueryGuarantyProductListEntity another) {
            LocalDate thisDate = this.getReturnDate();
            LocalDate anotherDate = another.getReturnDate();

            int cmp = thisDate.getYear() - anotherDate.getYear();
            if (cmp == 0) {
                cmp = thisDate.getMonthValue() - anotherDate.getMonthValue();
                if (cmp == 0) {
                    cmp = thisDate.getDayOfMonth() - anotherDate.getDayOfMonth();
                }
            }

            return -cmp;
        }
    }
}
