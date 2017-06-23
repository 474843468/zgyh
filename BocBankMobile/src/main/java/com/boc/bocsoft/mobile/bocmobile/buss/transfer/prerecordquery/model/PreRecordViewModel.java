package com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.model;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 预约交易查询 View层数据模型
 * Created by wangf on 2016/7/22.
 */
public class PreRecordViewModel {

    /**
     * 预约交易查询 上送数据项
     */

    //是否重新查询
    private String _refresh;
    //日期查询类型
    private String dateType;
    //起始日期
    private String startDate;
    //结束日期
    private String endDate;
    //每页显示条数
    private int pageSize;
    //当前页
    private int currentIndex;

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    /**
     * 预约交易查询 返回数据项
     */

    //记录总数
    private int recordCount;

    private List<ListBean> list;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String amount;//金额
        private String batSeq;//转账批次号
        private String channel;//交易渠道
        private String currency;//币种
        private LocalDate firstSubmitDate;//预约日期
        private String payeeAccountName;//收款人姓名
        private String payeeAccountNumber;//转入账户
        private String payerAccountNumber;//转出账户
        private LocalDate paymentDate;//执行日期
        private String periodicalSeq;//周期预约交易
        private String status;//交易状态
        private String transMode;//预约类型
        private String transactionId;//网银交易序号

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBatSeq() {
            return batSeq;
        }

        public void setBatSeq(String batSeq) {
            this.batSeq = batSeq;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public LocalDate getFirstSubmitDate() {
            return firstSubmitDate;
        }

        public void setFirstSubmitDate(LocalDate firstSubmitDate) {
            this.firstSubmitDate = firstSubmitDate;
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

        public String getPayerAccountNumber() {
            return payerAccountNumber;
        }

        public void setPayerAccountNumber(String payerAccountNumber) {
            this.payerAccountNumber = payerAccountNumber;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getPeriodicalSeq() {
            return periodicalSeq;
        }

        public void setPeriodicalSeq(String periodicalSeq) {
            this.periodicalSeq = periodicalSeq;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTransMode() {
            return transMode;
        }

        public void setTransMode(String transMode) {
            this.transMode = transMode;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }
    }
}
