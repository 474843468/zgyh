package com.boc.bocsoft.mobile.wfss.common.model;

/**
 * 返回报文头
 * Created by lxw on 2016/11/2 0002.
 */
public class WFSSResponseHeader {

    // 操作结果
    private String stat;
    // 操作结果描述
    private String result;
    // 操作返回结果时间
    private String date;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
