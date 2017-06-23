package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTrans;

import java.util.List;

/**
 * Created by liuweidong on 2016/12/14.
 */

public class PsnCrcdQueryUnauthorizedTransResult {
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
        private String bookAmount;// 记账金额
        private String debitCreditFlag;// 借贷标识
        private String cardNumberTail;// 卡号
        private String tranCurrency;// 交易币种
        private String tranAmount;// 交易金额
        private String remark;// 摘要
        private String tranCode;// 交易类型码

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

        public String getTranCode() {
            return tranCode;
        }

        public void setTranCode(String tranCode) {
            this.tranCode = tranCode;
        }
    }
}
