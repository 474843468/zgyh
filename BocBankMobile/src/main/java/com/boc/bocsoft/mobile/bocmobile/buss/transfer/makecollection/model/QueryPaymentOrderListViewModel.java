package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.List;

/**
 * ViewModel:付款指令查询
 * Created by zhx on 2016/7/6
 */
public class QueryPaymentOrderListViewModel {
    /**
     * 开始日期
     */
    private LocalDateTime startDate;
    /**
     * 结束日期
     */
    private LocalDateTime endDate;
    /**
     * 当前页
     */
    private int currentIndex;
    /**
     * 每页显示条数
     */
    private int pageSize;

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    private int recordNum;
    /**
     * status : 1
     * createDate : 2011/02/23 05:01:48
     * payeeName : 黑山老腰
     * trfAmount : null
     * notifyId : 3201
     * requestAmount : 300
     */

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
        /**
         * 交易状态
         */
        private String status;
        /**
         * 催款日期
         */
        private LocalDateTime createDate;
        /**
         * 收款人姓名
         */
        private String payeeName;
        /**
         * 实付金额
         */
        private BigDecimal trfAmount;
        /**
         * 指令序号
         */
        private String notifyId;
        /**
         * 付款金额
         */
        private BigDecimal requestAmount;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getCreateDate() {
            return createDate;
        }

        public void setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public Object getTrfAmount() {
            return trfAmount;
        }

        public void setTrfAmount(BigDecimal trfAmount) {
            this.trfAmount = trfAmount;
        }

        public String getNotifyId() {
            return notifyId;
        }

        public void setNotifyId(String notifyId) {
            this.notifyId = notifyId;
        }

        public BigDecimal getRequestAmount() {
            return requestAmount;
        }

        public void setRequestAmount(BigDecimal requestAmount) {
            this.requestAmount = requestAmount;
        }
    }
}
