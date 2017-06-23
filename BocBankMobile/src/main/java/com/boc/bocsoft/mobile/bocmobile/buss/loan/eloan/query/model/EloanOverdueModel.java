package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;

import java.util.List;

/**
 * Created by louis.hui on 2016/11/10.
 */
public class EloanOverdueModel  {

    /**逾期数*/
    private String recordNumber;
    /**逾期贷款对象*/
    private OverdueLoanObj overdueLoanObj;

    private java.util.List<OverdueBean> List;
    public class OverdueBean{};

    public String getRecordNumber() {
        return recordNumber;
    }

    public OverdueLoanObj getOverdueLoanObj() {
        return overdueLoanObj;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public java.util.List<OverdueBean> getList() {
        return List;
    }

    public void setList(java.util.List<OverdueBean> list) {
        List = list;
    }

    public void setOverdueLoanObj(OverdueLoanObj overdueLoanObj) {
        this.overdueLoanObj = overdueLoanObj;
    }

    @Override
    public String toString() {
        return "PsnEOverdueQueryResult{" +
                "recordNumber='" + recordNumber + '\'' +
                ", overdueLoanObj=" + overdueLoanObj +
                ", List=" + List +
                '}';
    }

    public class OverdueLoanObj {
        /**应还款日期*/
        private String capitalForfeitSum;
        /**期号*/
        private String compoundInterestSum;
        /**逾期金额*/
        private String entOverdueInterestSum;
        /**逾期本金*/
        private String interestForfeitSum;
        /**逾期利息*/
        private String overdueAmountSum;
        private String overdueCapitalSum ;
        private String overdueForfeitSum ;
        private String overdueInterestSum ;
        private String overdueIssueSum ;
        private List<OverdueLoanBean> overdueList;

        public String getCapitalForfeitSum() {
            return capitalForfeitSum;
        }

        public String getCompoundInterestSum() {
            return compoundInterestSum;
        }

        public String getEntOverdueInterestSum() {
            return entOverdueInterestSum;
        }

        public String getInterestForfeitSum() {
            return interestForfeitSum;
        }

        public String getOverdueAmountSum() {
            return overdueAmountSum;
        }

        public String getOverdueCapitalSum() {
            return overdueCapitalSum;
        }

        public String getOverdueForfeitSum() {
            return overdueForfeitSum;
        }

        public String getOverdueInterestSum() {
            return overdueInterestSum;
        }

        public String getOverdueIssueSum() {
            return overdueIssueSum;
        }

        public List<OverdueLoanBean> getOverdueList() {
            return overdueList;
        }

        public void setCapitalForfeitSum(String capitalForfeitSum) {
            this.capitalForfeitSum = capitalForfeitSum;
        }

        public void setCompoundInterestSum(String compoundInterestSum) {
            this.compoundInterestSum = compoundInterestSum;
        }

        public void setEntOverdueInterestSum(String entOverdueInterestSum) {
            this.entOverdueInterestSum = entOverdueInterestSum;
        }

        public void setInterestForfeitSum(String interestForfeitSum) {
            this.interestForfeitSum = interestForfeitSum;
        }

        public void setOverdueAmountSum(String overdueAmountSum) {
            this.overdueAmountSum = overdueAmountSum;
        }

        public void setOverdueCapitalSum(String overdueCapitalSum) {
            this.overdueCapitalSum = overdueCapitalSum;
        }

        public void setOverdueInterestSum(String overdueInterestSum) {
            this.overdueInterestSum = overdueInterestSum;
        }

        public void setOverdueForfeitSum(String overdueForfeitSum) {
            this.overdueForfeitSum = overdueForfeitSum;
        }

        public void setOverdueIssueSum(String overdueIssueSum) {
            this.overdueIssueSum = overdueIssueSum;
        }

        public void setOverdueList(List<OverdueLoanBean> overdueList) {
            this.overdueList = overdueList;
        }

        @Override
        public String toString() {
            return "OverdueLoanObj{" +
                    "capitalForfeitSum='" + capitalForfeitSum + '\'' +
                    ", compoundInterestSum='" + compoundInterestSum + '\'' +
                    ", entOverdueInterestSum='" + entOverdueInterestSum + '\'' +
                    ", interestForfeitSum='" + interestForfeitSum + '\'' +
                    ", overdueAmountSum='" + overdueAmountSum + '\'' +
                    ", overdueCapitalSum='" + overdueCapitalSum + '\'' +
                    ", overdueForfeitSum='" + overdueForfeitSum + '\'' +
                    ", overdueInterestSum='" + overdueInterestSum + '\'' +
                    ", overdueIssueSum='" + overdueIssueSum + '\'' +
                    ", overdueList=" + overdueList +
                    '}';
        }

        public class OverdueLoanBean {
            /***/
            private String loanId;
            /***/
            private String overdueAmount;
            /***/
            private String overdueCapital;
            /***/
            private String overdueForfeit;
            /***/
            private String overdueInterest;
            /***/
            private String pymtDate;

            public String getLoanId() {
                return loanId;
            }

            public String getOverdueAmount() {
                return overdueAmount;
            }

            public String getOverdueCapital() {
                return overdueCapital;
            }

            public String getOverdueForfeit() {
                return overdueForfeit;
            }

            public String getOverdueInterest() {
                return overdueInterest;
            }

            public String getPymtDate() {
                return pymtDate;
            }

            public void setLoanId(String loanId) {
                this.loanId = loanId;
            }

            public void setOverdueAmount(String overdueAmount) {
                this.overdueAmount = overdueAmount;
            }

            public void setOverdueCapital(String overdueCapital) {
                this.overdueCapital = overdueCapital;
            }

            public void setOverdueForfeit(String overdueForfeit) {
                this.overdueForfeit = overdueForfeit;
            }

            public void setOverdueInterest(String overdueInterest) {
                this.overdueInterest = overdueInterest;
            }

            public void setPymtDate(String pymtDate) {
                this.pymtDate = pymtDate;
            }

            @Override
            public String toString() {
                return "OverdueLoanBean{" +
                        "loanId='" + loanId + '\'' +
                        ", overdueAmount='" + overdueAmount + '\'' +
                        ", overdueCapital='" + overdueCapital + '\'' +
                        ", overdueForfeit='" + overdueForfeit + '\'' +
                        ", overdueInterest='" + overdueInterest + '\'' +
                        ", pymtDate='" + pymtDate + '\'' +
                        '}';
            }
        }
    }
}
