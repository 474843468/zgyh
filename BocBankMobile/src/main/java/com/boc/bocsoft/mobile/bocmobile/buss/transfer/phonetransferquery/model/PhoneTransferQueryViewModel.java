package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.model;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 手机号转账记录查询
 *
 * Created by liuweidong on 2016/6/23.
 */
public class PhoneTransferQueryViewModel {

    // 请求
    /**
     * 服务码:PB035
     */
    private String serviceId;
    /**
     * 开始日期 格式：yyyy/mm/dd
     */
    private LocalDate beiginDate;
    /**
     * 结束日期 格式：yyyy/mm/dd
     */
    private LocalDate endDate;
    /**
     * 状态
     全部-送空
     01-提交成功
     05-已过期
     A-交易成功
     03B-交易失败，代表查询03和B状态的记录
     */
    private String status;
    /**
     * 当前页起始索引
     */
    private int currentIndex;
    /**
     * 页面显示记录条数
     */
    private int pageSize;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getBeiginDate() {
        return beiginDate;
    }

    public void setBeiginDate(LocalDate beiginDate) {
        this.beiginDate = beiginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    // 响应

    private List<ListBean> list;
    /**
     * 总记录数
     */
    private int recordNumber;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public static class ListBean {
        /**
         * 指令流水号
         */
        private Long transactionId;
        /**
         * 初次提交日期
         */
        private String firstSubmitDate;
        /**
         * 处理日期
         */
        private String handleDate;
        /**
         * 转出账户
         */
        private String payerAccountNumber;
        /**
         * 收款人姓名
         */
        private String payeeName;
        /**
         * 收款人手机号
         */
        private String payeeMobile;
        /**
         * 收款人账户
         */
        private String payeeAccountNumber;
        /**
         * 转账金额
         */
        private String amount;
        /**
         * 币种
         */
        private String currency;
        /**
         * 备注
         */
        private String furInfo;
        /**
         * 失败原因
         */
        private String failReason;
        /**
         * 状态
         01-提交成功
         02-已回复
         03-交易失败
         05-已过期
         A-交易成功
         B-交易失败
         */
        private String status;

        public Long getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(Long transactionId) {
            this.transactionId = transactionId;
        }

        public String getFirstSubmitDate() {
            return firstSubmitDate;
        }

        public void setFirstSubmitDate(String firstSubmitDate) {
            this.firstSubmitDate = firstSubmitDate;
        }

        public String getHandleDate() {
            return handleDate;
        }

        public void setHandleDate(String handleDate) {
            this.handleDate = handleDate;
        }

        public String getPayerAccountNumber() {
            return payerAccountNumber;
        }

        public void setPayerAccountNumber(String payerAccountNumber) {
            this.payerAccountNumber = payerAccountNumber;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public String getPayeeMobile() {
            return payeeMobile;
        }

        public void setPayeeMobile(String payeeMobile) {
            this.payeeMobile = payeeMobile;
        }

        public String getPayeeAccountNumber() {
            return payeeAccountNumber;
        }

        public void setPayeeAccountNumber(String payeeAccountNumber) {
            this.payeeAccountNumber = payeeAccountNumber;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getFurInfo() {
            return furInfo;
        }

        public void setFurInfo(String furInfo) {
            this.furInfo = furInfo;
        }

        public String getFailReason() {
            return failReason;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
