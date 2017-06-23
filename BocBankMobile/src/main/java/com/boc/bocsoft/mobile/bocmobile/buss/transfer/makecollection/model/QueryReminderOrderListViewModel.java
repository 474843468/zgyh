package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * ViewModel:催款指令查询
 * Created by zhx on 2016/7/5
 */
public class QueryReminderOrderListViewModel {
    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 当前页
     */
    private int currentIndex;
    /**
     * 每页显示条数
     */
    private int pageSize;

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 记录个数
     */
    private int recordNum;
    /**
     * status : 1
     * createDate : 2011/02/23 05:01:48
     * payerName : 黑山老腰
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
         * 催款状态:1：未付、2：已付、3：已撤销、4：状态未明、5：过期未付
         */
        private String status;
        /**
         * 催款日期
         */
        private String createDate;
        /**
         * 付款人姓名
         */
        private String payerName;
        /**
         * 实付金额
         */
        private String trfAmount;
        /**
         * 指令序号
         */
        private String notifyId;
        /**
         * 催款金额
         */
        private BigDecimal requestAmount;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getPayerName() {
            return payerName;
        }

        public void setPayerName(String payerName) {
            this.payerName = payerName;
        }

        public String getTrfAmount() {
            return trfAmount;
        }

        public void setTrfAmount(String trfAmount) {
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
