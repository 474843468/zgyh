package com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.sfkline;

import com.forms.androidcharts.entity.OHLCEntity;

/**
 * 柱状图数据结构
 * Created by yuht on 2016/10/21.
 */
public class OHLCItem {
    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

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

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String timeStamp;
    private double open;
    private double high;
    private double low;
    private double close;
    private String date;

    /**  将柱状图数据结构 转换为四方的柱状图数据接口 */
    public OHLCEntity toOHLCEntity(){
        OHLCEntity ohlc = new OHLCEntity();
        ohlc.setOpen(open);
        ohlc.setClose(close);
        ohlc.setHigh(high);
        ohlc.setLow(low);
        ohlc.setDate(date);
        return ohlc;
    }


}
