package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANHistoryQuery;

import java.util.List;

/**
 * Created by huixiaobo on 2016/6/20.
 *历史还款查询返回参数
 */
public class PsnEHistoryQueryResult {
    /**总记录数*/
    private Integer recordNumber;
    /**历史还款信息列表*/
    private List<PsnEHistoryQueryBean> List;

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public java.util.List<PsnEHistoryQueryBean> getList() {
        return List;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(java.util.List<PsnEHistoryQueryBean> list) {
        List = list;
    }

    @Override
    public String toString() {
        return "PsnEHistoryQueryResult{" +
                "recordNumber=" + recordNumber +
                ", List=" + List +
                '}';
    }

    public class PsnEHistoryQueryBean {
        /**还款日期*/
        private String repayDate;
        /**期号*/
        private String loanId;
        /**交易类型*/
        private String transType;
        /**还款金额*/
        private String repayAmount;
        /**还款本金*/
        private String repayCapital;
        /**还款利息*/
        private String repayInterest;
        /**还款流水号*/
        private String repayId;
        /**贷款账号*/
        private String loanActNum;
        /**总罚息*/
        private String repayForfeit;
        /**利息罚息*/
        private String interestForfeit;

        public String getRepayDate() {
            return repayDate;
        }

        public String getLoanId() {
            return loanId;
        }

        public String getTransType() {
            return transType;
        }

        public String getRepayAmount() {
            return repayAmount;
        }

        public String getRepayCapital() {
            return repayCapital;
        }

        public String getRepayInterest() {
            return repayInterest;
        }

        public String getRepayId() {
            return repayId;
        }

        public String getLoanActNum() {
            return loanActNum;
        }

        public String getRepayForfeit() {
            return repayForfeit;
        }

        public String getInterestForfeit() {
            return interestForfeit;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public void setLoanId(String loanId) {
            this.loanId = loanId;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }

        public void setRepayCapital(String repayCapital) {
            this.repayCapital = repayCapital;
        }

        public void setRepayInterest(String repayInterest) {
            this.repayInterest = repayInterest;
        }

        public void setRepayId(String repayId) {
            this.repayId = repayId;
        }

        public void setLoanActNum(String loanActNum) {
            this.loanActNum = loanActNum;
        }

        public void setRepayForfeit(String repayForfeit) {
            this.repayForfeit = repayForfeit;
        }

        public void setInterestForfeit(String interestForfeit) {
            this.interestForfeit = interestForfeit;
        }

        @Override
        public String toString() {
            return "PsnEHistoryQueryBean{" +
                    "repayDate='" + repayDate + '\'' +
                    ", loanId='" + loanId + '\'' +
                    ", transType='" + transType + '\'' +
                    ", repayAmount='" + repayAmount + '\'' +
                    ", repayCapital='" + repayCapital + '\'' +
                    ", repayInterest='" + repayInterest + '\'' +
                    ", repayId='" + repayId + '\'' +
                    ", loanActNum='" + loanActNum + '\'' +
                    ", repayForfeit='" + repayForfeit + '\'' +
                    ", interestForfeit='" + interestForfeit + '\'' +
                    '}';
        }
    }

}
