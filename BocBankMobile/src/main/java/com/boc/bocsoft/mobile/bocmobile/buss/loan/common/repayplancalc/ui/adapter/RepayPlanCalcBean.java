package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.ui.adapter;

/**
 * 还款计划试算listview公共组件数据
 * Created by liuzc on 2016/8/30.
 */
public class RepayPlanCalcBean {
    private String repayDate = null; //还款日期
    private String repayAmount = null; //还款总额
    private String repayCapital = null; //还款本金
    private String repayInt = null; //还款利息

    public String getRepayInt() {
        return repayInt;
    }

    public void setRepayInt(String repayInt) {
        this.repayInt = repayInt;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getRepayCapital() {
        return repayCapital;
    }

    public void setRepayCapital(String repayCapital) {
        this.repayCapital = repayCapital;
    }
}
