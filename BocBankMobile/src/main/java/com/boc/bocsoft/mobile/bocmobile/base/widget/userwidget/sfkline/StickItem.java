package com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.sfkline;

import com.forms.androidcharts.entity.StickEntity;

/**
 * 均线数据结构
 * Created by yuht on 2016/10/21.
 */
public class StickItem {
    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private double high;
    private double low;
    private String date;


    /**  将均线数据结构 转换为四方的均线图数据接口 */
    public StickEntity toStickEntity(){
        StickEntity stick = new StickEntity();
        stick.setHigh(high);
        stick.setLow(low);
        stick.setDate(date);
        return stick;
    }
}
