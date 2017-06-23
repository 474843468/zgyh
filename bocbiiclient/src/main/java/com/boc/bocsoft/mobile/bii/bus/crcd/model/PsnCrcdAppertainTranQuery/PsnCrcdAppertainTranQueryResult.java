package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery;

import java.util.List;

/**
 * Name: liukai
 * Time：2016/12/2 15:42.
 * Created by lk7066 on 2016/12/2.
 * It's used to
 */

public class PsnCrcdAppertainTranQueryResult {

    private int recordNumber;

    private List<ListBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private String remark;

        private String transDate;

        private String transCode;

        private String bookCurrency;

        private String debitCreditFlag;

        private String bookDate;

        private String tranCurrency;

        private String tranAmount;

        private String bookAmount;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTransDate() {
            return transDate;
        }

        public void setTransDate(String transDate) {
            this.transDate = transDate;
        }

        public String getTransCode() {
            return transCode;
        }

        public void setTransCode(String transCode) {
            this.transCode = transCode;
        }

        public String getBookCurrency() {
            return bookCurrency;
        }

        public void setBookCurrency(String bookCurrency) {
            this.bookCurrency = bookCurrency;
        }

        public String getDebitCreditFlag() {
            return debitCreditFlag;
        }

        public void setDebitCreditFlag(String debitCreditFlag) {
            this.debitCreditFlag = debitCreditFlag;
        }

        public String getBookDate() {
            return bookDate;
        }

        public void setBookDate(String bookDate) {
            this.bookDate = bookDate;
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

        public String getBookAmount() {
            return bookAmount;
        }

        public void setBookAmount(String bookAmount) {
            this.bookAmount = bookAmount;
        }
    }
}
