package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayplanlistview;

/**
 * 还款计划listview公共组件数据
 * Created by liuzc on 2016/8/10.
 */
public class RepayPlanBean {
    private String firstLineInfo = null; //第一行显示信息
    private String secondLineLeftInfo = null; //第二行左侧显示信息
    private String secondLinerightInfo = null; //第二行右侧显示信息

    public String getFirstLineInfo() {
        return firstLineInfo;
    }

    public void setFirstLineInfo(String firstLineInfo) {
        this.firstLineInfo = firstLineInfo;
    }

    public String getSecondLineLeftInfo() {
        return secondLineLeftInfo;
    }

    public void setSecondLineLeftInfo(String secondLineLeftInfo) {
        this.secondLineLeftInfo = secondLineLeftInfo;
    }

    public String getSecondLinerightInfo() {
        return secondLinerightInfo;
    }

    public void setSecondLinerightInfo(String secondLinerightInfo) {
        this.secondLinerightInfo = secondLinerightInfo;
    }

}
