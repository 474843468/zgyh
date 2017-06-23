package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueQuery;

import java.math.BigDecimal;
import java.util.List;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryResult.OverdueBean;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryResult.OverdueLoanObj;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryResult.OverdueLoanObj.OverdueLoanBean;

/**
 * 其他类型贷款-还款计划-逾期还款计划返回参数
 * Created by liuzc on 2016/8/11.
 */
public class PsnLOANOverdueQueryResult {
    /**
     * overdueAmountSum : 800
     * overdueCapitalSum : 100000
     * overdueForfeitSum : 660
     * overdueInterestSum : 1160
     * overdueIssueSum : 24
     */

 /*   private BigDecimal overdueAmountSum; //累计逾期未还款总额
    private BigDecimal overdueCapitalSum; //累计逾期本金
    private BigDecimal overdueInterestSum;//累计逾期利息
    private int overdueIssueSum; //累计逾期未还次数
    private List<ListBean> list;//逾期还款信息列表


    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public BigDecimal getOverdueAmountSum() {
        return overdueAmountSum;
    }

    public void setOverdueAmountSum(BigDecimal overdueAmountSum) {
        this.overdueAmountSum = overdueAmountSum;
    }

    public BigDecimal getOverdueCapitalSum() {
        return overdueCapitalSum;
    }

    public void setOverdueCapitalSum(BigDecimal overdueCapitalSum) {
        this.overdueCapitalSum = overdueCapitalSum;
    }

    public BigDecimal getOverdueInterestSum() {
        return overdueInterestSum;
    }

    public void setOverdueInterestSum(BigDecimal overdueInterestSum) {
        this.overdueInterestSum = overdueInterestSum;
    }

    public int getOverdueIssueSum() {
        return overdueIssueSum;
    }

    public void setOverdueIssueSum(int overdueIssueSum) {
        this.overdueIssueSum = overdueIssueSum;
    }

    public static class ListBean {

        *//**
         * overdueAmount : 10000
         * overdueCapital : 600000
         * overdueInterest : 2460
         * pymtDate : 2011/03/18
         *//*

        private BigDecimal overdueAmount;
        private BigDecimal overdueCapital;
        private BigDecimal overdueInterest;
        private String pymtDate;

        public BigDecimal getOverdueAmount() {
            return overdueAmount;
        }

        public void setOverdueAmount(BigDecimal overdueAmount) {
            this.overdueAmount = overdueAmount;
        }

        public BigDecimal getOverdueCapital() {
            return overdueCapital;
        }

        public void setOverdueCapital(BigDecimal overdueCapital) {
            this.overdueCapital = overdueCapital;
        }

        public BigDecimal getOverdueInterest() {
            return overdueInterest;
        }

        public void setOverdueInterest(BigDecimal overdueInterest) {
            this.overdueInterest = overdueInterest;
        }

        public String getPymtDate() {
            return pymtDate;
        }

        public void setPymtDate(String pymtDate) {
            this.pymtDate = pymtDate;
        }
    }*/
    
    
    /**逾期数*/
    private String recordNumber;
    /**逾期贷款对象*/
    private OverdueLoanObj overdueLoanObj;

    private List<OverdueBean> List;
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
