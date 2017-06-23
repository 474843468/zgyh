package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBill;

import java.util.List;

/**
 * 4.4 004查询信用卡未出账单PsnCrcdQueryFutureBill
 * Created by liuweidong on 2016/12/14.
 */

public class PsnCrcdQueryFutureBillResult {
    private String recordNumber;
    private List<CrcdTransactionListBean> crcdTransactionList;

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<CrcdTransactionListBean> getCrcdTransactionList() {
        return crcdTransactionList;
    }

    public void setCrcdTransactionList(List<CrcdTransactionListBean> crcdTransactionList) {
        this.crcdTransactionList = crcdTransactionList;
    }

    public static class CrcdTransactionListBean{
        private String transDate;// 交易日期
        private String bookCurrency;// 记账币种
        private String bookDate;// 记账日期
        private String bookAmount;// 记账金额
        private String debitCreditFlag;// 借贷标识
        private String cardNumberTail;// 卡号
        private String tranCurrency;// 交易币种
        private String tranAmount;// 交易金额
        private String remark;// 摘要
        private String transCode;// 借方合计

        public String getTransDate() {
            return transDate;
        }

        public void setTransDate(String transDate) {
            this.transDate = transDate;
        }

        public String getBookCurrency() {
            return bookCurrency;
        }

        public void setBookCurrency(String bookCurrency) {
            this.bookCurrency = bookCurrency;
        }

        public String getBookDate() {
            return bookDate;
        }

        public void setBookDate(String bookDate) {
            this.bookDate = bookDate;
        }

        public String getBookAmount() {
            return bookAmount;
        }

        public void setBookAmount(String bookAmount) {
            this.bookAmount = bookAmount;
        }

        public String getDebitCreditFlag() {
            return debitCreditFlag;
        }

        public void setDebitCreditFlag(String debitCreditFlag) {
            this.debitCreditFlag = debitCreditFlag;
        }

        public String getCardNumberTail() {
            return cardNumberTail;
        }

        public void setCardNumberTail(String cardNumberTail) {
            this.cardNumberTail = cardNumberTail;
        }

        public String getTranCurrency() {
            return tranCurrency;
        }

        public void setTranCurrency(String tranCurrency) {
            this.tranCurrency = tranCurrency;
        }

        public String getTranAmount() {
            return tranAmount;
        }

        public void setTranAmount(String tranAmount) {
            this.tranAmount = tranAmount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTransCode() {
            return transCode;
        }

        public void setTransCode(String transCode) {
            this.transCode = transCode;
        }
    }
}
