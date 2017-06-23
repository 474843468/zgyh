package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * Contact：中银理财-组合购买
 * Created by zhx on 2016/9/19
 */
public class XpadAutComTradStatusViewModel {
    // 账号缓存标识
    private String accountKey;
    // 起始日期（yyyy/mm/dd）
    private String startDate;
    // 结束日期（yyyy/mm/dd）
    private String endDate;

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

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    // 总笔数
    private int recordNumber;
    private List<AutComTradEntity> list;

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<AutComTradEntity> list) {
        this.list = list;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<AutComTradEntity> getList() {
        return list;
    }

    public static class AutComTradEntity implements Parcelable , Comparable<AutComTradEntity>{
        /**
         * trfPrice : 12.3
         * cashRemit : 1
         * prodName : 吃货01
         * canBeCanceled : 0
         * status : 1
         * cancelDate : 2012/05/20
         * prodCode : 9456
         * account : 5456587524359872
         * channel : 2
         * returnDate : 2012/05/20
         * buyAmt : 900
         * tranSeq : 4354354311115
         * currency : 014
         */
        // 组合交易流水号
        private String tranSeq;
        // 产品代码
        private String prodCode;
        // 产品名称
        private String prodName;
        // 币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        private String currency;
        // 钞汇标识（01：钞 02：汇 00：人民币）
        private String cashRemit;
        // 购买金额
        private String buyAmt;
        // 购买价格
        private String trfPrice;
        // 状态（0：正常 1：解除）
        private String status;
        // 交易日期（即质押日期 yyyy/mm/dd）
        private LocalDate returnDate;
        // 渠道（交易渠道 0：理财系统交易 1：（核心系统OFP）柜面 2：网银 3：电话银行自助 4：电话银行人工 5:手机银行 6:家居银行 7:微信银行 8:自助终端 9：OCRM）
        private String channel;
        // 是否可解除（0：是 1：否 (状态已为解除的返回‘否’)）
        private String canBeCanceled;
        // 截止日期
        private LocalDate cancelDate;
        private String account;
        private String productKind;

        public void setTrfPrice(String trfPrice) {
            this.trfPrice = trfPrice;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public void setCanBeCanceled(String canBeCanceled) {
            this.canBeCanceled = canBeCanceled;
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

        public void setAccount(String account) {
            this.account = account;
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

        public String getCanBeCanceled() {
            return canBeCanceled;
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

        public String getAccount() {
            return account;
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

        @Override
        public int describeContents() {
            return 0;
        }

        public String getProductKind() {
            return productKind;
        }

        public void setProductKind(String productKind) {
            this.productKind = productKind;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.tranSeq);
            dest.writeString(this.prodCode);
            dest.writeString(this.prodName);
            dest.writeString(this.currency);
            dest.writeString(this.cashRemit);
            dest.writeString(this.buyAmt);
            dest.writeString(this.trfPrice);
            dest.writeString(this.status);
            dest.writeSerializable(this.returnDate);
            dest.writeString(this.channel);
            dest.writeString(this.canBeCanceled);
            dest.writeSerializable(this.cancelDate);
            dest.writeString(this.account);
            dest.writeString(this.productKind);
        }

        public AutComTradEntity() {
        }

        private AutComTradEntity(Parcel in) {
            this.tranSeq = in.readString();
            this.prodCode = in.readString();
            this.prodName = in.readString();
            this.currency = in.readString();
            this.cashRemit = in.readString();
            this.buyAmt = in.readString();
            this.trfPrice = in.readString();
            this.status = in.readString();
            this.returnDate = (LocalDate) in.readSerializable();
            this.channel = in.readString();
            this.canBeCanceled = in.readString();
            this.cancelDate = (LocalDate) in.readSerializable();
            this.account = in.readString();
            this.productKind = in.readString();
        }

        public static final Parcelable.Creator<AutComTradEntity> CREATOR = new Parcelable.Creator<AutComTradEntity>() {
            public AutComTradEntity createFromParcel(Parcel source) {
                return new AutComTradEntity(source);
            }

            public AutComTradEntity[] newArray(int size) {
                return new AutComTradEntity[size];
            }
        };

        @Override
        public int compareTo(AutComTradEntity another) {
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
