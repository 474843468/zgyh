package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 手机号转账在途交易查询响应
 * <p/>
 * Created by liuweidong on 2016/6/22.
 */
public class PsnMobileTransferQueryUnSubmitTransResult {
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
        private LocalDate handleDate;
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

        public LocalDate getHandleDate() {
            return handleDate;
        }

        public void setHandleDate(LocalDate handleDate) {
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
