package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.model;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * 还款计划试算返回
 * Created by liuzc on 2016/8/30.
 */
public class RepayPlanCalcRes {

    //还款总金额
    private BigDecimal totalAmount;

    //还本总金额
    private BigDecimal totalCapital;

    //还息总金额
    private BigDecimal totalInterest;

    //还款计划试算列表
    private List<PsnLoanRepaymentPlanResult.ListBean> planList;


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

    public List<PsnLoanRepaymentPlanResult.ListBean> getList() {
        return planList;
    }

    public void setList(List<PsnLoanRepaymentPlanResult.ListBean> list) {
        planList = list;
    }


}
