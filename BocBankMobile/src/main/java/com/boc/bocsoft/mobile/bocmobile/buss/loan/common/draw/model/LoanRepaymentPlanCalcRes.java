package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 贷款管理-中银E贷-提款-还款计划试算
 * Created by xintong on 2016/6/16.
 */
public class LoanRepaymentPlanCalcRes {

    //还款总金额
    private BigDecimal totalAmount;
    //还本总金额
    private BigDecimal totalCapita;
    //还息总金额
    private BigDecimal totalInterest;
    //还款计划试算列表
    private List planLis;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalCapita() {
        return totalCapita;
    }

    public void setTotalCapita(BigDecimal totalCapita) {
        this.totalCapita = totalCapita;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public List getPlanLis() {
        return planLis;
    }

    public void setPlanLis(List planLis) {
        this.planLis = planLis;
    }
}
