package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.otherloanqrylistview;

/**
 * 其他类型贷款进度查询listview公共组件数据
 * Created by liuzc on 2016/8/15.
 */
public class OtherLoanQryBean {
    private String firstLineInfo = null; //第一行显示信息
    private String secondLineLeftInfo = null; //第二行左侧显示信息
    private String secondLinerightFirstInfo = null; //第二行右侧第一条显示信息
    private String secondLinerightSecInfo = null; //第二行右侧第二条显示信息

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

    public String getSecondLinerightFirstInfo() {
        return secondLinerightFirstInfo;
    }

    public void setSecondLinerightFirstInfo(String secondLinerightFirstInfo) {
        this.secondLinerightFirstInfo = secondLinerightFirstInfo;
    }

    public String getSecondLinerightSecInfo() {
        return secondLinerightSecInfo;
    }

    public void setSecondLinerightSecInfo(String secondLinerightSecInfo) {
        this.secondLinerightSecInfo = secondLinerightSecInfo;
    }

}
