package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by wangtong on 2016/6/28.
 */
public class PsnReminderOrderListResult {

    private int recordNum;

    private List<ActiveReminderListBean> activeReminderList;

    public int getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(int recordNum) {
        this.recordNum = recordNum;
    }

    public List<ActiveReminderListBean> getActiveReminderList() {
        return activeReminderList;
    }

    public void setActiveReminderList(List<ActiveReminderListBean> activeReminderList) {
        this.activeReminderList = activeReminderList;
    }

    public static class ActiveReminderListBean {

        private LocalDateTime createDate;
        private String notifyId;
        private String payerName;
        private String requestAmount;
        private String status;
        private String trfAmount;


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
