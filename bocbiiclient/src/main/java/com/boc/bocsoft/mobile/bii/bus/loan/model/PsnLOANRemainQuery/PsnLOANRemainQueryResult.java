package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANRemainQuery;

import java.util.List;

/**
 * 其他类型贷款-还款计划-剩余还款计划返回参数
 * Created by liuzc on 2016/8/11.
 */
public class PsnLOANRemainQueryResult {
    private int recordNumber; //记录总数
    private List<ListBean> List;//剩余还款信息列表

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> list) {
        this.List = list;
    }

    public static class ListBean {

        /**
         * repayDate : 2015/05/06
         * remainAmount : 949.91
         * remainCapital : 245.74
         * loanId : 1
         * remainInterest : 704.17
         */

        private String repayDate; //还款日期
        private String remainAmount; //剩余金额
        private String remainCapital; //剩余本金
        private String loanId; //期号
        private String remainInterest; //剩余利息

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getRemainAmount() {
            return remainAmount;
        }

        public void setRemainAmount(String remainAmount) {
            this.remainAmount = remainAmount;
        }

        public String getRemainCapital() {
            return remainCapital;
        }

        public void setRemainCapital(String remainCapital) {
            this.remainCapital = remainCapital;
        }

        public String getLoanId() {
            return loanId;
        }

        public void setLoanId(String loanId) {
            this.loanId = loanId;
        }

        public String getRemainInterest() {
            return remainInterest;
        }

        public void setRemainInterest(String remainInterest) {
            this.remainInterest = remainInterest;
        }
    }

}
