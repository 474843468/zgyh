package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan;

import java.math.BigDecimal;
import java.util.List;

/**
 * 贷款管理-中银E贷-提款-还款计划试算返回参数
 * Created by xintong on 2016/6/16.
 */
public class PsnLoanRepaymentPlanResult {
    //还款总金额
    private BigDecimal totalAmount;

    //还本总金额
    private BigDecimal totalCapital;

    //还息总金额
    private BigDecimal totalInterest;

    //还款计划试算列表
    private List<ListBean> list;


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalCapital() {
        return totalCapital;
    }

    public void setTotalCapital(BigDecimal totalCapital) {
        this.totalCapital = totalCapital;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "PsnLoanRepaymentPlanResult{" +
                "totalAmount=" + totalAmount +
                ", totalCapital=" + totalCapital +
                ", totalInterest=" + totalInterest +
                ", planList=" + list +
                '}';
    }

    public static class ListBean {
        private String repayDate; //还款日期
        private BigDecimal remainAmount; //本期应还金额
        private BigDecimal remainCapital; //本期应还本金
        private String loanId; //期号
        private BigDecimal remainInterest; //本期应还利息
        private String loanAmount; //本期的贷款余额

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public BigDecimal getRemainAmount() {
            return remainAmount;
        }

        public void setRemainAmount(BigDecimal remainAmount) {
            this.remainAmount = remainAmount;
        }

        public BigDecimal getRemainCapital() {
            return remainCapital;
        }

        public void setRemainCapital(BigDecimal remainCapital) {
            this.remainCapital = remainCapital;
        }

        public String getLoanId() {
            return loanId;
        }

        public void setLoanId(String loanId) {
            this.loanId = loanId;
        }

        public BigDecimal getRemainInterest() {
            return remainInterest;
        }

        public void setRemainInterest(BigDecimal remainInterest) {
            this.remainInterest = remainInterest;
        }

        public String getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(String loanAmount) {
            this.loanAmount = loanAmount;
        }
    }
}
