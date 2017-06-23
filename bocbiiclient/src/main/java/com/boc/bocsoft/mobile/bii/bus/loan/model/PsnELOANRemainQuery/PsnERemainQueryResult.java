package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRemainQuery;

import java.util.List;

/**
 * Created by huixiaobo on 2016/6/20.
 *剩余还款信息查询返回参数
 */
public class PsnERemainQueryResult {

    /**总记录数*/
    private Integer recordNumber;
    /**剩余还款信息列表*/
   private List<PsnERemainQueryBean> List;

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public java.util.List<PsnERemainQueryBean> getList() {
        return List;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(java.util.List<PsnERemainQueryBean> list) {
        List = list;
    }

    @Override
    public String toString() {
        return "PsnERemainQueryResult{" +
                "recordNumber=" + recordNumber +
                ", List=" + List +
                '}';
    }

    public class PsnERemainQueryBean {
        /**还款日期*/
        private String repayDate;
        /**期号*/
        private String loanId;
        /**剩余金额*/
        private String remainAmount;
        /**剩余本金*/
        private String remainCapital;
        /**剩余利息*/
        private String remainInterest;

        public String getRepayDate() {
            return repayDate;
        }

        public String getLoanId() {
            return loanId;
        }

        public String getRemainAmount() {
            return remainAmount;
        }

        public String getRemainCapital() {
            return remainCapital;
        }

        public String getRemainInterest() {
            return remainInterest;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public void setLoanId(String loanId) {
            this.loanId = loanId;
        }

        public void setRemainAmount(String remainAmount) {
            this.remainAmount = remainAmount;
        }

        public void setRemainCapital(String remainCapital) {
            this.remainCapital = remainCapital;
        }

        public void setRemainInterest(String remainInterest) {
            this.remainInterest = remainInterest;
        }

        @Override
        public String toString() {
            return "PsnERemainQueryBean{" +
                    "repayDate='" + repayDate + '\'' +
                    ", loanId='" + loanId + '\'' +
                    ", remainAmount='" + remainAmount + '\'' +
                    ", remainCapital='" + remainCapital + '\'' +
                    ", remainInterest='" + remainInterest + '\'' +
                    '}';
        }
    }

}
