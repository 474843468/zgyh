package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.hostoryrecordlistview;

/**
 * 还款计划listview公共组件数据
 *
 */
public class HostoryRecordBean {
    private String firstLineTopInfo = null; //第一行顶部显示信息
    private String firstLineBottomInfo = null;//第一行底部部显示信息
    private String secondLineLeftInfo = null; //第二行左侧显示信息
    private String secondLinerightInfo = null; //第二行右侧显示信息

    public String getFirstLineBottomInfo() {
        return firstLineBottomInfo;
    }

    public void setFirstLineBottomInfo(String firstLineBottomInfo) {
        this.firstLineBottomInfo = firstLineBottomInfo;
    }

    public String getFirstLineTopInfo() {
        return firstLineTopInfo;
    }

    public void setFirstLineTopInfo(String firstLineTopInfo) {
        this.firstLineTopInfo = firstLineTopInfo;
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
