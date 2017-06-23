package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by wangtong on 2016/6/28.
 */
public class PsnPaymentOrderListResult {

    private int recordNum;

    private List<PaymentRecordListBean> paymentRecordList;

    public int getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(int recordNum) {
        this.recordNum = recordNum;
    }

    public List<PaymentRecordListBean> getPaymentRecordList() {
        return paymentRecordList;
    }

    public void setPaymentRecordList(List<PaymentRecordListBean> paymentRecordList) {
        this.paymentRecordList = paymentRecordList;
    }

    public static class PaymentRecordListBean {
        private LocalDateTime createDate;
        private String notifyId;
        private String payerName;
        private String payeeName;
        private String requestAmount;
        private String status;
        private String trfAmount;

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public LocalDateTime getCreateDate() {
            return createDate;
        }

        public void setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
        }

        public String getNotifyId() {
            return notifyId;
        }

        public void setNotifyId(String notifyId) {
            this.notifyId = notifyId;
        }

        public String getPayerName() {
            return payerName;
        }

        public void setPayerName(String payerName) {
            this.payerName = payerName;
        }

        public String getRequestAmount() {
            return requestAmount;
        }

        public void setRequestAmount(String requestAmount) {
            this.requestAmount = requestAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTrfAmount() {
            return trfAmount;
        }

        public void setTrfAmount(String trfAmount) {
            this.trfAmount = trfAmount;
        }
    }
}
