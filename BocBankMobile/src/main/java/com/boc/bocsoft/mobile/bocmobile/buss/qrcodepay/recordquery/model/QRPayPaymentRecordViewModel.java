package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by wangf on 2016/9/8.
 */
public class QRPayPaymentRecordViewModel {

    /**
     * 交易记录
     * 上送数据项
     */

    //交易类型
    private String type;
    //账户流水号
    private String actSeq;
    //查询起始日期
    private String startDate;
    //查询结束日期
    private String endDate;
    //是否重新查询结果
    private String _refresh;
    //页面大小
    private int pageSize;
    //当前页索引
    private int currentIndex;
    //交易状态  0:全部 1:成功 2:失败 3:银行处理中
    private String tranStatus;


    /**
     * 交易记录
     * 返回数据项
     */
    //记录条数
    private int recordNumber;


    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    private java.util.List<ListBean> List;

    public java.util.List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean implements Parcelable {

        //交易类型  01：支付 02：转账
        private String type;
        //转账类型
        private String transferType;
        //订单流水号
        private String tranSeq;
        //交易时间
        private LocalDateTime tranTime;
        //交易结果
        private String tranStatus;
        //交易金额
        private String tranAmount;
        //商户号
        private String merchantNo;
        //商户名称
        private String merchantName;
        //商户类型
        private String merchantCatNo;
        //交易说明 1：已退货 2：已撤销
        private String tranRemark;
        //付款方卡号 交易类型为02：转账时有意义
        private String payerAccNo;
        //付款方姓名 交易类型为02：转账时有意义
        private String payerName;
        //收款方附言 交易类型为02：转账时有意义
        private String payerComments;
        //付款凭证号 交易成功时有值
        private String voucherNum;
        //收款方卡号
        private String payeeAccNo;
        //收款方姓名
        private String payeeName;
        // 收款方附言
        private String payeeComments;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTransferType() {
            return transferType;
        }

        public void setTransferType(String transferType) {
            this.transferType = transferType;
        }

        public String getTranSeq() {
            return tranSeq;
        }

        public void setTranSeq(String tranSeq) {
            this.tranSeq = tranSeq;
        }

        public LocalDateTime getTranTime() {
            return tranTime;
        }

        public void setTranTime(LocalDateTime tranTime) {
            this.tranTime = tranTime;
        }

        public String getTranStatus() {
            return tranStatus;
        }

        public void setTranStatus(String tranStatus) {
            this.tranStatus = tranStatus;
        }

        public String getTranAmount() {
            return tranAmount;
        }

        public void setTranAmount(String tranAmount) {
            this.tranAmount = tranAmount;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getMerchantCatNo() {
            return merchantCatNo;
        }

        public void setMerchantCatNo(String merchantCatNo) {
            this.merchantCatNo = merchantCatNo;
        }

        public String getTranRemark() {
            return tranRemark;
        }

        public void setTranRemark(String tranRemark) {
            this.tranRemark = tranRemark;
        }

        public String getPayerAccNo() {
            return payerAccNo;
        }

        public void setPayerAccNo(String payerAccNo) {
            this.payerAccNo = payerAccNo;
        }

        public String getPayerName() {
            return payerName;
        }

        public void setPayerName(String payerName) {
            this.payerName = payerName;
        }

        public String getPayerComments() {
            return payerComments;
        }

        public void setPayerComments(String payerComments) {
            this.payerComments = payerComments;
        }

        public String getVoucherNum() {
            return voucherNum;
        }

        public void setVoucherNum(String voucherNum) {
            this.voucherNum = voucherNum;
        }

        public String getPayeeAccNo() {
            return payeeAccNo;
        }

        public void setPayeeAccNo(String payeeAccNo) {
            this.payeeAccNo = payeeAccNo;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public String getPayeeComments() {
            return payeeComments;
        }

        public void setPayeeComments(String payeeComments) {
            this.payeeComments = payeeComments;
        }

        public ListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.type);
            dest.writeString(this.transferType);
            dest.writeString(this.tranSeq);
            dest.writeSerializable(this.tranTime);
            dest.writeString(this.tranStatus);
            dest.writeString(this.tranAmount);
            dest.writeString(this.merchantNo);
            dest.writeString(this.merchantName);
            dest.writeString(this.merchantCatNo);
            dest.writeString(this.tranRemark);
            dest.writeString(this.payerAccNo);
            dest.writeString(this.payerName);
            dest.writeString(this.payerComments);
            dest.writeString(this.voucherNum);
            dest.writeString(this.payeeAccNo);
            dest.writeString(this.payeeName);
            dest.writeString(this.payeeComments);
        }

        protected ListBean(Parcel in) {
            this.type = in.readString();
            this.transferType = in.readString();
            this.tranSeq = in.readString();
            this.tranTime = (LocalDateTime) in.readSerializable();
            this.tranStatus = in.readString();
            this.tranAmount = in.readString();
            this.merchantNo = in.readString();
            this.merchantName = in.readString();
            this.merchantCatNo = in.readString();
            this.tranRemark = in.readString();
            this.payerAccNo = in.readString();
            this.payerName = in.readString();
            this.payerComments = in.readString();
            this.voucherNum = in.readString();
            this.payeeAccNo = in.readString();
            this.payeeName = in.readString();
            this.payeeComments = in.readString();
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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
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

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }
}
