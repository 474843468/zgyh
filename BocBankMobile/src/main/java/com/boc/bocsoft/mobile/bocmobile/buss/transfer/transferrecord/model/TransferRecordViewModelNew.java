package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 转账记录View层数据模型 - 新接口
 * Created by wangf on 2016/8/9
 */
public class TransferRecordViewModelNew {


    /**
     * 查询转账记录上送数据项
     */

    private String transType;
    private String startDate;
    private String endDate;
    private String startAmount;
    private String endAmount;
    private String payeeAccountName;
    private String status;
    private int currentIndex;
    private int pageSize;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
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

    public String getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(String startAmount) {
        this.startAmount = startAmount;
    }

    public String getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(String endAmount) {
        this.endAmount = endAmount;
    }

    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
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


    /**
     * 查询转账记录返回数据项
     */

    private int recordnumber;

    private List<ListBean> list;

    public int getRecordnumber() {
        return recordnumber;
    }

    public void setRecordnumber(int recordnumber) {
        this.recordnumber = recordnumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String batSeq;//转账批次号
        private String status;//转账状态
        private LocalDate paymentDate;//转账时间
        private String payerAccountNumber;//转出账户
        private String payeeAccountName;//收款人姓名
        private String payeeAccountNumber;//转入账户
        private String feeCur;//币种
        private String amount;//转账金额
        private String channel;//交易渠道
        private String transactionId;//交易流水号
        private String transferType;//转账类型
        private String reexchangeStatus;//退汇交易状态

        public String getBatSeq() {
            return batSeq;
        }

        public void setBatSeq(String batSeq) {
            this.batSeq = batSeq;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getPayerAccountNumber() {
            return payerAccountNumber;
        }

        public void setPayerAccountNumber(String payerAccountNumber) {
            this.payerAccountNumber = payerAccountNumber;
        }

        public String getPayeeAccountName() {
            return payeeAccountName;
        }

        public void setPayeeAccountName(String payeeAccountName) {
            this.payeeAccountName = payeeAccountName;
        }

        public String getPayeeAccountNumber() {
            return payeeAccountNumber;
        }

        public void setPayeeAccountNumber(String payeeAccountNumber) {
            this.payeeAccountNumber = payeeAccountNumber;
        }

        public String getFeeCur() {
            return feeCur;
        }

        public void setFeeCur(String feeCur) {
            this.feeCur = feeCur;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getTransferType() {
            return transferType;
        }

        public void setTransferType(String transferType) {
            this.transferType = transferType;
        }

        public String getReexchangeStatus() {
            return reexchangeStatus;
        }

        public void setReexchangeStatus(String reexchangeStatus) {
            this.reexchangeStatus = reexchangeStatus;
        }
    }

}
